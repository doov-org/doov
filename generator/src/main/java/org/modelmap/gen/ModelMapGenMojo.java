package org.modelmap.gen;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.modelmap.core.FieldId;
import org.modelmap.gen.temp.PropertyParsingException;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import static java.nio.file.Files.createDirectories;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;
import static java.time.format.FormatStyle.SHORT;
import static org.apache.maven.plugins.annotations.LifecyclePhase.INSTALL;
import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE;
import static org.modelmap.gen.FieldInfoGen.literals;
import static org.modelmap.gen.ProjetWrapperGen.*;
import static org.modelmap.gen.temp.MacroProcessor.replaceProperties;

@Mojo(name = "generate", defaultPhase = INSTALL, threadSafe = true, requiresDependencyResolution = COMPILE)
public final class ModelMapGenMojo extends AbstractMojo {

    @Parameter(required = true, property = "project.build.sourceDirectory")
    private File outputDirectory;

    @Parameter(required = true, property = "project.build.outputDirectory")
    private File buildDirectory;

    @Parameter(required = true, property = "project.build.outputDirectory")
    private File buildResourceDirectory;

    @Parameter(required = true)
    private List<String> projectClasses;

    @Parameter(required = true)
    private List<String> tunnelClasses;

    @Parameter(required = true, readonly = true, property = "project")
    private MavenProject project;

    @Parameter(required = true, readonly = true, defaultValue = "net.courtanet")
    private String packageFilter;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (projectClasses == null)
            getLog().warn("no project classes");
        if (projectClasses.isEmpty())
            getLog().warn("project is empty");
        if (tunnelClasses == null)
            getLog().warn("no tunnel classes");
        if (tunnelClasses.isEmpty())
            getLog().warn("tunnel is empty");
        if (tunnelClasses.size() != projectClasses.size())
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

        final List<?> classpathFiles;
        try {
            classpathFiles = project.getCompileClasspathElements();
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoFailureException(e.getMessage());
        }
        final URL[] urls = new URL[classpathFiles.size() + 1];
        try {
            for (int i = 0; i < classpathFiles.size(); ++i) {
                getLog().debug((String) classpathFiles.get(i));
                urls[i] = new File((String) classpathFiles.get(i)).toURI().toURL();
            }
            urls[classpathFiles.size()] = new File(buildDirectory + "/classes").toURI().toURL();
        } catch (MalformedURLException e) {
            throw new MojoFailureException(e.getMessage(), e);
        }

        final URLClassLoader classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
        try {
            for (int i = 0; i < projectClasses.size(); i++) {
                final Class<?> tunnelClazz = Class.forName(tunnelClasses.get(i), true, classLoader);
                final List<FieldId> fieldsOrder = fieldsOrder(tunnelClazz);
                final Class<?> projectClazz = Class.forName(projectClasses.get(i), true, classLoader);
                final List<VisitorPath> collected = process(projectClazz, packageFilter);
                generateCsv(collected, projectClazz);
                generateProjetWrapper(collected, projectClazz);
                generateFieldInfo(collected, fieldsOrder, projectClazz);
            }
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private static List<FieldId> fieldsOrder(Class<?> tunnelClazz) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        final List<FieldId> fields = new ArrayList<>();
        // FIXME import all fields for annotations? from annotations?
        // final TunnelID[] tunnels = (TunnelID[]) tunnelClazz.getMethod("values").invoke(null);
        //        for (TunnelID tunnelId : tunnels) {
        //            for (ElementID screenId : tunnelId.screens()) {
        //                fields.addAll(screenId.fields());
        //            }
        //        }
        return fields;
    }

    private static List<VisitorPath> process(Class<?> projetClass, String packageFilter)
            throws IllegalArgumentException, SecurityException, IllegalAccessException,
            InvocationTargetException, IntrospectionException {
        final List<VisitorPath> collected = new ArrayList<>();
        ModelVisitor.visitModel(projetClass, new Visitor(projetClass, collected), packageFilter);
        return collected;
    }

    private void generateCsv(List<VisitorPath> collected, Class<?> clazz) throws IOException, MojoExecutionException {
        final File targetFile = new File(buildResourceDirectory, clazz.getSimpleName() + ".csv");
        targetFile.getParentFile().mkdirs();
        try (FileWriter writter = new FileWriter(targetFile)) {
            for (VisitorPath path : collected) {
                writter.write(path.toCsv());
            }
            for (FieldId field : fieldsWithoutPath(collected)) {
                writter.write("NO_PATH;");
                writter.write(field.toString());
                writter.write('\n');
            }
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
        getLog().info("written : " + targetFile);
    }

    static String template(String template) throws IOException {
        URL resource = Resources.getResource(ModelMapGenMojo.class, template);
        return Resources.toString(resource, Charsets.UTF_8);
    }

    private void generateFieldInfo(List<VisitorPath> collected, List<FieldId> fieldsOrder, Class<?> clazz)
            throws IOException, PropertyParsingException {
        final String targetClassName = "E" + clazz.getSimpleName().replace("Projet", "Field") + "Info";
        final String targetPackage = targetPackage(clazz.getPackage(), true);
        final File targetFile = new File(outputDirectory + "/" + targetPackage.replace('.', '/'), targetClassName
                + ".java");
        final String classTemplate = template("EFieldInfoEnum.template");
        createDirectories(targetFile.getParentFile().toPath());
        final Map<String, String> conf = new HashMap<>();
        conf.put("package.name", targetPackage);
        conf.put("process.class", clazz.getName());
        conf.put("process.date", ofLocalizedDateTime(SHORT).format(now()));
        conf.put("target.class.name", targetClassName);
        conf.put("literals", literals(fieldsOrder, collected));
        conf.put("source.generator.name", getClass().getName());
        final String content = replaceProperties(classTemplate, conf, MISSING_VALUE);
        Files.write(content.getBytes(), targetFile);
        getLog().info("written : " + targetFile);
    }

    private void generateProjetWrapper(List<VisitorPath> collected, Class<?> clazz) throws IOException,
            PropertyParsingException {
        final String targetClassName = clazz.getSimpleName() + "Wrapper";
        final String targetPackage = targetPackage(clazz.getPackage(), false);
        final File targetFile = new File(outputDirectory + "/" + targetPackage.replace('.', '/'), targetClassName
                + ".java");
        final String classTemplate = template("ProjetWrapperClass.template");
        createDirectories(targetFile.getParentFile().toPath());
        final Map<String, String> conf = new HashMap<>();
        conf.put("package.name", targetPackage);
        conf.put("process.class", clazz.getName());
        conf.put("process.date", ofLocalizedDateTime(SHORT).format(now()));
        conf.put("target.project.class.name", clazz.getSimpleName());
        conf.put("target.project.class.full.name", clazz.getName());
        // FIXME supported tempalte params in a better way
//        conf.put("project.template.params", extractTemplateParams(clazz));
//        conf.put("context.template.params", extractFormulePrimeTemplateParams(clazz));
        conf.put("target.field.info.class.name", "E" + clazz.getSimpleName().replace("Projet", "Field") + "Info");
        conf.put("target.class.name", targetClassName);
        conf.put("map.getter", mapGetter(collected));
        conf.put("map.getter.if", mapFieldTypeIfStatement("MapGetIfStatement.template", collected));
        conf.put("map.setter", mapSetter(collected));
        conf.put("map.setter.if", mapFieldTypeIfStatement("MapSetIfStatement.template", collected));
        conf.put("source.generator.name", getClass().getName());
        final String content = replaceProperties(classTemplate, conf, MISSING_VALUE);
        Files.write(content.getBytes(), targetFile);
        getLog().info("written : " + targetFile);
    }

//    private static String extractTemplateParams(Class<?> clazz) {
//        return asList(((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()).stream()
//                .map(t -> t.getTypeName()).collect(joining(", "));
//    }
//
//    private static String extractFormulePrimeTemplateParams(Class<?> clazz) {
//        return asList(((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()).stream()
//                .filter(t -> formulePrime(t)).map(t -> t.getTypeName()).collect(joining(", "));
//    }
//    private static boolean formulePrime(Type type) {
//        try {
//            return Formule.class.isAssignableFrom(Class.forName(type.getTypeName()))
//                    || Prime.class.isAssignableFrom(Class.forName(type.getTypeName()));
//        } catch (ClassNotFoundException e) {
//            return false;
//        }
//    }

    private String targetPackage(Package sourcePackage, boolean gwt) {
        return sourcePackage.getName().replace(".tm.", ".map.") + (gwt ? ".gwt" : "");
    }

    private static Collection<FieldId> fieldsWithoutPath(List<VisitorPath> collected) throws IllegalArgumentException,
            SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Set<FieldId> fields = new HashSet<>();
        for (VisitorPath path : collected) {
            final FieldId[] values = (FieldId[]) path.getFieldId().getClass().getMethod("values").invoke(null);
            fields.addAll(Arrays.asList(values));
        }
        for (VisitorPath path : collected) {
            fields.remove(path.getFieldId());
        }
        return filterFieldsType(fields);
    }

    private static Set<FieldId> filterFieldsType(Set<FieldId> fields) {
        final Set<FieldId> filterFields = new HashSet<>();
        for (FieldId FieldId : fields) {
            // FIXME read-only fields?
//            if (FieldId.type().isReadOnly()) {
//                continue;
//            }
            filterFields.add(FieldId);
        }
        return filterFields;
    }
}
