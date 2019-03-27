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
package io.doov.core.dsl.meta.predicate;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE_MATCH_ANY;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.readableMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.valueListMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.valueMetadata;

import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import io.doov.core.FieldId;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.ReduceType;
import io.doov.core.dsl.meta.*;

public class BinaryPredicateMetadata extends BinaryMetadata implements PredicateMetadata {
    private final AtomicInteger evalTrue = new AtomicInteger();
    private final AtomicInteger evalFalse = new AtomicInteger();

    protected BinaryPredicateMetadata(Metadata left, Operator operator, Metadata right) {
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

    public static BinaryPredicateMetadata noneMatchMetadata(Metadata metadata, Collection<?> values) {
        return new BinaryPredicateMetadata(metadata, none_match_values, valueListMetadata(values));
    }

    public static BinaryPredicateMetadata equalsMetadata(Metadata metadata, Object value) {
        return new BinaryPredicateMetadata(metadata, equals, valueMetadata(value));
    }

    public static BinaryPredicateMetadata equalsMetadata(Metadata metadata, Supplier<?> supplier) {
        return new BinaryPredicateMetadata(metadata, equals, readableMetadata(lambda));
    }

    public static BinaryPredicateMetadata equalsMetadata(Metadata metadata, Readable value) {
        return new BinaryPredicateMetadata(metadata, equals, readableMetadata(value));
    }

    public static BinaryPredicateMetadata equalsMetadata(Metadata metadata, DefaultCondition<?> condition) {
        return new BinaryPredicateMetadata(metadata, equals, condition.metadata());
    }

    public static BinaryPredicateMetadata notEqualsMetadata(Metadata metadata, Object value) {
        return new BinaryPredicateMetadata(metadata, not_equals, valueMetadata(value));
    }

    public static BinaryPredicateMetadata notEqualsMetadata(Metadata metadata, Supplier<?> supplier) {
        return new BinaryPredicateMetadata(metadata, not_equals, readableMetadata(lambda));
    }

    public static BinaryPredicateMetadata notEqualsMetadata(Metadata metadata, Readable value) {
        return new BinaryPredicateMetadata(metadata, not_equals, readableMetadata(value));
    }

    public static BinaryPredicateMetadata anyMatchMetadata(Metadata metadata) {
        return new BinaryPredicateMetadata(metadata, any_match_values,
                ValuePredicateMetadata.anyMatchMetadata(metadata));
    }

    public static BinaryPredicateMetadata anyMatchMetadata(Metadata metadata, Collection<?> values) {
        return new BinaryPredicateMetadata(metadata, any_match_values, ValuePredicateMetadata.anyMatchMetadata(values));
    }

    public static BinaryPredicateMetadata allMatchMetadata(Metadata metadata) {
        return new BinaryPredicateMetadata(metadata, all_match_values, readableMetadata(lambda));
    }

    public static BinaryPredicateMetadata allMatchMetadata(Metadata metadata, Collection<?> values) {
        return new BinaryPredicateMetadata(metadata, all_match_values, valueListMetadata(values));
    }

    public static BinaryPredicateMetadata matchNoneMetadata(Metadata metadata) {
        return new BinaryPredicateMetadata(metadata, none_match_values, readableMetadata(lambda));
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
        } else if (getOperator() == any_match_values) {
            if (getRight().type() == FIELD_PREDICATE_MATCH_ANY) {
                FieldId fieldId = left().filter(m -> m.type() == FIELD_PREDICATE).findFirst()
                        .map(m -> ((LeafMetadata<?>) m).elements()).map(Deque::getFirst)
                        .map(e -> (DslField<?>) e.getReadable()).map(DslField::id).orElse(null);
                return new BinaryPredicateMetadata(getLeft().reduce(context, type), equals,
                        ValuePredicateMetadata.valueMetadata(context.getEvalValue(fieldId)));
            }
        }
        return new BinaryPredicateMetadata(getLeft().reduce(context, type), getOperator(),
                getRight().reduce(context, type));
    }
}
