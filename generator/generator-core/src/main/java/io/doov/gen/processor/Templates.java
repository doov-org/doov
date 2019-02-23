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
package io.doov.gen.processor;

import java.io.IOException;
import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class Templates {

    // Wrapper Templates
    public static final String wrapperClass = template("WrapperClass.template");
    public static final String wrapperConstructor = template("WrapperConstructor.template");
    public static final String mapSetIf = template("MapSetIfStatement.template");
    public static final String mapGetIf = template("MapGetIfStatement.template");

    public static final String getSwitchBlock = template("GetSwitchBlock.template");
    public static final String setSwitchBlock = template("SetSwitchBlock.template");
    public static final String lazyInitListBlockNull = template("LazyInitListBlockNull.template");
    public static final String lazyInitListBlock = template("LazyInitListBlock.template");
    public static final String lazyInitBlock = template("LazyInitBlock.template");
    public static final String sizeCheckBlock = template("SizeCheckBlock.template");
    public static final String nullCheckBlock = template("NullCheckBlock.template");
    public static final String mapSetMethod = template("MapSetMethod.template");
    public static final String mapGetMethod = template("MapGetMethod.template");
    public static final String propertyLiteralConsumer = template("PropertyLiteralConsumer.template");
    public static final String propertyLiteralSupplier = template("PropertyLiteralSupplier.template");
    public static final String propertyIdEnum = template("PropertyIdEnum.template");

    // FieldInfo Templates
    public static final String fieldInfoClass = template("FieldInfoClass.template");
    public static final String fieldInfoEnum = template("FieldInfoEnum.template");

    // DslFieldModel Templates
    public static final String dslFieldModel = template("DslFieldModel.template");
    public final static String dslFieldMethod = template("DslFieldMethod.template");
    public final static String dslFieldIterableMethod = template("DslFieldIterableMethod.template");
    public final static String dslEntrypointMethod = template("DslFieldEntrypointMethod.template");


    private static String template(String template) {
        try {
            URL resource = Resources.getResource(Templates.class, template);
            return Resources.toString(resource, Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("failed to load template " + template, e);
        }
    }
}
