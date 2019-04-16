/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;

public class Exemple {

    public static void main(String... args) {

        GenericModel model = new GenericModel();

        IntegerFieldInfo nombreEnfants = model.intField(2, "nombreEnfants");
        IntegerFieldInfo montantPret = model.intField(500000, "montant");

        ValidationRule rule = DOOV.when(nombreEnfants.between(1,3).and(montantPret.lesserThan(300000))).validate();

        System.out.println(rule.ast().toString());
        System.out.println(rule.ast().json().toString());


    }
}
