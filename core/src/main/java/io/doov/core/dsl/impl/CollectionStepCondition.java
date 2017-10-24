/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.util.function.BiPredicate;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.Metadata;

public class CollectionStepCondition extends AbstractStepCondition {

    public CollectionStepCondition(Metadata metadata, BiPredicate<FieldModel, Context> predicate) {
        super(metadata, predicate);
    }

}
