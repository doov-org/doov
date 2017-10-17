package io.doov.core.dsl.impl;

import java.util.function.BiPredicate;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;

abstract class AbstractStepCondition implements StepCondition {

    private final Metadata metadata;
    private final BiPredicate<FieldModel, Context> predicate;

    AbstractStepCondition(Metadata metadata, BiPredicate<FieldModel, Context> predicate) {
        this.metadata = metadata;
        this.predicate = predicate;
    }

    @Override
    public BiPredicate<FieldModel, Context> predicate() {
        return (model, context) -> {
            boolean test = predicate.test(model, context);
            if (!test) {
                context.failed(metadata);
            }
            return test;
        };
    }

    @Override
    public String readable() {
        return metadata.readable();
    }

    public Metadata getMetadata() {
        return metadata;
    }

}
