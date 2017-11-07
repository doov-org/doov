/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package io.doov.core.dsl.lang;

import java.util.function.BiPredicate;

import io.doov.core.dsl.BaseModel;
import io.doov.core.dsl.impl.LogicalBinaryCondition;
import io.doov.core.dsl.impl.LogicalUnaryCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.SyntaxTree;

public interface StepCondition extends Readable, SyntaxTree {

    BiPredicate<BaseModel, Context> predicate();

    Metadata getMetadata();

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
