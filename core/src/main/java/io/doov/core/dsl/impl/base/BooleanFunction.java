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
package io.doov.core.dsl.impl.base;

import static io.doov.core.dsl.meta.function.BooleanFunctionMetadata.andMetadata;
import static io.doov.core.dsl.meta.function.BooleanFunctionMetadata.isMetadata;
import static io.doov.core.dsl.meta.function.BooleanFunctionMetadata.orMetadata;
import static io.doov.core.dsl.meta.predicate.UnaryPredicateMetadata.notMetadata;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.types.LogicalFieldInfo;
import io.doov.core.dsl.grammar.bool.Equals;
import io.doov.core.dsl.grammar.bool.*;
import io.doov.core.dsl.grammar.leaf.Constant;
import io.doov.core.dsl.grammar.leaf.NotYetImplemented;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.impl.LeafStepCondition;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.function.BooleanFunctionMetadata;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

/**
 * Base class for boolean conditions.
 * <p>
 * It contains a {@link DslField} to get the value from the model, a {@link BooleanFunctionMetadata} to describe this node, and a
 * {@link BiFunction} to take the value from the model and return an optional value.
 */
public class BooleanFunction extends DefaultCondition<Boolean> {

    public BooleanFunction(DslField<Boolean> field) {
        super(field);
    }

    public BooleanFunction(PredicateMetadata metadata, BiFunction<FieldModel, Context, Optional<Boolean>> value) {
        super(metadata, value);
    }

    /**
     * Returns a step condition checking if the node value is not true.
     *
     * @return the step condition
     */
    public final StepCondition not() {
        return LeafStepCondition.stepCondition(notMetadata(metadata), new Not(ast), getFunction(), value -> !value);
    }

    /**
     * Returns a step condition checking if the node value and the given value is true.
     *
     * @param value the right value
     * @return the step condition
     */
    public final StepCondition and(boolean value) {
        return LeafStepCondition.stepCondition(
                andMetadata(metadata, value),
                new And(ast,new Constant<>(value)),
                getFunction(), value,
                Boolean::logicalAnd);
    }

    /**
     * Returns a step condition checking if the node value and the given field value is true.
     *
     * @param value the right field value
     * @return the step condition
     */
    public final StepCondition and(LogicalFieldInfo value) {
        return LeafStepCondition.stepCondition(
                andMetadata(metadata, value),
                new NotYetImplemented<>(LogicalFieldInfo.class),
                getFunction(), value,
                Boolean::logicalAnd);
    }

    /**
     * Returns a step condition checking if the node value or the given value is true.
     *
     * @param value the right value
     * @return the step condition
     */
    public final StepCondition or(boolean value) {
        return LeafStepCondition.stepCondition(
                orMetadata(metadata, value),
                new Or(ast, new Constant<>(value)),
                getFunction(), value,
                Boolean::logicalOr);
    }

    /**
     * Returns a step condition checking if the node value or the given field value is true.
     *
     * @param value the right value
     * @return the step condition
     */
    public final StepCondition or(LogicalFieldInfo value) {
        return LeafStepCondition.stepCondition(orMetadata(metadata, value),
                new NotYetImplemented<>(LogicalFieldInfo.class),
                getFunction(), value,
                Boolean::logicalOr);
    }

    /**
     * Returns a step condition checking if the node value is true.
     *
     * @return the step condition
     */
    public final StepCondition isTrue() {
        return LeafStepCondition.stepCondition(
                isMetadata(metadata, true),
                new Equals<>(ast,new Constant<>(true)),
                getFunction(), TRUE,
                Boolean::equals);
    }

    /**
     * Returns a step condition checking if the node value is false.
     *
     * @return the step condition
     */
    public final StepCondition isFalse() {
        return LeafStepCondition.stepCondition(
                isMetadata(metadata, false),
                new Equals<>(ast,new Constant<>(false)),
                getFunction(), FALSE,
                Boolean::equals);
    }

}
