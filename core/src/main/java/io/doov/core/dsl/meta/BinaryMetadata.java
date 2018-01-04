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
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.DefaultOperator.and;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.*;

import io.doov.core.dsl.lang.Context;

public class BinaryMetadata extends PredicateMetadata {
    private final Metadata left;
    private final Operator operator;
    private final Metadata right;

    BinaryMetadata(Metadata left, Operator operator, Metadata right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Operator getOperator() {
        return operator;
    }

    public static BinaryMetadata andMetadata(Metadata left, Metadata right) {
        return new BinaryMetadata(left, DefaultOperator.and, right);
    }

    public static BinaryMetadata orMetadata(Metadata left, Metadata right) {
        return new BinaryMetadata(left, DefaultOperator.or, right);
    }

    @Override
    public String readable() {
        return astToString(this, Locale.getDefault());
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        visitor.start(this, depth);
        left.accept(visitor, depth + 1);
        visitor.visit(this, depth);
        right.accept(visitor, depth + 1);
        visitor.end(this, depth);
    }

    @Override
    public List<Metadata> children() {
        return Arrays.asList(left, right);
    }

    @Override
    public MetadataType type() {
        return BINARY_PREDICATE;
    }

    @Override
    public Metadata message(Context context) {
        if (operator == or && context.isEvalTrue(left) && context.isEvalFalse(right))
            return left.message(context);
        else if (operator == or && context.isEvalFalse(left) && context.isEvalTrue(right))
            return right.message(context);
        else if (operator == and && context.isEvalFalse(this))
            return new EmptyMetadata();
        return new BinaryMetadata(left.message(context), operator, right.message(context));
    }
}
