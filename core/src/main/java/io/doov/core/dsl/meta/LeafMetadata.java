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
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.ElementType.*;
import static java.util.stream.Collectors.joining;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.lang.Readable;

public abstract class LeafMetadata<M extends LeafMetadata<M>> extends AbstractMetadata {
    private static final Collector<CharSequence, ?, String> COLLECTOR_LIST = joining(", ", " : ", "");
    private final MetadataType type;
    private final Deque<Element> elements;

    public LeafMetadata(MetadataType type) {
        this(new ArrayDeque<>(), type);
    }

    private LeafMetadata(Deque<Element> elements, MetadataType type) {
        this.elements = elements;
        this.type = type;
    }

    @Override
    public MetadataType type() {
        return type;
    }

    public Deque<Element> elements() {
        return elements;
    }

    public List<Element> elementsAsList() {
        return new ArrayList<>(elements);
    }

    public M valueReadable(Readable readable) {
        return add(readable == null ? null : new Element(readable, VALUE));
    }

    // field

    public M field(DslField<?> readable) {
        return add(readable == null ? null : new Element(readable, FIELD));
    }

    // operator

    public M operator(Operator op) {
        return add(op == null ? null : new Element(op, OPERATOR));
    }

    // value

    public M valueObject(Object readable) {
        if (readable == null)
            return valueReadable(() -> "null");
        if (readable instanceof String)
            return valueString((String) readable);
        return valueReadable(() -> String.valueOf(readable));
    }

    @SuppressWarnings("unchecked")
    public M add(Element element) {
        if (element != null) {
            elements().add(element);
        }
        return (M) this;
    }

    public M valueString(String readable) {
        return add(readable == null ? null : new Element(() -> readable, STRING_VALUE));
    }

    public M valueSupplier(Supplier<?> readable) {
        return add(readable == null ? null : new Element(() -> String.valueOf(readable.get()), VALUE));
    }

    public M valueUnknown(String readable) {
        return add(readable == null ? null : new Element(() -> "-function- " + readable, UNKNOWN));
    }

    @SuppressWarnings("unchecked")
    public M valueCondition(DefaultCondition<?> condition) {
        LeafMetadata<?> conditionMetadata = (LeafMetadata<?>) condition.getMetadata();
        conditionMetadata.elements().stream().forEach(e -> add(e));
        return (M) this;
    }

    public M valueListReadable(Collection<? extends Readable> readables) {
        return add(readables == null || readables.isEmpty() ? null
                        : new Element(() -> formatListReadable(readables), VALUE));
    }

    public M valueListObject(Collection<?> readables) {
        return add(readables == null || readables.isEmpty() ? null
                        : new Element(() -> formatListObject(readables), VALUE));
    }

    private static String formatListReadable(Collection<? extends Readable> readables) {
        return readables.stream().map(Readable::readable).collect(COLLECTOR_LIST);
    }

    private static String formatListObject(Collection<?> readables) {
        return readables.stream().map(Object::toString).collect(COLLECTOR_LIST);
    }
}
