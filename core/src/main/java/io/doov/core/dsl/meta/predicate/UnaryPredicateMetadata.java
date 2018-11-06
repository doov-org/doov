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

import static io.doov.core.dsl.meta.DefaultOperator.not;

import java.util.concurrent.atomic.AtomicInteger;

import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.*;

public class UnaryPredicateMetadata extends UnaryMetadata implements PredicateMetadata {
    private final AtomicInteger evalTrue = new AtomicInteger();
    private final AtomicInteger evalFalse = new AtomicInteger();

    private UnaryPredicateMetadata(Operator operator, Metadata value) {
        super(operator, value);
    }

    @Override
    public AtomicInteger evalTrue() {
        return evalTrue;
    }

    @Override
    public AtomicInteger evalFalse() {
        return evalFalse;
    }

    public static UnaryPredicateMetadata notMetadata(Metadata value) {
        return new UnaryPredicateMetadata(DefaultOperator.not, value);
    }

    /**
     * Be carrefull about the <em>boolean satisfiability problem</em> when we use the <b>not</b> operator
     * https://en.wikipedia.org/wiki/Boolean_satisfiability_problem
     */
    @Override
    public Metadata message(Context context) {
        if (getOperator() == not)
            return this;
        return new UnaryPredicateMetadata(getOperator(), getValue().message(context));
    }
}
