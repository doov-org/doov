/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import static java.util.stream.Collectors.toList;

import java.util.*;
import java.util.stream.StreamSupport;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.builder.IterableStepMap;
import io.doov.core.dsl.meta.ConcatMetadata;
import io.doov.core.dsl.meta.Metadata;

public class ConcatInput<T> extends AbstractDSLBuilder implements MappingInput<List<T>> {

    private final List<IterableStepMap<T, ? extends Iterable<T>>> iterablesInputList;
    private final Metadata metadata;

    public ConcatInput(IterableStepMap<T, ? extends Iterable<T>>... iterablesInputs) {
        this.iterablesInputList = Arrays.asList(iterablesInputs);
        this.metadata = ConcatMetadata.create(iterablesInputList.stream()
                .map(IterableStepMap::getInput)
                .map(MappingInput::metadata)
                .collect(toList()));
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public List<T> read(FieldModel inModel, Context context) {
        return iterablesInputList.stream()
                .flatMap(r -> StreamSupport.stream(r.getInput().read(inModel, context).spliterator(), false))
                .collect(toList());
    }

    @Override
    public boolean validate(FieldModel inModel) {
        return true;
    }
}
