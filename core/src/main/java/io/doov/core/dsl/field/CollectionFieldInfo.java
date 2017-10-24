/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import java.util.Collection;
import java.util.Optional;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.CollectionStepCondition;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;

public class CollectionFieldInfo<T, C extends Collection<T>> extends DefaultFieldInfo<C> {

    CollectionFieldInfo(FieldId fieldId, String readable, Class<?> type, Class<?>[] genericTypes, FieldId[] siblings) {
        super(fieldId, readable, type, genericTypes, siblings);
    }

    public StepCondition contains(T value) {
        return new CollectionStepCondition(FieldMetadata.containsMetadata(this, value),
                        (model, context) -> Optional.<C> ofNullable(model.get(id()))
                                        .map(collection -> collection.contains(value)).orElse(false));
    }

    public StepCondition isEmpty() {
        return new CollectionStepCondition(FieldMetadata.isEmptyMetadata(this),
                        (model, context) -> Optional.<C> ofNullable(model.get(id()))
                                        .map(collection -> collection.isEmpty()).orElse(false));
    }
    
    public StepCondition isNotEmpty() {
        return new CollectionStepCondition(FieldMetadata.isNotEmptyMetadata(this),
                        (model, context) -> Optional.<C> ofNullable(model.get(id()))
                                        .map(collection -> !collection.isEmpty()).orElse(false));
    }

    public StepCondition hasSize(int size) {
        return new CollectionStepCondition(FieldMetadata.hasSizeMetadata(this, size),
                        (model, context) -> Optional.<C> ofNullable(model.get(id()))
                                        .map(collection -> collection.size() == size).orElse(false));
    }
    
    public StepCondition hasNotSize(int size) {
        return new CollectionStepCondition(FieldMetadata.hasNotSizeMetadata(this, size),
                        (model, context) -> Optional.<C> ofNullable(model.get(id()))
                                        .map(collection -> collection.size() != size).orElse(false));
    }
}
