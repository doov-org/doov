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

import java.io.File;

import org.gradle.api.Project;

public class ModelMapGenerator {

    private final String name;
    private File outputDirectory ;
    private File outputResourceDirectory;
    private String sourceClass;
    private String fieldClass;
    private String packageFilter;
    private String fieldPathProvider;
    private String baseClass;
    private String typeAdapters;
    private String fieldInfoTypes;
    private Boolean enumFieldInfo;
    private String wrapperPackage;
    private String fieldInfoPackage;
    private String dslModelPackage;
    private Boolean dslEntrypointMethods;

    public ModelMapGenerator(String name, Project project) {
        this.name = name;
        this.outputDirectory = new File(project.getBuildDir(), "doov/" + name);
        this.outputResourceDirectory = new File(project.getBuildDir(), "doov/" + name);
        this.enumFieldInfo = true;
        this.dslEntrypointMethods = true;
    }

    public String getName() {
        return name;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public File getOutputResourceDirectory() {
        return outputResourceDirectory;
    }

    public void setOutputResourceDirectory(File outputResourceDirectory) {
        this.outputResourceDirectory = outputResourceDirectory;
    }

    public String getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(String sourceClass) {
        this.sourceClass = sourceClass;
    }

    public String getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(String fieldClass) {
        this.fieldClass = fieldClass;
    }

    public String getPackageFilter() {
        return packageFilter;
    }

    public void setPackageFilter(String packageFilter) {
        this.packageFilter = packageFilter;
    }

    public String getFieldPathProvider() {
        return fieldPathProvider;
    }

    public void setFieldPathProvider(String fieldPathProvider) {
        this.fieldPathProvider = fieldPathProvider;
    }

    public String getBaseClass() {
        return baseClass;
    }

    public void setBaseClass(String baseClass) {
        this.baseClass = baseClass;
    }

    public String getTypeAdapters() {
        return typeAdapters;
    }

    public void setTypeAdapters(String typeAdapters) {
        this.typeAdapters = typeAdapters;
    }

    public String getFieldInfoTypes() {
        return fieldInfoTypes;
    }

    public void setFieldInfoTypes(String fieldInfoTypes) {
        this.fieldInfoTypes = fieldInfoTypes;
    }

    public Boolean getEnumFieldInfo() {
        return enumFieldInfo;
    }

    public void setEnumFieldInfo(Boolean enumFieldInfo) {
        this.enumFieldInfo = enumFieldInfo;
    }

    public String getWrapperPackage() {
        return wrapperPackage;
    }

    public void setWrapperPackage(String wrapperPackage) {
        this.wrapperPackage = wrapperPackage;
    }

    public String getFieldInfoPackage() {
        return fieldInfoPackage;
    }

    public void setFieldInfoPackage(String fieldInfoPackage) {
        this.fieldInfoPackage = fieldInfoPackage;
    }

    public String getDslModelPackage() {
        return dslModelPackage;
    }

    public void setDslModelPackage(String dslModelPackage) {
        this.dslModelPackage = dslModelPackage;
    }

    public Boolean getDslEntrypointMethods() {
        return dslEntrypointMethods;
    }

    public void setDslEntrypointMethods(Boolean dslEntrypointMethods) {
        this.dslEntrypointMethods = dslEntrypointMethods;
    }

}
