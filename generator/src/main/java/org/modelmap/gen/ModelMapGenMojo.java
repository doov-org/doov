package org.modelmap.gen;

import static java.nio.file.Files.createDirectories;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;
import static java.time.format.FormatStyle.SHORT;
import static java.util.Arrays.asList;
import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;
import static org.modelmap.gen.FieldInfoGen.constants;
import static org.modelmap.gen.FieldInfoGen.imports;
import static org.modelmap.gen.ModelWrapperGen.mapFieldProperties;
import static org.modelmap.gen.ModelWrapperGen.mapFieldTypeIfStatement;
import static org.modelmap.gen.ModelWrapperGen.mapGetter;
import static org.modelmap.gen.ModelWrapperGen.mapSetter;
import static org.modelmap.gen.ModelWrapperGen.validatePath;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.modelmap.core.FieldId;
import org.modelmap.gen.processor.MacroProcessor;
import org.modelmap.gen.utils.ClassLoaderUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

@Mojo(name = "generate", defaultPhase = GENERATE_SOURCES, threadSafe = true)
public final class ModelMapGenMojo extends AbstractMojo {

    @Parameter(required = true, defaultValue = "${basedir}/src/generated/java")
    private File outputDirectory;

    @Parameter(required = true, property = "project.build.outputDirectory")
    private File buildDirectory;

    @Parameter(required = true, defaultValue = "${basedir}/target")
    private File outputResourceDirectory;

    @Parameter(required = true)
    private List<String> sourceClasses;

    @Parameter(required = true)
    private List<String> fieldClasses;

    @Parameter(required = true, readonly = true, property = "project")
    private MavenProject project;

    @Parameter(required = true)
    private String packageFilter;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (sourceClasses == null)
            getLog().warn("no project classes");
        if (sourceClasses.isEmpty())
            getLog().warn("project is empty");
        if (fieldClasses == null)
            getLog().warn("no tunnel classes");
        if (fieldClasses.isEmpty())
            getLog().warn("tunnel is empty");
        if (fieldClasses.size() != sourceClasses.size())
            getLog().warn("tunnel and projet have different size");
        if (outputDirectory.exists() && !outputDirectory.isDirectory())
            throw new MojoFailureException(outputDirectory + " is not directory");

        // add source directory to current project
        try {
            createDirectories(outputDirectory.toPath());
            project.addCompileSourceRoot(outputDirectory.getPath());
        } catch (IOException e) {
            throw new MojoExecutionException("unable to create source folder", e);
        }

        final URLClassLoader classLoader = ClassLoaderUtils.getUrlClassLoader(project);
        final Map<Class<? extends FieldId>, Class<?>> generationInput = new HashMap<>();
        try {
            for (int i = 0; i < sourceClasses.size(); i++) {
                @SuppressWarnings("unchecked")
                final Class<? extends FieldId> fieldClazz = (Class<? extends FieldId>) Class
                                .forName(fieldClasses.get(i), true, classLoader);
                final Class<?> modelClazz = Class.forName(sourceClasses.get(i), true, classLoader);
                generationInput.put(fieldClazz, modelClazz);
            }
            generateModels(generationInput);
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

    }

    private void generateModels(Map<Class<? extends FieldId>, Class<?>> generationInput) {
        new ArrayList<>(generationInput.entrySet()).parallelStream().forEach(entry -> {
            final Class<? extends FieldId> fieldClazz = entry.getKey();
            final Class<?> modelClazz = entry.getValue();
            try {
                final List<VisitorPath> collected = process(modelClazz, packageFilter);
                final Map<FieldId, VisitorPath> fieldPaths = validatePath(collected);
                Runnable generateCsv = () -> generateCsv(fieldPaths, modelClazz);
                Runnable generateWrapper = () -> generateWrapper(fieldPaths, modelClazz, fieldClazz);
                Runnable generateFieldInfo = () -> generateFieldInfo(fieldPaths, fieldClazz);
                asList(generateWrapper, generateCsv, generateFieldInfo).parallelStream().forEach(Runnable::run);
            } catch (Exception e) {
                throw new RuntimeException("generation failed for class " + modelClazz, e);
            }
        });
    }

    private List<VisitorPath> process(Class<?> projetClass, String filter) throws Exception {
        final List<VisitorPath> collected = new ArrayList<>();
        new ModelVisitor(getLog()).visitModel(projetClass, new Visitor(projetClass, collected), filter);
        return collected;
    }

    private void generateCsv(Map<FieldId, VisitorPath> fieldPaths, Class<?> clazz) {
        final File targetFile = new File(outputResourceDirectory, clazz.getSimpleName() + ".csv");
        targetFile.getParentFile().mkdirs();
        FieldCsvGen.write(targetFile, fieldPaths);
        getLog().info("written : " + targetFile);
    }

    static String template(String template) {
        try {
            URL resource = Resources.getResource(ModelMapGenMojo.class, template);
            return Resources.toString(resource, Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("failed to load template " + template, e);
        }
    }

    private void generateFieldInfo(Map<FieldId, VisitorPath> fieldPaths, Class<?> clazz) {
        try {
            final String targetClassName = fieldInfoClassName(clazz);
            final String targetPackage = clazz.getPackage().getName();
            final File targetFile = new File(outputDirectory + "/" + targetPackage.replace('.', '/'),
                            targetClassName + ".java");
            final String classTemplate = template("FieldInfo.template");
            createDirectories(targetFile.getParentFile().toPath());
            final Map<String, String> conf = new HashMap<>();
            conf.put("package.name", targetPackage);
            conf.put("process.class", clazz.getName());
            conf.put("process.date", ofLocalizedDateTime(SHORT).format(now()));
            conf.put("target.class.name", targetClassName);
            conf.put("imports", imports(fieldPaths));
            conf.put("constants", constants(fieldPaths));
            conf.put("source.generator.name", getClass().getName());
            final String content = MacroProcessor.replaceProperties(classTemplate, conf);
            Files.write(content.getBytes(), targetFile);
            getLog().info("written : " + targetFile);
        } catch (IOException e) {
            throw new RuntimeException("error when generating wrapper", e);
        }
    }

    private static final String fieldInfoClassName(Class<?> clazz) {
        return clazz.getSimpleName().startsWith("E") ? clazz.getSimpleName().substring(1)
                        : clazz.getSimpleName() + "Info";
    }

    private void generateWrapper(Map<FieldId, VisitorPath> fieldPaths, Class<?> modelClass, Class<?> fieldClass)
                    throws RuntimeException {
        try {
            final String targetClassName = modelClass.getSimpleName() + "Wrapper";
            final String targetPackage = modelClass.getPackage().getName();
            final File targetFile = new File(outputDirectory + "/" + targetPackage.replace('.', '/'),
                            targetClassName + ".java");
            final String classTemplate = template("WrapperClass.template");

            createDirectories(targetFile.getParentFile().toPath());

            Map<String, String> conf = new HashMap<>();
            conf.put("package.name", targetPackage);
            conf.put("process.class", modelClass.getName());
            conf.put("process.date", ofLocalizedDateTime(SHORT).format(now()));
            conf.put("target.model.class.name", modelClass.getSimpleName());
            conf.put("target.model.class.full.name", modelClass.getName());
            conf.put("target.field.info.package.name", fieldClass.getPackage().getName());
            conf.put("target.field.info.class.name", fieldInfoClassName(fieldClass));
            conf.put("target.class.name", targetClassName);
            conf.put("map.getter", mapGetter(fieldPaths));
            conf.put("map.getter.if", mapFieldTypeIfStatement("MapGetIfStatement.template", fieldPaths));
            conf.put("map.setter", mapSetter(fieldPaths));
            conf.put("map.setter.if", mapFieldTypeIfStatement("MapSetIfStatement.template", fieldPaths));
            conf.put("map.properties", mapFieldProperties(fieldPaths, modelClass));
            conf.put("source.generator.name", getClass().getName());

            String content = MacroProcessor.replaceProperties(classTemplate, conf);
            Files.write(content.getBytes(), targetFile);
            getLog().info("written : " + targetFile);
        } catch (IOException e) {
            throw new RuntimeException("error when generating wrapper", e);
        }
    }
}
