package org.modelmap.gen;

import static java.nio.file.Files.createDirectories;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;
import static java.time.format.FormatStyle.SHORT;
import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;
import static org.modelmap.gen.FieldInfoGen.literals;
import static org.modelmap.gen.ModelWrapperGen.mapFieldTypeIfStatement;
import static org.modelmap.gen.ModelWrapperGen.mapGetter;
import static org.modelmap.gen.ModelWrapperGen.mapSetter;
import static org.modelmap.gen.ModelWrapperGen.validatePath;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import org.apache.maven.plugin.*;
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

    @Parameter(required = true, property = "project.build.sourceDirectory")
    private File outputDirectory;

    @Parameter(required = true, property = "project.build.outputDirectory")
    private File buildDirectory;

    @Parameter(required = true, property = "project.build.outputDirectory")
    private File buildResourceDirectory;

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
        try {
            for (int i = 0; i < sourceClasses.size(); i++) {
                @SuppressWarnings("unchecked")
                final Class<? extends FieldId> fieldClazz = (Class<? extends FieldId>)
                                Class.forName(fieldClasses.get(i), true, classLoader);
                final Class<?> modelClazz = Class.forName(sourceClasses.get(i), true, classLoader);
                final List<VisitorPath> collected = process(modelClazz, packageFilter);
                final Map<FieldId, VisitorPath> fieldPaths = validatePath(collected);
                generateCsv(fieldPaths, modelClazz);
                generateWrapper(fieldPaths, modelClazz, fieldClazz);
                generateFieldInfo(fieldPaths, fieldClazz);
            }
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private List<VisitorPath> process(Class<?> projetClass, String filter) throws Exception {
        final List<VisitorPath> collected = new ArrayList<>();
        new ModelVisitor(getLog()).visitModel(projetClass, new Visitor(projetClass, collected), filter);
        return collected;
    }

    private void generateCsv(Map<FieldId, VisitorPath> fieldPaths, Class<?> clazz) throws IOException,
                    MojoExecutionException {
        final File targetFile = new File(buildResourceDirectory, clazz.getSimpleName() + ".csv");
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

    private void generateFieldInfo(Map<FieldId, VisitorPath> fieldPaths, Class<?> clazz) throws IOException {
        final String targetClassName = clazz.getSimpleName() + "Info";
        final String targetPackage = clazz.getPackage().getName();
        final File targetFile = new File(outputDirectory + "/" + targetPackage.replace('.', '/'), targetClassName
                        + ".java");
        final String classTemplate = template("FieldInfoEnum.template");
        createDirectories(targetFile.getParentFile().toPath());
        final Map<String, String> conf = new HashMap<>();
        conf.put("package.name", targetPackage);
        conf.put("process.class", clazz.getName());
        conf.put("process.date", ofLocalizedDateTime(SHORT).format(now()));
        conf.put("target.class.name", targetClassName);
        conf.put("literals", literals(fieldPaths));
        conf.put("source.generator.name", getClass().getName());
        final String content = MacroProcessor.replaceProperties(classTemplate, conf);
        Files.write(content.getBytes(), targetFile);
        getLog().info("written : " + targetFile);
    }

    private void generateWrapper(Map<FieldId, VisitorPath> fieldPaths, Class<?> modelClass, Class<?> fieldClass)
                    throws IOException {
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
        conf.put("target.field.info.class.name", fieldClass.getSimpleName() + "Info");
        conf.put("target.class.name", targetClassName);
        conf.put("map.getter", mapGetter(fieldPaths, modelClass));
        conf.put("map.getter.if", mapFieldTypeIfStatement("MapGetIfStatement.template", fieldPaths));
        conf.put("map.setter", mapSetter(fieldPaths, modelClass));
        conf.put("map.setter.if", mapFieldTypeIfStatement("MapSetIfStatement.template", fieldPaths));
        conf.put("source.generator.name", getClass().getName());

        String content = MacroProcessor.replaceProperties(classTemplate, conf);
        Files.write(content.getBytes(), targetFile);
        getLog().info("written : " + targetFile);
    }
}
