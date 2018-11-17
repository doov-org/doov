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
import static io.doov.core.dsl.meta.Element.leftParenthesis;
import static io.doov.core.dsl.meta.Element.rightParenthesis;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE_MATCH_ANY;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.*;

public class LeafPredicateMetadata<M extends LeafPredicateMetadata<M>> extends LeafMetadata<M>
        implements PredicateMetadata {
    private final AtomicInteger evalTrue = new AtomicInteger();
    private final AtomicInteger evalFalse = new AtomicInteger();

    private LeafPredicateMetadata(Deque<Element> elements, MetadataType type) {
        super(elements, type);
    }

    public LeafPredicateMetadata(MetadataType type) {
        this(new ArrayDeque<>(), type);
    }

    public LeafPredicateMetadata(Metadata metadata) {
        this(new ArrayDeque<>(metadata.flatten()), metadata.type());
    }

    public LeafPredicateMetadata(Metadata metadata, MetadataType type) {
        this(new ArrayDeque<>(metadata.flatten()), type);
    }

    @Override
    public AtomicInteger evalTrue() {
        return evalTrue;
    }

    @Override
    public AtomicInteger evalFalse() {
        return evalFalse;
    }

    @Override
    public LeafPredicateMetadata<M> merge(LeafMetadata<?> other) {
        removeDuplicate(elements(), other.elements());
        final Deque<Element> merge = new ArrayDeque<>(elements());
        merge.addAll(other.elements());
        return new LeafPredicateMetadata<M>(merge, mergeType(type(), other.type()));
    }

    private static MetadataType mergeType(MetadataType current, MetadataType merged) {
        if (current == FIELD_PREDICATE && merged == FIELD_PREDICATE_MATCH_ANY)
            return FIELD_PREDICATE_MATCH_ANY;
        return current;
    }

    private static void removeDuplicate(Deque<Element> current, Deque<Element> merged) {
        if (current.isEmpty() || merged.isEmpty())
            return;
        for (Element element : current) {
            if (merged.isEmpty())
                break;
            if (merged.peek().getType() == element.getType()
                    && merged.peek().getReadable().readable().equals(element.getReadable().readable()))
                merged.pop();
        }
    }

    @Override
    public Metadata reduce(Context context) {
        if (type() == FIELD_PREDICATE_MATCH_ANY) {
            final DslField<?> field = (DslField<?>) elements().getFirst().getReadable();
            return new LeafPredicateMetadata<M>(FIELD_PREDICATE).field(field).operator(not_equals)
                    .valueObject(context.getEvalValue(field.id()));
        }
        return this;
    }

    public static <M extends LeafPredicateMetadata<M>> M fieldMetadata(DslField<?> field) {
        return new LeafPredicateMetadata<M>(FIELD_PREDICATE).field(field);
    }

    // boolean

    public static <M extends LeafPredicateMetadata<M>> M trueMetadata() {
        return new LeafPredicateMetadata<M>(LEAF_PREDICATE).operator(always_true);
    }

    public static <M extends LeafPredicateMetadata<M>> M falseMetadata() {
        return new LeafPredicateMetadata<M>(LEAF_PREDICATE).operator(always_false);
    }

    // when

    public static <M extends LeafPredicateMetadata<M>> M whenMetadata(Metadata metadata, StepCondition condition) {
        final M exp = new LeafPredicateMetadata<M>(metadata, FIELD_PREDICATE).operator(when);
        exp.elements().add(leftParenthesis());
        exp.elements().addAll(condition.metadata().flatten());
        exp.elements().add(rightParenthesis());
        return exp;
    }

    // equals

    public static <M extends LeafPredicateMetadata<M>> M equalsMetadata(Metadata metadata, Object value) {
        return new LeafPredicateMetadata<M>(metadata, FIELD_PREDICATE).operator(equals).valueObject(value);
    }

    public static <M extends LeafPredicateMetadata<M>> M equalsMetadata(Metadata metadata, Readable value) {
        return new LeafPredicateMetadata<M>(metadata, FIELD_PREDICATE).operator(equals).valueReadable(value);
    }

    public static <M extends LeafPredicateMetadata<M>> M equalsMetadata(Metadata metadata,
            DefaultCondition<?> condition) {
        return new LeafPredicateMetadata<M>(metadata, FIELD_PREDICATE).operator(equals).valueCondition(condition);
    }

    public static <M extends LeafPredicateMetadata<M>> M notEqualsMetadata(Metadata metadata, Object value) {
        return new LeafPredicateMetadata<M>(metadata, FIELD_PREDICATE).operator(not_equals).valueObject(value);
    }

    public static <M extends LeafPredicateMetadata<M>> M notEqualsMetadata(Metadata metadata, Readable value) {
        return new LeafPredicateMetadata<M>(metadata, FIELD_PREDICATE).operator(not_equals).valueReadable(value);
    }

    // null

    public static <M extends LeafPredicateMetadata<M>> M nullMetadata(Metadata metadata) {
        return new LeafPredicateMetadata<M>(metadata, FIELD_PREDICATE).operator(is_null);
    }

    public static <M extends LeafPredicateMetadata<M>> M notNullMetadata(Metadata metadata) {
        return new LeafPredicateMetadata<M>(metadata, FIELD_PREDICATE).operator(is_not_null);
    }

    // match

    public static <M extends LeafPredicateMetadata<M>> M matchAnyMetadata(Metadata metadata) {
        return new LeafPredicateMetadata<M>(metadata, FIELD_PREDICATE_MATCH_ANY).operator(match_any)
                .valueUnknown("-function-");
    }

    public static <M extends LeafPredicateMetadata<M>> M matchAnyMetadata(Metadata metadata, Collection<?> values) {
        return new LeafPredicateMetadata<M>(metadata, FIELD_PREDICATE_MATCH_ANY).operator(match_any)
                .valueListObject(values);
    }

    public static <M extends LeafPredicateMetadata<M>> M matchAllMetadata(Metadata metadata) {
        return new LeafPredicateMetadata<M>(metadata, FIELD_PREDICATE).operator(match_all).valueUnknown("-function-");
    }

    public static <M extends LeafPredicateMetadata<M>> M matchAllMetadata(Metadata metadata, Collection<?> values) {
        return new LeafPredicateMetadata<M>(metadata, FIELD_PREDICATE).operator(match_all).valueListObject(values);
    }

    public static <M extends LeafPredicateMetadata<M>> M matchNoneMetadata(Metadata metadata) {
        return new LeafPredicateMetadata<M>(metadata, FIELD_PREDICATE).operator(match_none).valueUnknown("-function-");
    }

    public static <M extends LeafPredicateMetadata<M>> M matchNoneMetadata(Metadata metadata, Collection<?> values) {
        return new LeafPredicateMetadata<M>(metadata, FIELD_PREDICATE).operator(match_none).valueListObject(values);
    }
}
