package io.doov.core.dsl.lang;

import java.util.function.BiPredicate;

import io.doov.core.FieldModel;
import io.doov.core.dsl.impl.LogicalBinaryCondition;
import io.doov.core.dsl.impl.LogicalUnaryCondition;
import io.doov.core.dsl.meta.Readable;

public interface StepCondition extends Readable {

    BiPredicate<FieldModel, Context> predicate();

    default StepCondition and(StepCondition condition) {
        return LogicalBinaryCondition.and(this, condition);
    }

    default StepCondition or(StepCondition condition) {
        return LogicalBinaryCondition.or(this, condition);
    }

    default StepCondition not() {
        return LogicalUnaryCondition.negate(this);
    }

}
