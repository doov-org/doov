/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import io.doov.core.FieldInfo;

/**
 * Provider interface for FieldInfo class types.
 */
public interface FieldTypeProvider {

    /**
     * @param fieldInfo untyped field info
     * @return class type for typed field info implementation class
     */
    default Class<? extends FieldInfo> fielInfoType(FieldInfo fieldInfo) {
        Optional<? extends Class<? extends FieldInfo>> first = getTypes().entrySet().stream()
                        .filter(e -> e.getKey().test(fieldInfo))
                        .map(Map.Entry::getValue)
                        .findFirst();
        return first.isPresent() ? first.get() : getDefaultFieldInfoClass();
    }

    /**
     * @return class type for default field info implementation
     */
    default Class<? extends FieldInfo> getDefaultFieldInfoClass() {
        return DefaultFieldInfo.class;
    }

    /**
     * To implement by the implementors of this interface
     *
     * @return immutable map of predicate to field info class type
     */
    Map<Predicate<FieldInfo>, Class<? extends FieldInfo>> getTypes();

}
