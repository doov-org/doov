/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.gen.processor;

import java.io.IOException;
import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import io.doov.gen.ModelMapGenMojo;

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

    private static String template(String template) {
        try {
            URL resource = Resources.getResource(ModelMapGenMojo.class, template);
            return Resources.toString(resource, Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("failed to load template " + template, e);
        }
    }
}
