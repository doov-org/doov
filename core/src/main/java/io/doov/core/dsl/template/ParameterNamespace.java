/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.*;
import java.util.function.Function;

import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.runtime.GenericModel;

public class ParameterNamespace {

    private GenericModel namespace;

    private Map<String,IntegerFieldInfo> integerIndex;

    public static Function<ParameterNamespace,IntegerFieldInfo> $Integer = $Integer("DEFAULT");

    public static Function<ParameterNamespace,IntegerFieldInfo> $Integer(String name) {
        return ns -> ns.ofInteger(name);
    }

    public ParameterNamespace() {
        this.namespace = new GenericModel();
        this.integerIndex = new HashMap<>();
    }

    public IntegerFieldInfo ofInteger(String name) {
        if (integerIndex.containsKey(name)) {
            return integerIndex.get(name);
        } else {
            IntegerFieldInfo field = namespace.intField(0, name);
            integerIndex.put(name,field);
            return field;
        }
    }

}
