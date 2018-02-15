/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import io.doov.core.FieldInfo;

public interface FieldTypeProvider {

    default Class<? extends FieldInfo> fielInfoType(FieldInfo fieldInfo) {
        Optional<? extends Class<? extends FieldInfo>> first = getTypes().entrySet().stream()
                        .filter(e -> e.getKey().test(fieldInfo))
                        .map(Map.Entry::getValue)
                        .findFirst();
        return first.isPresent() ? first.get() : getDefaultFieldInfoClass();
    }

    default Class<? extends FieldInfo> getDefaultFieldInfoClass() {
        return DefaultFieldInfo.class;
    }

    Map<Predicate<FieldInfo>, Class<? extends FieldInfo>> getTypes();

}
