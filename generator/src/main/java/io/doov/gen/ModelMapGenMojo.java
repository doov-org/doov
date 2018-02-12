/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.gen;

import static io.doov.gen.FieldInfoGen.constants;
import static io.doov.gen.FieldInfoGen.createFieldInfos;
import static io.doov.gen.FieldInfoGen.imports;
import static io.doov.gen.FieldInfoGen.methods;
import static io.doov.gen.ModelWrapperGen.*;
import static io.doov.gen.utils.ClassUtils.transformPathToMethod;
import static java.nio.file.Files.createDirectories;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;
import static java.time.format.FormatStyle.SHORT;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.*;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import io.doov.core.*;
import io.doov.core.dsl.field.FieldTypeProvider;
import io.doov.core.dsl.field.FieldTypes;
import io.doov.core.dsl.path.FieldPath;
import io.doov.core.dsl.path.FieldPathProvider;
import io.doov.core.serial.TypeAdapterRegistry;
import io.doov.core.serial.TypeAdapters;
import io.doov.gen.processor.MacroProcessor;
import io.doov.gen.utils.ClassLoaderUtils;
import io.doov.gen.utils.ClassUtils;

@Mojo(name = "generate", defaultPhase = GENERATE_SOURCES, threadSafe = true)
public final class ModelMapGenMojo extends AbstractMojo {

    @Parameter(required = true, defaultValue = "${basedir}/src/generated/java")
    private File outputDirectory;

    @Parameter(required = true, property = "project.build.outputDirectory")
    private File buildDirectory;

    @Parameter(required = true, defaultValue = "${basedir}/target")
    private File outputResourceDirectory;

    @Parameter(required = true)
    private String sourceClass;

    @Parameter(required = true)
    private String fieldClass;

    @Parameter(required = true, readonly = true, property = "project")
    private MavenProject project;

    @Parameter(required = true)
    private String packageFilter;

    @Parameter
    private String fieldPathProvider;

    @Parameter
    private String baseClass;

    @Parameter
    private String typeAdapters;

    @Parameter(defaultValue = "true")
    private boolean enumFieldInfo;

    @Parameter
    private String fieldInfoTypes;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (sourceClass == null) {
            getLog().warn("no project classes");
        }
        if (sourceClass.isEmpty()) {
            getLog().warn("project is empty");
        }
        if (fieldClass == null) {
            getLog().warn("no tunnel classes");
        }
        if (fieldClass.isEmpty()) {
            getLog().warn("tunnel is empty");
        }
        if (outputDirectory.exists() && !outputDirectory.isDirectory()) {
            throw new MojoFailureException(outputDirectory + " is not directory");
        }

        // add source directory to current project
        try {
            createDirectories(outputDirectory.toPath());
            project.addCompileSourceRoot(outputDirectory.getPath());
        } catch (IOException e) {
            throw new MojoExecutionException("unable to create source folder", e);
        }

        final URLClassLoader classLoader = ClassLoaderUtils.getUrlClassLoader(project);
        try {
            List<FieldPath> fieldPaths = fieldPathProvider != null ?
                    loadClassWithType(this.fieldPathProvider, FieldPathProvider.class, null, classLoader)
                            .newInstance().values()
                    : Collections.emptyList();
            Class<?> modelClazz = Class.forName(sourceClass, true, classLoader);
            Class<? extends FieldId> fieldClazz = loadClassWithType(fieldClass, FieldId.class, null, classLoader);
            Class<? extends FieldModel> baseClazz = loadClassWithType(baseClass,
                    AbstractWrapper.class, AbstractWrapper.class, classLoader);
            Class<? extends TypeAdapterRegistry> typeAdapterClazz = loadClassWithType(typeAdapters,
                    TypeAdapterRegistry.class, TypeAdapters.class, classLoader);
            FieldTypeProvider typeProvider = loadClassWithType(fieldInfoTypes,
                    FieldTypeProvider.class, FieldTypes.class, classLoader).newInstance();
            generateModels(fieldClazz, modelClazz, baseClazz, typeAdapterClazz, typeProvider, fieldPaths);
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

    }

    private <T> Class<? extends T> loadClassWithType(String className,
                                                     Class<T> type,
                                                     Class<? extends T> defaultClass,
                                                     URLClassLoader classLoader)
            throws MojoExecutionException, ClassNotFoundException {
        Class<? extends T> classToLoad = defaultClass;
        if (className != null) {
            Class<?> loadedClass = Class.forName(className, true, classLoader);
            if (!type.isAssignableFrom(type)) {
                throw new MojoExecutionException("Class " + loadedClass + " does not implement " + type.getName());
            }
            classToLoad = (Class<? extends T>) loadedClass;
        }
        return classToLoad;
    }

    private void generateModels(Class<? extends FieldId> fieldClazz,
                    Class<?> modelClazz,
                    Class<? extends FieldModel> baseClazz,
                    Class<? extends TypeAdapterRegistry> typeAdapterClazz,
                    FieldTypeProvider typeProvider,
                    List<FieldPath> fieldPaths) {
        try {
            final List<VisitorPath> collected;
            if (fieldPaths.isEmpty()) {
                collected = process(modelClazz, packageFilter, fieldClazz);
            } else {
                collected = fieldPaths.stream().map(this::createVisitorPath).collect(toList());
            }
            final Map<FieldId, VisitorPath> fieldPathMap = validatePath(collected, getLog());
            final Map<FieldId, GeneratorFieldInfo> fieldInfoMap = createFieldInfos(fieldPathMap);
            Runnable generateCsv = () -> generateCsv(fieldPathMap, modelClazz);
            Runnable generateWrapper = () -> generateWrapper(fieldPathMap, modelClazz, fieldClazz, baseClazz, typeAdapterClazz);
            Runnable generateFieldInfo = () -> generateFieldInfo(fieldInfoMap, fieldClazz);
            Runnable generateDslFields = () -> generateDslFields(fieldInfoMap, modelClazz, fieldClazz, typeProvider);
            asList(generateWrapper, generateCsv, generateFieldInfo, generateDslFields).parallelStream()
                    .forEach(Runnable::run);
        } catch (Exception e) {
            throw new RuntimeException("generation failed for class " + modelClazz, e);
        }
    }

    private VisitorPath createVisitorPath(FieldPath p) {
        getLog().debug("Processing field path " + p);
        LinkedHashMap<Class, Method> path = transformPathToMethod(p.getBaseClass(), p.getPath());
        List<Class> classes = new ArrayList<>(path.keySet());
        List<Method> methods = new ArrayList<>(path.values());
        // Last class of the path is the container of the field
        Class<?> containerClass = classes.get(classes.size() - 1);
        Method readMethod = ClassUtils.getReferencedMethod(containerClass, p.getReadMethod());
        Method writeMethod = ClassUtils.getReferencedMethod(containerClass, p.getWriteMethod());
        Map<String, String> cannonicalReplacement = new HashMap<>();
        PathConstraint constraint = p.getConstraint();
        if (constraint != null && constraint.canonicalPathReplacements() != null) {
            cannonicalReplacement.putAll(constraint.canonicalPathReplacements());
        }
        return new VisitorPath(p.getBaseClass(), methods, p.getFieldId(), p.getReadable(),
                        readMethod, writeMethod, p.isTransient(), cannonicalReplacement);
    }

    private List<VisitorPath> process(Class<?> projetClass, String filter, Class<? extends FieldId> fieldClass)
                    throws Exception {
        final List<VisitorPath> collected = new ArrayList<>();
        new ModelVisitor(getLog()).visitModel(projetClass, fieldClass, new Visitor(projetClass, collected), filter);
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

    private void generateFieldInfo(Map<FieldId, GeneratorFieldInfo> fieldInfoMap, Class<?> fieldClass) {
        try {
            final String targetClassName = fieldInfoClassName(fieldClass);
            final String targetPackage = fieldClass.getPackage().getName();
            final File targetFile = new File(outputDirectory + "/" + targetPackage.replace('.', '/'),
                    targetClassName + ".java");
            final String classTemplate = enumFieldInfo ? template("FieldInfoEnum.template") :
                    template("FieldInfoClass.template");
            createDirectories(targetFile.getParentFile().toPath());
            final Map<String, String> conf = new HashMap<>();
            conf.put("package.name", targetPackage);
            conf.put("process.class", fieldClass.getName());
            conf.put("process.date", ofLocalizedDateTime(SHORT).format(now()));
            conf.put("target.class.name", targetClassName);
            conf.put("imports", imports(fieldInfoMap, null));
            conf.put("constants", constants(fieldInfoMap, enumFieldInfo));
            conf.put("source.generator.name", getClass().getName());
            final String content = MacroProcessor.replaceProperties(classTemplate, conf);
            Files.write(content, targetFile, Charset.forName("UTF8"));
            getLog().info("written : " + targetFile);
        } catch (IOException e) {
            throw new RuntimeException("error when generating wrapper", e);
        }
    }

    private static String fieldInfoClassName(Class<?> clazz) {
        return clazz.getSimpleName().startsWith("E") ? clazz.getSimpleName().substring(1)
                : clazz.getSimpleName() + "Info";
    }

    private void generateDslFields(Map<FieldId, GeneratorFieldInfo> fieldInfoMap,
                                   Class<?> modelClazz,
                                   Class<?> fieldClass,
                                   FieldTypeProvider typeProvider) {
        try {
            final String targetClassName = dslFieldsClassName(modelClazz);
            final String fieldInfoClassName = fieldInfoClassName(fieldClass);
            final String targetPackage = fieldClass.getPackage().getName() + ".dsl";
            final File targetFile = new File(outputDirectory + "/" + targetPackage.replace('.', '/'),
                    targetClassName + ".java");
            final String classTemplate = template("DslFieldModel.template");
            createDirectories(targetFile.getParentFile().toPath());
            final Map<String, String> conf = new HashMap<>();
            conf.put("package.name", targetPackage);
            conf.put("process.class", fieldClass.getName());
            conf.put("process.date", ofLocalizedDateTime(SHORT).format(now()));
            conf.put("target.class.name", targetClassName);
            conf.put("process.field.info.class", fieldClass.getPackage().getName() + "." + fieldInfoClassName);
            conf.put("imports", imports(fieldInfoMap, typeProvider));
            conf.put("methods", methods(fieldInfoMap, typeProvider, enumFieldInfo));
            conf.put("source.generator.name", getClass().getName());
            final String content = MacroProcessor.replaceProperties(classTemplate, conf);
            Files.write(content, targetFile, Charset.forName("UTF8"));
            getLog().info("written : " + targetFile);
        } catch (IOException e) {
            throw new RuntimeException("error when generating wrapper", e);
        }
    }


    private static String dslFieldsClassName(Class<?> clazz) {
        return "DSL" +
                (clazz.getSimpleName().startsWith("E") ? clazz.getSimpleName().substring(1) : clazz.getSimpleName());
    }

    private void generateWrapper(Map<FieldId, VisitorPath> fieldPaths,
                                 Class<?> modelClass,
                                 Class<?> fieldClass,
                                 Class<? extends FieldModel> baseClazz,
                                 Class<? extends TypeAdapterRegistry> typeAdapterClazz) throws RuntimeException {
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
            conf.put("process.base.class.package", baseClazz.getCanonicalName());
            conf.put("process.base.class.name", baseClassName(baseClazz, modelClass));
            conf.put("process.date", ofLocalizedDateTime(SHORT).format(now()));
            conf.put("type.adapter.class.package", typeAdapterClazz.getCanonicalName());
            conf.put("type.adapter.class.name", typeAdapterClazz.getSimpleName());
            conf.put("constructors", mapConstructors(targetClassName, baseClazz, modelClass));
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
            Files.write(content, targetFile, Charset.forName("UTF8"));
            getLog().info("written : " + targetFile);
        } catch (IOException e) {
            throw new RuntimeException("error when generating wrapper", e);
        }
    }

    private String baseClassName(Class<? extends FieldModel> baseClazz, Class<?> modelClass) {
        if (AbstractWrapper.class.equals(baseClazz)) {
            return AbstractWrapper.class.getSimpleName() + "<" + modelClass.getSimpleName() + ">";
        } else {
            return baseClazz.getSimpleName();
        }
    }

}
