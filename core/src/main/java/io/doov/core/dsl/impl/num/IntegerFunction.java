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
package io.doov.core.dsl.impl.num;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.grammar.mapping.Convert1;
import io.doov.core.dsl.grammar.ASTNode;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class IntegerFunction extends NumericFunction<Integer> implements IntegerOperators {

    public IntegerFunction(DslField<Integer> field) {
        super(field);
    }

    public IntegerFunction(PredicateMetadata metadata, ASTNode<Integer> ast,BiFunction<FieldModel, Context, Optional<Integer>> value) {
        super(metadata, ast, value);
    }

    public IntegerFunction(NumericCondition<Long> condition) {
        this(condition.getMetadata(),new Convert1<>(condition.ast(),Long::intValue),
                (model, context) -> condition.getFunction().apply(model, context).map(Long::intValue));
    }

    @Override
    protected IntegerFunction numericFunction(PredicateMetadata metadata, ASTNode<Integer> ast,
                    BiFunction<FieldModel, Context, Optional<Integer>> value) {
        return new IntegerFunction(metadata, ast, value);
    }

    @Override
    protected Class<Integer> classTag() {
        return Integer.class;
    }

}
