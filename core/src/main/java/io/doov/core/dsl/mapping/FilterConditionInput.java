/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.impl.DefaultFunction;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public class FilterConditionInput<E, I extends Iterable<E>> extends AbstractDSLBuilder implements MappingInput<List<E>> {

    private final MappingInput<I> mappingInput;
    private Function<DefaultFunction<E, ? extends Metadata>, StepCondition> conditionFunction;
    private final FilterMetadata metadata;

    public FilterConditionInput(MappingInput<I> input,
            Function<DefaultFunction<E, ? extends Metadata>, StepCondition> conditionFunction) {
        this.mappingInput = input;
        this.conditionFunction = conditionFunction;
        DefaultFunction<E, Metadata> metadataInput =
                new DefaultFunction<>(StaticMetadata.create(() -> "item"), (entries, context) -> Optional.empty());
        StepCondition condition = conditionFunction.apply(metadataInput);
        this.metadata = FilterMetadata.create(mappingInput.metadata(), condition.metadata());

    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public List<E> read(FieldModel inModel, Context context) {
        return StreamSupport.stream(mappingInput.read(inModel, context).spliterator(), false)
                .filter(t -> conditionFunction.apply(DOOV.lift(t, DefaultFunction::new))
                        .predicate().test(inModel, context))
                .collect(Collectors.toList());
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return true;
    }
}
