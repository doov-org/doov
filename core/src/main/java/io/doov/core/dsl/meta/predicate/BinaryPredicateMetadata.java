/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.core.dsl.meta.predicate;

import static io.doov.core.dsl.meta.DefaultOperator.equals;
import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;

import java.util.concurrent.atomic.AtomicInteger;

import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.ReduceType;
import io.doov.core.dsl.meta.*;

public class BinaryPredicateMetadata extends BinaryMetadata implements PredicateMetadata {
    private final AtomicInteger evalTrue = new AtomicInteger();
    private final AtomicInteger evalFalse = new AtomicInteger();

    BinaryPredicateMetadata(Metadata left, Operator operator, Metadata right) {
        super(left, operator, right);
    }

    @Override
    public AtomicInteger evalTrue() {
        return evalTrue;
    }

    @Override
    public AtomicInteger evalFalse() {
        return evalFalse;
    }
    
    public static BinaryPredicateMetadata equalsMetadata(Metadata left, Metadata right) {
        return new BinaryPredicateMetadata(left, equals, right);
    }
    
    public static BinaryPredicateMetadata notEqualsMetadata(Metadata left, Metadata right) {
        return new BinaryPredicateMetadata(left, not_equals, right);
    }

    public static BinaryPredicateMetadata andMetadata(Metadata left, Metadata right) {
        return new BinaryPredicateMetadata(left, and, right);
    }

    public static BinaryPredicateMetadata orMetadata(Metadata left, Metadata right) {
        return new BinaryPredicateMetadata(left, or, right);
    }

    @Override
    public Metadata reduce(Context context, ReduceType type) {
        boolean result = context.isEvalTrue(this) || !context.isEvalFalse(this);
        if (!result && type == ReduceType.SUCCESS) {
            return new EmptyMetadata();
        }
        if (result && type == ReduceType.FAILURE) {
            return new EmptyMetadata();
        }
        boolean left = context.isEvalTrue(getLeft()) || !context.isEvalFalse(getLeft());
        boolean right = context.isEvalTrue(getRight()) || !context.isEvalFalse(getRight());
        if (getOperator() == and) {
            if (!right && left) {
                return getRight().reduce(context, type);
            }
            if (!left && right) {
                return getLeft().reduce(context, type);
            }
        } else if (getOperator() == or) {
            if (!right && left) {
                return getLeft().reduce(context, type);
            }
            if (!left && right) {
                return getRight().reduce(context, type);
            }
        } else if (getLeft().type() == NARY_PREDICATE && ((NaryPredicateMetadata) getLeft()).getOperator() == count) {
            return getLeft().reduce(context, type);
        }
        return new BinaryPredicateMetadata(getLeft().reduce(context, type), getOperator(), getRight().reduce(context, type));
    }
}
