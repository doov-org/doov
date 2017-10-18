package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.emptyMetadata;

import java.util.Optional;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.DefaultFieldInfo;
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

    N value(FieldModel model, F field) {
        return model.get(field.id());
    }

    DefaultStepCondition<N> step(FieldMetadata metadata,
                    Function<FieldModel, Function<N, Boolean>> predicate) {
        return new DefaultStepCondition<>(this.metadata.merge(metadata), this.value, predicate);
    }

    static class DefaultStepCondition<N> extends AbstractStepCondition {

        DefaultStepCondition(Metadata metadata,
                        Function<FieldModel, Optional<N>> value,
                        Function<FieldModel, Function<N, Boolean>> predicate) {
            super(metadata, (model, context) -> value.apply(model).map(predicate.apply(model)).orElse(false));
        }

    }

}
