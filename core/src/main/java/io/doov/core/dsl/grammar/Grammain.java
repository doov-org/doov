/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import io.doov.core.dsl.field.types.BooleanFieldInfo;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.runtime.GenericModel;

public class Grammain {

    public static void main(String[] args) {
        GenericModel model = new GenericModel();

        BooleanFieldInfo a = model.booleanField(true,"a");
        BooleanFieldInfo b = model.booleanField(true,"b");
        BooleanFieldInfo c = model.booleanField(true,"c");
        StringFieldInfo str = model.stringField("toto","c");

        StepCondition rule = str.contains("yop").and(str.startsWith("yoo"));

        System.out.println(rule.ast().toString());

    }

}
