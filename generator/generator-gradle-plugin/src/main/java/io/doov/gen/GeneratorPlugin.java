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
        SourceSet main = javaPluginConvention.getSourceSets().maybeCreate("main");

        container.all(modelMap -> {
            ModelMapGenTask task = target.getTasks().create(modelMap.getName(), ModelMapGenTask.class);
            main.getJava().srcDir(modelMap.getOutputDirectory());

            target.afterEvaluate(project -> {
                task.setClasspath(main.getCompileClasspath());
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

                compileJava.dependsOn(task);
            });

        });

    }

}
