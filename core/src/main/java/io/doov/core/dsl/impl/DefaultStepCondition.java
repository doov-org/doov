package io.doov.core.dsl.impl;

import java.util.function.BiPredicate;

import io.doov.core.dsl.BaseModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.Metadata;

public class DefaultStepCondition extends AbstractStepCondition {

    public DefaultStepCondition(Metadata metadata, BiPredicate<BaseModel, Context> predicate) {
        super(metadata, predicate);
    }

}
