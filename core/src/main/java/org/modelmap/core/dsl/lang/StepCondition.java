package org.modelmap.core.dsl.lang;

import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.impl.LogicalBinaryCondition;
import org.modelmap.core.dsl.impl.LogicalUnaryCondition;
import org.modelmap.core.dsl.meta.Readable;

public interface StepCondition extends Readable {

    Predicate<FieldModel> predicate();

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
