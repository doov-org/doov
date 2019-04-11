/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import static io.doov.core.dsl.DOOV.*;

import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.runtime.GenericModel;

public class Grammain {

    public static void main(String[] args) {
        GenericModel model = new GenericModel();

        StringFieldInfo str1 = model.stringField("toto","c");
        StringFieldInfo str2 = model.stringField("toto","c");

        MappingRule rule  = when(str1.length().lesserThan(3))
                .then(mappings(
                        map("totoro").to(str1),
                        map("tatara").to(str2)
                        ))
                .otherwise(mappings(
                                map("totoro").to(str1),
                                map("tatara").to(str2)
                ));

        System.out.println(rule.ast().toString());

    }

}
