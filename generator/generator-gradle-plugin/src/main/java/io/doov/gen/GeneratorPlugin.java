package io.doov.gen;

import org.gradle.api.*;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.compile.JavaCompile;

public class GeneratorPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        NamedDomainObjectContainer<ModelMapGenerator> container = target.container(ModelMapGenerator.class,
                name -> new ModelMapGenerator(name, target));
        target.getExtensions().add("doovCodeGen", container);

        JavaCompile compileJava = target.getTasks().withType(JavaCompile.class).getByName("compileJava");
        JavaPluginConvention javaPluginConvention = target.getConvention().findPlugin(JavaPluginConvention.class);
        SourceSet generated = javaPluginConvention.getSourceSets().maybeCreate("generated");

        container.all(modelMap -> {
            ModelMapGenTask task = target.getTasks().create(modelMap.getName(), ModelMapGenTask.class);
            generated.getJava().srcDir(modelMap.getOutputDirectory());
            compileJava.source(generated.getJava());

            target.afterEvaluate(project -> {
                task.setClasspath(project.getConfigurations().getByName("compile"));
                task.getBaseClassProperty().set(modelMap.getBaseClass());
                task.getOutputDirectory().set(modelMap.getOutputDirectory());
                task.getOutputResourceDirectory().set(modelMap.getOutputResourceDirectory());
                task.getSourceClassProperty().set(modelMap.getSourceClass());
                task.getFieldClassProperty().set(modelMap.getFieldClass());
                task.getPackageFilter().set(modelMap.getPackageFilter());
                task.getEnumFieldInfo().set(modelMap.getEnumFieldInfo());
                task.getFieldPathProviderProperty().set(modelMap.getFieldPathProvider());
                task.getTypeAdaptersProperty().set(modelMap.getTypeAdapters());
                task.getDslModelPackage().set(modelMap.getDslModelPackage());
                task.getWrapperPackage().set(modelMap.getWrapperPackage());
                task.getFieldInfoPackage().set(modelMap.getFieldInfoPackage());
                task.getDslEntrypointMethods().set(modelMap.getDslEntrypointMethods());

                task.doFirst(t -> {
                   target.delete(modelMap.getOutputDirectory());
                   target.delete(modelMap.getOutputResourceDirectory());
                });
                compileJava.dependsOn(task);
            });

        });

    }

}
