package io.doov.core.dsl.impl;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.meta.Metadata;

public class PredicateStepCondition<N> extends AbstractStepCondition {

    PredicateStepCondition(Metadata metadata,
                    Function<FieldModel, Optional<N>> value,
                    Function<N, Boolean> predicate) {
        super(metadata, (model, context) -> value.apply(model).map(predicate).orElse(false));
    }

    PredicateStepCondition(Metadata metadata,
                    Function<FieldModel, Optional<N>> left,
                    Function<FieldModel, Optional<N>> right,
                    BiFunction<N, N, Boolean> predicate) {
        super(metadata, (model, context) -> left.apply(model)
                        .flatMap(l -> right.apply(model).map(r -> predicate.apply(l, r)))
                        .orElse(false));

    }

}
