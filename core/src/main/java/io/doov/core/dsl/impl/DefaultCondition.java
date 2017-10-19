package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.emptyMetadata;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.DefaultFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;
import io.doov.core.dsl.meta.Metadata;

class DefaultCondition<F extends DefaultFieldInfo<N>, N> {

    final F field;
    final FieldMetadata metadata;
    final Function<FieldModel, Optional<N>> value;

    DefaultCondition(F field) {
        this.field = field;
        this.metadata = emptyMetadata();
        this.value = fieldModel -> Optional.ofNullable(fieldModel.<N> get(field.id()));
    }

    DefaultCondition(FieldMetadata metadata, Function<FieldModel, Optional<N>> value) {
        this.field = null;
        this.metadata = metadata;
        this.value = value;
    }

    Optional<N> value(FieldModel model, F field) {
        return Optional.ofNullable(model.<N> get(field.id()));
    }

    StepCondition step(FieldMetadata metadata,
                    Function<N, Boolean> predicate) {
        return new DefaultStepCondition<>(this.metadata.merge(metadata), this.value, predicate);
    }

    StepCondition step(FieldMetadata metadata,
                    Function<FieldModel, Optional<N>> value,
                    BiFunction<N, N, Boolean> predicate) {
        return new DefaultStepCondition<>(this.metadata.merge(metadata), this.value, value, predicate);
    }

    static class DefaultStepCondition<N> extends AbstractStepCondition {

        DefaultStepCondition(Metadata metadata,
                        Function<FieldModel, Optional<N>> value,
                        Function<N, Boolean> predicate) {
            super(metadata, (model, context) -> value.apply(model).map(predicate).orElse(false));
        }

        DefaultStepCondition(Metadata metadata,
                        Function<FieldModel, Optional<N>> left,
                        Function<FieldModel, Optional<N>> right,
                        BiFunction<N, N, Boolean> predicate) {
            super(metadata, (model, context) -> left.apply(model)
                            .flatMap(l -> right.apply(model).map(r -> predicate.apply(l, r)))
                            .orElse(false));

        }

    }

}
