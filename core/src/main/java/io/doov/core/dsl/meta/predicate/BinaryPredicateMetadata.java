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

import static io.doov.core.dsl.meta.DefaultOperator.and;
import static io.doov.core.dsl.meta.DefaultOperator.count;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;

import java.util.concurrent.atomic.AtomicInteger;

import io.doov.core.dsl.lang.Context;
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

    public static BinaryPredicateMetadata andMetadata(Metadata left, Metadata right) {
        return new BinaryPredicateMetadata(left, and, right);
    }

    public static BinaryPredicateMetadata orMetadata(Metadata left, Metadata right) {
        return new BinaryPredicateMetadata(left, or, right);
    }

    @Override
    public Metadata message(Context context) {
        if (getOperator() == or && context.isEvalTrue(getLeft()) && context.isEvalFalse(getRight()))
            return getLeft().message(context);
        else if (getOperator() == or && context.isEvalFalse(getLeft()) && context.isEvalTrue(getRight()))
            return getRight().message(context);
        else if (getOperator() == and && context.isEvalTrue(getLeft()) && context.isEvalFalse(getRight()))
            return getRight().message(context);
        else if (getOperator() == and && context.isEvalFalse(getLeft()) && context.isEvalTrue(getRight()))
            return getLeft().message(context);
        else if (getLeft().type() == NARY_PREDICATE && ((NaryPredicateMetadata) getLeft()).getOperator() == count)
            return getLeft().message(context);
        return new BinaryPredicateMetadata(getLeft().message(context), getOperator(), getRight().message(context));
    }
}
