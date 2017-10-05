package org.modelmap.core.dsl.impl;

import java.util.function.Predicate;

import org.modelmap.core.FieldInfo;
import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.lang.StepCondition;
import org.modelmap.core.dsl.meta.FieldMetadata;

abstract class AbstractStepCondition<F extends FieldInfo, V> implements StepCondition {

    final FieldMetadata<F, V> metadata;
    final Predicate<FieldModel> predicate;

    AbstractStepCondition(FieldMetadata<F, V> metadata, Predicate<FieldModel> predicate) {
        this.metadata = metadata;
        this.predicate = predicate;
    }

}
