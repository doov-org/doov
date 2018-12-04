package io.doov.gen;

import static io.doov.gen.DslMethodsGen.fields;
import static io.doov.gen.DslMethodsGen.iterableMethods;
import static io.doov.gen.FieldInfoGen.constants;
import static io.doov.gen.FieldInfoGen.createFieldInfos;
import static io.doov.gen.ModelWrapperGen.*;
import static io.doov.gen.utils.ClassUtils.transformPathToMethod;
import static java.nio.file.Files.createDirectories;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;
import static java.time.format.FormatStyle.SHORT;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.*;
import org.gradle.api.tasks.Optional;

import com.google.common.io.Files;

import io.doov.core.*;
import io.doov.core.dsl.field.FieldTypeProvider;
import io.doov.core.dsl.field.FieldTypes;
import io.doov.core.dsl.impl.DefaultStepWhen;
import io.doov.core.dsl.impl.DefaultValidationRule;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.path.FieldPath;
import io.doov.core.dsl.path.FieldPathProvider;
import io.doov.core.serial.TypeAdapterRegistry;
import io.doov.core.serial.TypeAdapters;
import io.doov.gen.processor.MacroProcessor;
import io.doov.gen.processor.Templates;
import io.doov.gen.utils.ClassUtils;

public class ModelMapGenTask extends DefaultTask {

    private FileCollection classpath;
    private final Property<File> outputDirectory;
    private final Property<File> outputResourceDirectory;
    private final Property<String> sourceClassProperty;
    private final Property<String> fieldClassProperty;
    private final Property<String> packageFilter;
    private final Property<String> fieldPathProviderProperty;
    private final Property<String> baseClassProperty;
    private final Property<String> typeAdaptersProperty;
    private final Property<String> fieldInfoTypesProperty;
    private final Property<Boolean> enumFieldInfo;
    private final Property<String> wrapperPackage;
    private final Property<String> fieldInfoPackage;
    private final Property<String> dslModelPackage;
    private final Property<Boolean> dslEntrypointMethods;

    public ModelMapGenTask() {
        this.outputDirectory = getProject().getObjects().property(File.class);
        this.outputResourceDirectory = getProject().getObjects().property(File.class);
        this.sourceClassProperty = getProject().getObjects().property(String.class);
        this.fieldClassProperty = getProject().getObjects().property(String.class);
        this.packageFilter = getProject().getObjects().property(String.class);
        this.fieldPathProviderProperty = getProject().getObjects().property(String.class);
        this.baseClassProperty = getProject().getObjects().property(String.class);
        this.typeAdaptersProperty = getProject().getObjects().property(String.class);
        this.enumFieldInfo = getProject().getObjects().property(Boolean.class);
        this.fieldInfoTypesProperty = getProject().getObjects().property(String.class);
        this.wrapperPackage = getProject().getObjects().property(String.class);
        this.fieldInfoPackage = getProject().getObjects().property(String.class);
        this.dslModelPackage = getProject().getObjects().property(String.class);
        this.dslEntrypointMethods = getProject().getObjects().property(Boolean.class);
    }

    @Classpath
    public FileCollection getClasspath() {
        return classpath;
    }

    public void setClasspath(FileCollection classpath) {
        this.classpath = classpath;
    }

    @OutputDirectory
    public Property<File> getOutputDirectory() {
        return outputDirectory;
    }

    @OutputDirectory
    public Property<File> getOutputResourceDirectory() {
        return outputResourceDirectory;
    }

    @Input
    public Property<String> getSourceClassProperty() {
        return sourceClassProperty;
    }

    @Input
    public Property<String> getFieldClassProperty() {
        return fieldClassProperty;
    }

    @Input
    public Property<String> getPackageFilter() {
        return packageFilter;
    }

    @Input
    @Optional
    public Property<String> getFieldPathProviderProperty() {
        return fieldPathProviderProperty;
    }

    @Input
    @Optional
    public Property<String> getBaseClassProperty() {
        return baseClassProperty;
    }

    @Input
    @Optional
    public Property<String> getTypeAdaptersProperty() {
        return typeAdaptersProperty;
    }

    @Input
    @Optional
    public Property<String> getFieldInfoTypesProperty() {
        return fieldInfoTypesProperty;
    }

    @Input
    @Optional
    public Property<Boolean> getEnumFieldInfo() {
        return enumFieldInfo;
    }

    @Input
    @Optional
    public Property<String> getWrapperPackage() {
        return wrapperPackage;
    }

    @Input
    @Optional
    public Property<String> getFieldInfoPackage() {
        return fieldInfoPackage;
    }

    @Input
    @Optional
    public Property<String> getDslModelPackage() {
        return dslModelPackage;
    }

    @Input
    @Optional
    public Property<Boolean> getDslEntrypointMethods() {
        return dslEntrypointMethods;
    }

    @TaskAction
    public void action() {
        final URLClassLoader classLoader;
        try {
            URL[] urls = classpath.getFiles().stream()
                    .map(f -> {
                        try {
                            return f.toURI().toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(URL[]::new);
            classLoader = new URLClassLoader(urls, getClass().getClassLoader());
        } catch (Exception e) {
            throw new GradleException("Unable to load", e);
        }
        try {
            List<FieldPath> fieldPaths = fieldPathProviderProperty.isPresent() ?
                    loadClassWithType(this.fieldPathProviderProperty, FieldPathProvider.class, null, classLoader)
                            .newInstance().values()
                    : Collections.emptyList();
            Class<?> modelClazz = Class.forName(sourceClassProperty.get(), true, classLoader);
            Class<? extends FieldId> fieldClazz = loadClassWithType(fieldClassProperty, FieldId.class, null, classLoader);
            Class<? extends FieldModel> baseClazz = loadClassWithType(baseClassProperty,
                    AbstractWrapper.class, AbstractWrapper.class, classLoader);
            Class<? extends TypeAdapterRegistry> typeAdapterClazz = loadClassWithType(typeAdaptersProperty,
                    TypeAdapterRegistry.class, TypeAdapters.class, classLoader);
            FieldTypeProvider typeProvider = loadClassWithType(fieldInfoTypesProperty,
                    FieldTypeProvider.class, FieldTypes.class, classLoader).newInstance();

            cleanOutputDirectories();
            generateModels(fieldClazz, modelClazz, baseClazz, typeAdapterClazz, typeProvider, fieldPaths);
        } catch (Exception e) {
            throw new GradleException(e.getMessage(), e);
        }
    }

    private void cleanOutputDirectories() {
        outputResourceDirectory.map(File::delete).getOrNull();
        outputDirectory.map(File::delete).getOrNull();
    }

    private <T> Class<? extends T> loadClassWithType(Property<String> className,
            Class<T> type,
            Class<? extends T> defaultClass,
            URLClassLoader classLoader) throws ClassNotFoundException {
        Class<? extends T> classToLoad = defaultClass;
        if (className.isPresent()) {
            Class<?> loadedClass = Class.forName(className.get(), true, classLoader);
            if (!type.isAssignableFrom(loadedClass)) {
                throw new GradleException("Class " + loadedClass + " does not implement " + type.getName());
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
                collected = process(modelClazz, packageFilter.get(), fieldClazz);
            } else {
                collected = fieldPaths.stream().map(this::createVisitorPath).collect(toList());
            }
            final Map<FieldId, VisitorPath> fieldPathMap = validatePath(collected, getLogger());
            final Map<FieldId, GeneratorFieldInfo> fieldInfoMap = createFieldInfos(fieldPathMap);
            Runnable generateCsv = () -> generateCsv(fieldPathMap, modelClazz);
            Runnable generateWrapper = () -> generateWrapper(fieldPathMap, modelClazz, fieldClazz, baseClazz, typeAdapterClazz);
            Runnable generateFieldInfo = () -> generateFieldInfo(fieldInfoMap, fieldClazz);
            Runnable generateDslFields = () -> generateDslFields(fieldInfoMap, modelClazz, fieldClazz, baseClazz, typeProvider);
            asList(generateWrapper, generateCsv, generateFieldInfo, generateDslFields).parallelStream()
                    .forEach(Runnable::run);
        } catch (Exception e) {
            throw new GradleException("generation failed for class " + modelClazz, e);
        }
    }

    private VisitorPath createVisitorPath(FieldPath p) {
        getLogger().debug("Processing field path " + p);
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
        new ModelVisitor(getLogger()).visitModel(projetClass, fieldClass, new Visitor(projetClass, collected), filter);
        return collected;
    }

    private void generateCsv(Map<FieldId, VisitorPath> fieldPaths, Class<?> clazz) {
        final File targetFile = new File(outputResourceDirectory.get(), clazz.getSimpleName() + ".csv");
        targetFile.getParentFile().mkdirs();
        FieldCsvGen.write(targetFile, fieldPaths);
        getLogger().info("written : " + targetFile);
    }

    private void generateFieldInfo(Map<FieldId, GeneratorFieldInfo> fieldInfoMap, Class<?> fieldClass) {
        try {
            final String targetClassName = fieldInfoClassName(fieldClass);
            final String targetPackage = getFieldInfoPackage(fieldClass);
            final File targetFile = new File(outputDirectory.get(), targetPackage.replace('.', '/')
                    + "/" + targetClassName + ".java");
            final String classTemplate = enumFieldInfo.get() ? Templates.fieldInfoEnum : Templates.fieldInfoClass;
            createDirectories(targetFile.getParentFile().toPath());
            final Map<String, String> conf = new HashMap<>();
            conf.put("package.name", targetPackage);
            conf.put("process.class", fieldClass.getName());
            conf.put("process.date", ofLocalizedDateTime(SHORT).format(now()));
            conf.put("target.class.name", targetClassName);
            conf.put("imports", FieldInfoGen.imports(fieldInfoMap));
            conf.put("constants", constants(fieldInfoMap, enumFieldInfo.get()));
            conf.put("source.generator.name", getClass().getName());
            final String content = MacroProcessor.replaceProperties(classTemplate, conf);
            Files.write(content, targetFile, Charset.forName("UTF8"));
            getLogger().info("written : " + targetFile);
        } catch (IOException e) {
            throw new GradleException("error when generating wrapper", e);
        }
    }

    private static String fieldInfoClassName(Class<?> clazz) {
        return clazz.getSimpleName().startsWith("E") ? clazz.getSimpleName().substring(1)
                : clazz.getSimpleName() + "Info";
    }

    private void generateDslFields(Map<FieldId, GeneratorFieldInfo> fieldInfoMap,
            Class<?> modelClazz,
            Class<?> fieldClass,
            Class<? extends FieldModel> baseClazz,
            FieldTypeProvider typeProvider) {
        try {
            final String targetClassName = dslFieldsClassName(modelClazz);
            final String fieldInfoClassName = fieldInfoClassName(fieldClass);
            final String targetFieldInfoPackage = getFieldInfoPackage(fieldClass);
            final String targetPackage = getDslModelPackage(fieldClass);
            final String wrapperFqcn = getWrapperPackage(modelClazz) + "." + modelClazz.getSimpleName() + "Wrapper";
            final File targetFile = new File(outputDirectory.get(), targetPackage.replace('.', '/')
                    + "/" + targetClassName + ".java");
            createDirectories(targetFile.getParentFile().toPath());
            final Map<String, String> conf = new HashMap<>();
            conf.put("package.name", targetPackage);
            conf.put("process.class", fieldClass.getName());
            conf.put("process.date", ofLocalizedDateTime(SHORT).format(now()));
            conf.put("process.base.class.package", baseClazz.getPackage().getName());
            conf.put("target.class.name", targetClassName);
            conf.put("model.class.name", modelClazz.getSimpleName());
            conf.put("process.field.info.class", targetFieldInfoPackage + "." + fieldInfoClassName);
            conf.put("imports", DslMethodsGen.imports(fieldInfoMap, typeProvider, dslEntrypointMethods.get() ?
                    Arrays.asList(wrapperFqcn,
                            modelClazz.getName(),
                            DefaultStepWhen.class.getName(),
                            DefaultValidationRule.class.getName(),
                            Result.class.getName(),
                            StepCondition.class.getName(),
                            StepWhen.class.getName(),
                            RuleRegistry.class.getName()) : Collections.emptyList()));
            conf.put("fields", fields(fieldInfoMap, typeProvider, enumFieldInfo.get()));
            conf.put("methods", iterableMethods(fieldInfoMap, typeProvider));
            String entryPointMethods = dslEntrypointMethods.get() ? MacroProcessor.replaceProperties(Templates.dslEntrypointMethod, conf) : "";
            conf.put("entrypoint", entryPointMethods);
            conf.put("source.generator.name", getClass().getName());
            final String content = MacroProcessor.replaceProperties(Templates.dslFieldModel, conf);
            Files.write(content, targetFile, Charset.forName("UTF8"));
            getLogger().info("written : " + targetFile);
        } catch (IOException e) {
            throw new GradleException("error when generating wrapper", e);
        }
    }

    private static String dslFieldsClassName(Class<?> clazz) {
        return "Dsl" + (clazz.getSimpleName().startsWith("E") ? clazz.getSimpleName().substring(1) : clazz.getSimpleName());
    }

    private void generateWrapper(Map<FieldId, VisitorPath> fieldPaths,
            Class<?> modelClass,
            Class<?> fieldClass,
            Class<? extends FieldModel> baseClazz,
            Class<? extends TypeAdapterRegistry> typeAdapterClazz) throws RuntimeException {
        try {
            final String targetClassName = modelClass.getSimpleName() + "Wrapper";
            final String targetFieldInfoPackage = getFieldInfoPackage(fieldClass);
            final String targetPackage = getWrapperPackage(modelClass);
            final File targetFile = new File(outputDirectory.get(), targetPackage.replace('.', '/')
                    + "/" + targetClassName + ".java");
            createDirectories(targetFile.getParentFile().toPath());
            Map<String, String> conf = new HashMap<>();
            conf.put("package.name", targetPackage);
            conf.put("process.class", modelClass.getCanonicalName());
            conf.put("process.base.class.package", baseClazz.getCanonicalName());
            conf.put("process.base.class.name", baseClassName(baseClazz, modelClass));
            conf.put("process.date", ofLocalizedDateTime(SHORT).format(now()));
            conf.put("type.adapter.class.package", typeAdapterClazz.getCanonicalName());
            conf.put("type.adapter.class.name", typeAdapterClazz.getSimpleName());
            conf.put("constructors", mapConstructors(targetClassName, baseClazz, modelClass));
            conf.put("target.model.class.name", modelClass.getSimpleName());
            conf.put("target.model.class.full.name", modelClass.getName());
            conf.put("target.field.info.package.name", targetFieldInfoPackage);
            conf.put("target.field.info.class.name", fieldInfoClassName(fieldClass));
            conf.put("target.class.name", targetClassName);
            conf.put("map.getter", mapGetter(fieldPaths));
            conf.put("map.getter.if", mapFieldTypeIfStatement(Templates.mapGetIf, fieldPaths));
            conf.put("map.setter", mapSetter(fieldPaths));
            conf.put("map.setter.if", mapFieldTypeIfStatement(Templates.mapSetIf, fieldPaths));
            conf.put("map.properties", mapFieldProperties(fieldPaths, modelClass));
            conf.put("source.generator.name", getClass().getName());

            String content = MacroProcessor.replaceProperties(Templates.wrapperClass, conf);
            Files.write(content, targetFile, Charset.forName("UTF8"));
            getLogger().info("written : " + targetFile);
        } catch (IOException e) {
            throw new GradleException("error when generating wrapper", e);
        }
    }

    private String baseClassName(Class<? extends FieldModel> baseClazz, Class<?> modelClass) {
        if (AbstractWrapper.class.equals(baseClazz)) {
            return AbstractWrapper.class.getSimpleName() + "<" + modelClass.getSimpleName() + ">";
        } else {
            return baseClazz.getSimpleName();
        }
    }

    public String getFieldInfoPackage(Class<?> fieldClass) {
        return fieldInfoPackage.getOrElse(fieldClass.getPackage().getName());
    }

    private String getWrapperPackage(Class<?> modelClass) {
        return wrapperPackage.getOrElse(modelClass.getPackage().getName());
    }

    private String getDslModelPackage(Class<?> fieldClass) {
        return dslModelPackage.getOrElse(fieldClass.getPackage().getName() + ".dsl");
    }

}
