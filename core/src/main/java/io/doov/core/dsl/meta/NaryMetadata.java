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

import static io.doov.core.dsl.meta.DefaultOperator.count;
import static io.doov.core.dsl.meta.DefaultOperator.match_all;
import static io.doov.core.dsl.meta.DefaultOperator.match_any;
import static io.doov.core.dsl.meta.DefaultOperator.sum;
import static io.doov.core.dsl.meta.ElementType.OPERATOR;
import static io.doov.core.dsl.meta.MetadataType.EMPTY;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.Context;

public class NaryMetadata extends PredicateMetadata {
    private final Operator operator;
    private final List<Metadata> values;

    private NaryMetadata(Operator operator, List<Metadata> values) {
        this.operator = operator;
        this.values = values;
    }

    public Operator getOperator() {
        return operator;
    }

    public static NaryMetadata matchAnyMetadata(List<Metadata> values) {
        return new NaryMetadata(DefaultOperator.match_any, values);
    }

    public static NaryMetadata matchAllMetadata(List<Metadata> values) {
        return new NaryMetadata(DefaultOperator.match_all, values);
    }

    public static NaryMetadata matchNoneMetadata(List<Metadata> values) {
        return new NaryMetadata(DefaultOperator.match_none, values);
    }

    public static NaryMetadata countMetadata(List<Metadata> values) {
        return new NaryMetadata(DefaultOperator.count, values);
    }

    public static NaryMetadata sumMetadata(List<Metadata> values) {
        return new NaryMetadata(DefaultOperator.sum, values);
    }

    public static NaryMetadata minMetadata(List<Metadata> values) {
        return new NaryMetadata(DefaultOperator.min, values);
    }

    @Override
    public String readable() {
        return astToString(this, Locale.getDefault());
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        visitor.start(this, depth);
        values.stream().filter(Objects::nonNull)
                        .filter(md -> EMPTY != md.type())
                        .forEach(v -> {
                            v.accept(visitor, depth + 1);
                            visitor.visit(this, depth);
                        });
        visitor.end(this, depth);
    }

    @Override
    public PredicateMetadata merge(LeafMetadata other) {
        final List<Element> elts = other.stream().collect(Collectors.toList());
        if (elts.size() == 2 && elts.get(0).getType() == OPERATOR) {
            // special case to build : count (predicate ...) operator value
            return new BinaryMetadata(this, (Operator) elts.get(0).getReadable(),
                            new LeafMetadata(LEAF_PREDICATE).valueReadable(elts.get(1).getReadable()));
        }
        return new NaryMetadata(new ComposeOperator(operator, other), values);
    }

    @Override
    public List<Element> flatten() {
        final List<Element> flatten = new ArrayList<>();
        flatten.add(new Element(operator, OPERATOR));
        flatten.addAll(values.stream().map(Metadata::flatten).flatMap(List::stream).collect(Collectors.toList()));
        return flatten;
    }

    @Override
    public List<Metadata> children() {
        return Collections.unmodifiableList(values);
    }

    @Override
    public MetadataType type() {
        return NARY_PREDICATE;
    }

    @Override
    public Metadata message(Context context) {
        if (operator == match_all && context.isEvalFalse(this)) {
            return new EmptyMetadata();
        } else if (operator == match_any) {
            final List<Metadata> childMsgs = values.stream()
                            .filter(md -> context.isEvalTrue(md))
                            .map(md -> md.message(context))
                            .filter(Objects::nonNull)
                            .filter(md -> EMPTY != md.type())
                            .collect(toList());
            if (childMsgs.size() == 1)
                return childMsgs.get(0);
            return new NaryMetadata(operator, childMsgs);
        } else if (operator == sum) {
            return new NaryMetadata(operator, values.stream()
                            .filter(md -> {
                                if (md.type() != FIELD_PREDICATE)
                                    return true;
                                final List<Element> elements = md.flatten();
                                if (elements.size() < 1)
                                    return true;
                                if (elements.get(0).getType() != ElementType.FIELD)
                                    return true;
                                final Number value = (Number) context
                                                .getEvalValue(((DslField) elements.get(0).getReadable()).id());
                                return value.intValue() != 0;
                            }).map(md -> md.message(context))
                            .filter(Objects::nonNull)
                            .filter(md -> EMPTY != md.type())
                            .collect(toList()));
        } else if (operator == count) {
            final List<Metadata> childMsgs = values.stream()
                            .filter(md -> context.isEvalTrue(md))
                            .map(md -> md.message(context))
                            .filter(Objects::nonNull)
                            .filter(md -> EMPTY != md.type())
                            .collect(toList());
            return new NaryMetadata(operator, childMsgs);
        }
        return new NaryMetadata(operator, values.stream()
                        .map(md -> md.message(context))
                        .filter(Objects::nonNull)
                        .filter(md -> EMPTY != md.type())
                        .collect(toList()));
    }
}
