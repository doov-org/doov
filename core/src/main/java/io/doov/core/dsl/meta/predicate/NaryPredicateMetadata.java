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

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.ElementType.FIELD;
import static io.doov.core.dsl.meta.ElementType.OPERATOR;
import static io.doov.core.dsl.meta.MetadataType.EMPTY;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.*;

public class NaryPredicateMetadata extends NaryMetadata implements PredicateMetadata {
    private final AtomicInteger evalTrue = new AtomicInteger();
    private final AtomicInteger evalFalse = new AtomicInteger();

    private NaryPredicateMetadata(Operator operator, List<Metadata> values) {
        super(operator, values);
    }

    @Override
    public AtomicInteger evalTrue() {
        return evalTrue;
    }

    @Override
    public AtomicInteger evalFalse() {
        return evalFalse;
    }

    public static NaryPredicateMetadata matchAnyMetadata(List<Metadata> values) {
        return new NaryPredicateMetadata(match_any, values);
    }

    public static NaryPredicateMetadata matchAllMetadata(List<Metadata> values) {
        return new NaryPredicateMetadata(match_all, values);
    }

    public static NaryPredicateMetadata matchNoneMetadata(List<Metadata> values) {
        return new NaryPredicateMetadata(match_none, values);
    }

    public static NaryPredicateMetadata countMetadata(List<Metadata> values) {
        return new NaryPredicateMetadata(count, values);
    }

    public static NaryPredicateMetadata sumMetadata(List<Metadata> values) {
        return new NaryPredicateMetadata(sum, values);
    }

    public static NaryPredicateMetadata minMetadata(List<Metadata> values) {
        return new NaryPredicateMetadata(min, values);
    }

    @Override
    public PredicateMetadata merge(LeafMetadata<?> other) {
        final List<Element> elts = other.elements().stream().collect(Collectors.toList());
        if (elts.get(0).getType() == OPERATOR && (elts.get(0).getReadable() == sum || elts.get(0).getReadable() == min
                || elts.get(0).getReadable() == count)) {
            // special case to build : count (predicate ...) operator value
            return new BinaryPredicateMetadata(this, (Operator) elts.get(elts.size() - 2).getReadable(),
                    new LeafPredicateMetadata<>(LEAF_PREDICATE)
                            .valueReadable(elts.get(elts.size() - 1).getReadable()));
        }
        return new NaryPredicateMetadata(new ComposeOperator(getOperator(), other), getValues());
    }

    @Override
    public Metadata message(Context context) {
        if (getOperator() == match_all && context.isEvalFalse(this)) {
            final List<Metadata> childMsgs = children().filter(md -> context.isEvalFalse(md))
                    .map(md -> md.message(context)).filter(Objects::nonNull).filter(md -> EMPTY != md.type())
                    .collect(toList());
            if (childMsgs.size() == 1)
                return childMsgs.get(0);
            return new NaryPredicateMetadata(getOperator(), childMsgs);
        }
        if (getOperator() == match_all && context.isEvalTrue(this)) {
            return new EmptyMetadata();
        } else if (getOperator() == match_any && context.isEvalTrue(this)) {
            final List<Metadata> childMsgs = children().filter(md -> context.isEvalFalse(md))
                    .map(md -> md.message(context)).filter(Objects::nonNull).filter(md -> EMPTY != md.type())
                    .collect(toList());
            if (childMsgs.size() == 1)
                return childMsgs.get(0);
            return new NaryPredicateMetadata(getOperator(), childMsgs);
        } else if (getOperator() == sum) {
            return new NaryPredicateMetadata(sum, children().filter(md -> sumContentFilter(context, md))
                    .map(md -> md.message(context))
                    .filter(Objects::nonNull)
                    .filter(md -> EMPTY != md.type())
                    .collect(toList()));
        } else if (getOperator() == count) {
            final List<Metadata> childMsgs = children().filter(md -> context.isEvalFalse(md))
                    .map(md -> md.message(context))
                    .filter(Objects::nonNull)
                    .filter(md -> EMPTY != md.type())
                    .collect(toList());
            return rewriteCount(childMsgs);
        }
        return new NaryPredicateMetadata(getOperator(), children().map(md -> md.message(context))
                .filter(Objects::nonNull).filter(md -> EMPTY != md.type()).collect(toList()));
    }

    private static boolean sumContentFilter(Context context, Metadata md) {
        if (md.type() != FIELD_PREDICATE)
            return true;
        final List<Element> elements = md.flatten();
        if (elements.size() < 1)
            return true;
        if (elements.get(0).getType() != FIELD)
            return true;
        final Object value = context.getEvalValue(((DslField<?>) elements.get(0).getReadable()).id());
        if (value == null)
            return false;
        try {
            return Double.parseDouble(value.toString()) != 0;
        } catch (NumberFormatException e) {
            return true;
        }

    }

    private static Metadata rewriteCount(List<Metadata> childMsgs) {
        if (childMsgs.size() == 0)
            return new EmptyMetadata();
        if (childMsgs.size() == 1)
            return childMsgs.get(0);
        return new BinaryPredicateMetadata(childMsgs.get(0), and, rewriteCount(childMsgs.subList(1, childMsgs.size())));
    }
}
