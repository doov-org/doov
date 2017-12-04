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

import static io.doov.core.dsl.meta.FieldMetadata.Type.field;
import static io.doov.core.dsl.meta.FieldMetadata.Type.operator;
import static io.doov.core.dsl.meta.FieldMetadata.Type.value;
import static java.util.stream.Collectors.joining;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.ast.AstVisitorUtils;

public class FieldMetadata implements Metadata {

    private static final Collector<CharSequence, ?, String> COLLECTOR_LIST = joining(", ", "[", "]");

    private static final FieldMetadata EMPTY = new FieldMetadata();

    private final Deque<Element> elements;

    public FieldMetadata() {
        elements = new LinkedList<>();
    }

    public FieldMetadata(FieldMetadata metadata) {
        elements = new LinkedList<>(metadata.elements);
    }

    public Stream<Element> stream() {
        return elements.stream();
    }

    // elements

    FieldMetadata elements(FieldMetadata readable) {
        if (readable != null) {
            elements.addAll(readable.elements);
        }
        return this;
    }

    // field

    private FieldMetadata field(Readable readable) {
        if (readable != null) {
            elements.add(new Element(readable, field));
        }
        return this;
    }

    // operator

    FieldMetadata operator(Readable readable) {
        if (readable != null) {
            elements.add(new Element(readable, operator));
        }
        return this;
    }

    private FieldMetadata operator(String readable) {
        if (readable != null) {
            elements.add(new Element(() -> readable, operator));
        }
        return this;
    }

    // value

    private FieldMetadata valueListReadable(Collection<? extends Readable> readables) {
        if (readables != null && !readables.isEmpty()) {
            elements.add(new Element(() -> formatListReadable(readables), value));
        }
        return this;
    }

    private FieldMetadata valueListObject(Collection<?> readables) {
        if (readables != null && !readables.isEmpty()) {
            elements.add(new Element(() -> formatListObject(readables), value));
        }
        return this;
    }

    private FieldMetadata valueObject(Object readable) {
        if (readable != null) {
            elements.add(new Element(() -> String.valueOf(readable), value));
        }
        return this;
    }

    private FieldMetadata valueString(String readable) {
        if (readable != null) {
            elements.add(new Element(() -> readable, value));
        }
        return this;
    }

    private FieldMetadata valueReadable(Readable readable) {
        if (readable != null) {
            elements.add(new Element(readable, value));
        }
        return this;
    }

    private FieldMetadata valueSupplier(Supplier<Object> readable) {
        if (readable != null) {
            elements.add(new Element(() -> String.valueOf(readable.get()), value));
        }
        return this;
    }

    // implementation

    @Override
    public Metadata merge(FieldMetadata other) {
        removeDuplicateField(other);
        FieldMetadata fieldMetadata = new FieldMetadata(this);
        fieldMetadata.elements.addAll(other.elements);
        return fieldMetadata;
    }

    private void removeDuplicateField(FieldMetadata other) {
        if (this.elements.isEmpty() || other.elements.isEmpty()) {
            return;
        }
        if (this.elements.peek().type.equals(Type.field) && other.elements.peek().type.equals(Type.field)) {
            other.elements.pop();
        }
    }

    @Override
    public String readable() {
        return AstVisitorUtils.astToString(this);
    }

    @Override
    public void accept(MetadataVisitor visitor) {
        visitor.start(this);
        visitor.visit(this);
        visitor.end(this);
    }

    // static

    public static FieldMetadata emptyMetadata() {
        return EMPTY;
    }

    public static FieldMetadata fieldMetadata(Readable field) {
        return new FieldMetadata().field(field);
    }

    // boolean

    public static FieldMetadata trueMetadata() {
        return new FieldMetadata().operator("always true");
    }

    public static FieldMetadata falseMetadata() {
        return new FieldMetadata().operator("always false");
    }

    // min

    public static FieldMetadata minMetadata(Collection<? extends Readable> values) {
        return new FieldMetadata().operator("min").valueListReadable(values);
    }

    // sum

    public static FieldMetadata sumMetadata(Collection<? extends Readable> values) {
        return new FieldMetadata().operator("sum").valueListReadable(values);
    }

    // times

    public static FieldMetadata timesMetadata(Readable field, int multiplier) {
        return new FieldMetadata().field(field).operator("times").valueObject(multiplier);
    }

    // when

    public static FieldMetadata whenMetadata(Readable field, Readable condition) {
        return new FieldMetadata().field(field).operator("when").valueReadable(condition);
    }

    // equals

    public static FieldMetadata equalsMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator("equals").valueObject(value);
    }

    public static FieldMetadata equalsMetadata(Readable field, Readable value) {
        return new FieldMetadata().field(field).operator("equals").valueReadable(value);
    }

    public static FieldMetadata notEqualsMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator("not equals").valueObject(value);
    }

    // null

    public static FieldMetadata nullMetadata(Readable field) {
        return new FieldMetadata().field(field).operator("is null");
    }

    public static FieldMetadata notNullMetadata(Readable field) {
        return new FieldMetadata().field(field).operator("is not null");
    }

    // match

    public static FieldMetadata matchAnyMetadata(Readable field, Collection<?> values) {
        return new FieldMetadata().field(field).operator("match any").valueListObject(values);
    }

    public static FieldMetadata matchAllMetadata(Readable field, Collection<?> values) {
        return new FieldMetadata().field(field).operator("match all").valueListObject(values);
    }

    public static FieldMetadata matchNoneMetadata(Readable field, Collection<?> values) {
        return new FieldMetadata().field(field).operator("match none").valueListObject(values);
    }

    // map

    public static FieldMetadata mapToIntMetadata(Readable field) {
        return new FieldMetadata().field(field).operator("map to int");
    }

    // with

    public static FieldMetadata withMetadata(Readable field, Readable adjuster) {
        return new FieldMetadata().field(field).operator("with").valueReadable(adjuster);
    }

    // minus

    public static FieldMetadata minusMetadata(Readable field, int value, Object unit) {
        return new FieldMetadata().field(field).operator("minus").valueString(value + " " + unit);
    }

    public static FieldMetadata minusMetadata(Readable field1, Readable field2, Object unit) {
        return new FieldMetadata().field(field1).operator("minus").valueString(field2 + " " + unit);
    }

    // plus

    public static FieldMetadata plusMetadata(Readable field, int value, Object unit) {
        return new FieldMetadata().field(field).operator("plus").valueString(value + " " + unit);
    }

    public static FieldMetadata plusMetadata(Readable field1, Readable field2, Object unit) {
        return new FieldMetadata().field(field1).operator("plus").valueString(field2 + " " + unit);
    }

    // after

    public static FieldMetadata afterMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator("after").valueObject(value);
    }

    public static FieldMetadata afterMetadata(Readable field1, Readable field2) {
        return new FieldMetadata().field(field1).operator("after").valueReadable(field2);
    }

    public static FieldMetadata afterMetadata(Readable field, Supplier<Object> value) {
        return new FieldMetadata().field(field).operator("after").valueSupplier(value);
    }

    // before

    public static FieldMetadata beforeMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator("before").valueObject(value);
    }

    public static FieldMetadata beforeMetadata(Readable field1, Readable field2) {
        return new FieldMetadata().field(field1).operator("before").valueObject(field2);
    }

    public static FieldMetadata beforeMetadata(Readable field, Supplier<Object> value) {
        return new FieldMetadata().field(field).operator("before").valueSupplier(value);
    }

    // age at

    public static FieldMetadata ageAtMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator("age at").valueObject(value);
    }

    public static FieldMetadata ageAtMetadata(Readable field1, Readable field2) {
        return new FieldMetadata().field(field1).operator("age at").valueReadable(field2);
    }

    public static FieldMetadata ageAtMetadata(Readable field, Supplier<Object> supplier) {
        return new FieldMetadata().field(field).operator("age at").valueSupplier(supplier);
    }

    // string

    public static FieldMetadata matchesMetadata(Readable field, String value) {
        return new FieldMetadata().field(field).operator("matches").valueString(value);
    }

    public static FieldMetadata containsMetadata(Readable field, String value) {
        return new FieldMetadata().field(field).operator("contains").valueString(value);
    }

    public static FieldMetadata startsWithMetadata(Readable field, String value) {
        return new FieldMetadata().field(field).operator("starts with").valueString(value);
    }

    public static FieldMetadata endsWithMetadata(Readable field, String value) {
        return new FieldMetadata().field(field).operator("ends with").valueString(value);
    }

    // boolean

    public static FieldMetadata notMetadata(Readable field) {
        return new FieldMetadata().field(field).operator("not");
    }

    public static FieldMetadata andMetadata(Readable field, boolean value) {
        return new FieldMetadata().field(field).operator("and").valueObject(value);
    }

    public static FieldMetadata andMetadata(Readable field, Readable value) {
        return new FieldMetadata().field(field).operator("and").valueReadable(value);
    }

    public static FieldMetadata orMetadata(Readable field, boolean value) {
        return new FieldMetadata().field(field).operator("or").valueObject(value);
    }

    public static FieldMetadata orMetadata(Readable field, Readable value) {
        return new FieldMetadata().field(field).operator("or").valueReadable(value);
    }

    public static FieldMetadata xorMetadata(Readable field, boolean value) {
        return new FieldMetadata().field(field).operator("xor").valueObject(value);
    }

    public static FieldMetadata xorMetadata(Readable field, Readable value) {
        return new FieldMetadata().field(field).operator("xor").valueReadable(value);
    }

    // is

    public static FieldMetadata isMetadata(Readable field, boolean value) {
        return new FieldMetadata().field(field).operator("is").valueObject(value);
    }

    // lesser

    public static FieldMetadata lesserThanMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator("lesser than").valueObject(value);
    }

    public static FieldMetadata lesserThanMetadata(Readable field1, Readable field2) {
        return new FieldMetadata().field(field1).operator("lesser than").valueReadable(field2);
    }

    public static FieldMetadata lesserOrEqualsMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator("lesser or equals").valueObject(value);
    }

    public static FieldMetadata lesserOrEqualsMetadata(Readable field1, Readable field2) {
        return new FieldMetadata().field(field1).operator("lesser or equals").valueReadable(field2);
    }

    // lesser

    public static FieldMetadata greaterThanMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator("greater than").valueObject(value);
    }

    public static FieldMetadata greaterThanMetadata(Readable field1, Readable field2) {
        return new FieldMetadata().field(field1).operator("greater than").valueReadable(field2);
    }

    public static FieldMetadata greaterOrEqualsMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator("greater or equals").valueObject(value);
    }

    public static FieldMetadata greaterOrEqualsMetadata(Readable field1, Readable field2) {
        return new FieldMetadata().field(field1).operator("greater or equals").valueReadable(field2);
    }

    // length is

    public static FieldMetadata lengthIsMetadata(Readable field) {
        return new FieldMetadata().field(field).operator("length is");
    }

    // length is

    public static FieldMetadata containsMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator("contains").valueObject(value);
    }

    public static FieldMetadata containsMetadata(Readable field, Collection<Object> values) {
        return new FieldMetadata().field(field).operator("contains").valueListObject(values);
    }

    // empty

    public static FieldMetadata isEmptyMetadata(Readable field) {
        return new FieldMetadata().field(field).operator("is empty");
    }

    public static FieldMetadata isNotEmptyMetadata(Readable field) {
        return new FieldMetadata().field(field).operator("is not empty");
    }

    // size

    public static FieldMetadata hasSizeMetadata(Readable field, int size) {
        return new FieldMetadata().field(field).operator("has size").valueObject(size);
    }

    public static FieldMetadata hasNotSizeMetadata(Readable field, int size) {
        return new FieldMetadata().field(field).operator("has not size").valueObject(size);
    }

    // utils

    private static String formatListReadable(Collection<? extends Readable> readables) {
        return readables.stream().map(Readable::readable).collect(COLLECTOR_LIST);
    }

    private static String formatListObject(Collection<?> readables) {
        return readables.stream().map(Object::toString).collect(COLLECTOR_LIST);
    }

    // classes

    public static class Element {

        private final Readable readable;
        private final Type type;

        private Element(Readable readable, Type type) {
            this.readable = readable;
            this.type = type;
        }

        public Readable getReadable() {
            return readable;
        }

        public Type getType() {
            return type;
        }

    }

    public enum Type {

        field, operator, value

    }

}
