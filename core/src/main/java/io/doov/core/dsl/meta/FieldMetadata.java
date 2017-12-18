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

import static io.doov.core.dsl.meta.FieldMetadata.Type.field;
import static io.doov.core.dsl.meta.FieldMetadata.Type.operator;
import static io.doov.core.dsl.meta.FieldMetadata.Type.unknown;
import static io.doov.core.dsl.meta.FieldMetadata.Type.value;
import static java.util.stream.Collectors.joining;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.ast.AstVisitorUtils;

public class FieldMetadata implements Metadata {

    private static final Collector<CharSequence, ?, String> COLLECTOR_LIST = joining(", ", " : ", "");

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

    protected FieldMetadata elements(FieldMetadata readable) {
        if (readable != null) {
            elements.addAll(readable.elements);
        }
        return this;
    }

    private FieldMetadata add(Element element) {
        if (element != null) {
            elements.add(element);
        }
        return this;
    }

    // field

    public FieldMetadata field(Readable readable) {
        return add(readable == null ? null : new Element(readable, field));
    }

    // operator

    public FieldMetadata operator(Operator op) {
        return add(op == null ? null : new Element(op, operator));
    }

    // value

    public FieldMetadata valueObject(Object readable) {
        return add(readable == null ? null : new Element(() -> String.valueOf(readable), value));
    }

    public FieldMetadata valueString(String readable) {
        return add(readable == null ? null : new Element(() -> readable, value));
    }

    public FieldMetadata valueReadable(Readable readable) {
        return add(readable == null ? null : new Element(readable, value));
    }

    public FieldMetadata valueSupplier(Supplier<?> readable) {
        return add(readable == null ? null : new Element(() -> String.valueOf(readable.get()), value));
    }

    public FieldMetadata valueUnknown(String readable) {
        return add(readable == null ? null : new Element(() -> "UNKNOWN " + readable, unknown));
    }

    public FieldMetadata valueListReadable(Collection<? extends Readable> readables) {
        return add(readables == null || readables.isEmpty() ? null
                        : new Element(() -> formatListReadable(readables), value));
    }

    public FieldMetadata valueListObject(Collection<?> readables) {
        return add(readables == null || readables.isEmpty() ? null
                        : new Element(() -> formatListObject(readables), value));
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

    public static FieldMetadata fieldMetadata(Readable field) {
        return new FieldMetadata().field(field);
    }

    public static FieldMetadata unknownMetadata(String value) {
        return new FieldMetadata().valueUnknown(value);
    }

    // boolean

    public static FieldMetadata trueMetadata() {
        return new FieldMetadata().operator(DefaultOperator.always_true);
    }

    public static FieldMetadata falseMetadata() {
        return new FieldMetadata().operator(DefaultOperator.always_false);
    }

    // min

    public static FieldMetadata minMetadata(Collection<? extends Readable> values) {
        return new FieldMetadata().operator(DefaultOperator.min).valueListReadable(values);
    }

    // sum

    public static FieldMetadata sumMetadata(Collection<? extends Readable> values) {
        return new FieldMetadata().operator(DefaultOperator.sum).valueListReadable(values);
    }

    // times

    public static FieldMetadata timesMetadata(Readable field, int multiplier) {
        return new FieldMetadata().field(field).operator(DefaultOperator.times).valueObject(multiplier);
    }

    // when

    public static FieldMetadata whenMetadata(Readable field, Readable condition) {
        return new FieldMetadata().field(field).operator(DefaultOperator.when).valueReadable(condition);
    }

    // equals

    public static FieldMetadata equalsMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.equals).valueObject(value);
    }

    public static FieldMetadata equalsMetadata(Readable field, Readable value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.equals).valueReadable(value);
    }

    public static FieldMetadata notEqualsMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.not_equals).valueObject(value);
    }

    public static FieldMetadata notEqualsMetadata(Readable field, Readable value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.not_equals).valueReadable(value);
    }

    // null

    public static FieldMetadata nullMetadata(Readable field) {
        return new FieldMetadata().field(field).operator(DefaultOperator.is_null);
    }

    public static FieldMetadata notNullMetadata(Readable field) {
        return new FieldMetadata().field(field).operator(DefaultOperator.is_not_null);
    }

    // match

    public static FieldMetadata matchAnyMetadata(Readable field) {
        return new FieldMetadata().field(field).operator(DefaultOperator.match_any).valueUnknown("lambda");
    }

    public static FieldMetadata matchAnyMetadata(Readable field, Collection<?> values) {
        return new FieldMetadata().field(field).operator(DefaultOperator.match_any).valueListObject(values);
    }

    public static FieldMetadata matchAllMetadata(Readable field) {
        return new FieldMetadata().field(field).operator(DefaultOperator.match_all).valueUnknown("lambda");
    }

    public static FieldMetadata matchAllMetadata(Readable field, Collection<?> values) {
        return new FieldMetadata().field(field).operator(DefaultOperator.match_all).valueListObject(values);
    }

    public static FieldMetadata matchNoneMetadata(Readable field) {
        return new FieldMetadata().field(field).operator(DefaultOperator.match_none).valueUnknown("lamdba");
    }

    public static FieldMetadata matchNoneMetadata(Readable field, Collection<?> values) {
        return new FieldMetadata().field(field).operator(DefaultOperator.match_none).valueListObject(values);
    }

    // map

    public static FieldMetadata mapToIntMetadata(Readable field) {
        return new FieldMetadata().field(field).operator(DefaultOperator.as_a_number);
    }

    // with

    public static FieldMetadata withMetadata(Readable field, Readable adjuster) {
        return new FieldMetadata().field(field).operator(DefaultOperator.with).valueReadable(adjuster);
    }

    // minus

    public static FieldMetadata minusMetadata(Readable field, int value, Object unit) {
        return new FieldMetadata().field(field).operator(DefaultOperator.minus).valueString(value + " " + formatUnit(unit));
    }

    public static FieldMetadata minusMetadata(Readable field1, Readable field2, Object unit) {
        return new FieldMetadata().field(field1).operator(DefaultOperator.minus).field(field2).valueObject(formatUnit(unit));
    }

    // plus

    public static FieldMetadata plusMetadata(Readable field, int value, Object unit) {
        return new FieldMetadata().field(field).operator(DefaultOperator.plus).valueString(value + " " + formatUnit(unit));
    }

    public static FieldMetadata plusMetadata(Readable field1, Readable field2, Object unit) {
        return new FieldMetadata().field(field1).operator(DefaultOperator.plus).field(field2).valueObject(formatUnit(unit));
    }

    // after

    public static FieldMetadata afterMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.after).valueObject(value);
    }

    public static FieldMetadata afterMetadata(Readable field1, Readable field2) {
        return new FieldMetadata().field(field1).operator(DefaultOperator.after).valueReadable(field2);
    }

    public static FieldMetadata afterMetadata(Readable field, Supplier<?> value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.after).valueSupplier(value);
    }

    // before

    public static FieldMetadata beforeMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.before).valueObject(value);
    }

    public static FieldMetadata beforeMetadata(Readable field1, Readable field2) {
        return new FieldMetadata().field(field1).operator(DefaultOperator.before).valueReadable(field2);
    }

    public static FieldMetadata beforeMetadata(Readable field, Supplier<?> value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.before).valueSupplier(value);
    }

    // age at

    public static FieldMetadata ageAtMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.age_at).valueObject(value);
    }

    public static FieldMetadata ageAtMetadata(Readable field1, Readable field2) {
        return new FieldMetadata().field(field1).operator(DefaultOperator.age_at).valueReadable(field2);
    }

    public static FieldMetadata ageAtMetadata(Readable field, Supplier<?> supplier) {
        return new FieldMetadata().field(field).operator(DefaultOperator.age_at).valueSupplier(supplier);
    }

    // string

    public static FieldMetadata matchesMetadata(Readable field, String value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.matches).valueString(value);
    }

    public static FieldMetadata containsMetadata(Readable field, String value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.contains).valueString(value);
    }

    public static FieldMetadata startsWithMetadata(Readable field, String value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.starts_with).valueString(value);
    }

    public static FieldMetadata endsWithMetadata(Readable field, String value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.ends_with).valueString(value);
    }

    // boolean

    public static FieldMetadata notMetadata(Readable field) {
        return new FieldMetadata().field(field).operator(DefaultOperator.not);
    }

    public static FieldMetadata andMetadata(Readable field, boolean value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.and).valueObject(value);
    }

    public static FieldMetadata andMetadata(Readable field, Readable value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.and).valueReadable(value);
    }

    public static FieldMetadata orMetadata(Readable field, boolean value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.or).valueObject(value);
    }

    public static FieldMetadata orMetadata(Readable field, Readable value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.or).valueReadable(value);
    }

    public static FieldMetadata xorMetadata(Readable field, boolean value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.xor).valueObject(value);
    }

    public static FieldMetadata xorMetadata(Readable field, Readable value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.xor).valueReadable(value);
    }

    // is

    public static FieldMetadata isMetadata(Readable field, boolean value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.is).valueObject(value);
    }

    // lesser

    public static FieldMetadata lesserThanMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.lesser_than).valueObject(value);
    }

    public static FieldMetadata lesserThanMetadata(Readable field1, Readable field2) {
        return new FieldMetadata().field(field1).operator(DefaultOperator.lesser_than).valueReadable(field2);
    }

    public static FieldMetadata lesserOrEqualsMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.lesser_or_equals).valueObject(value);
    }

    public static FieldMetadata lesserOrEqualsMetadata(Readable field1, Readable field2) {
        return new FieldMetadata().field(field1).operator(DefaultOperator.lesser_or_equals).valueReadable(field2);
    }

    // lesser

    public static FieldMetadata greaterThanMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.greater_than).valueObject(value);
    }

    public static FieldMetadata greaterThanMetadata(Readable field1, Readable field2) {
        return new FieldMetadata().field(field1).operator(DefaultOperator.greater_than).valueReadable(field2);
    }

    public static FieldMetadata greaterOrEqualsMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.greater_or_equals).valueObject(value);
    }

    public static FieldMetadata greaterOrEqualsMetadata(Readable field1, Readable field2) {
        return new FieldMetadata().field(field1).operator(DefaultOperator.greater_or_equals).valueReadable(field2);
    }

    // length is

    public static FieldMetadata lengthIsMetadata(Readable field) {
        return new FieldMetadata().field(field).operator(DefaultOperator.length_is);
    }

    // length is

    public static FieldMetadata containsMetadata(Readable field, Object value) {
        return new FieldMetadata().field(field).operator(DefaultOperator.contains).valueObject(value);
    }

    public static FieldMetadata containsMetadata(Readable field, Collection<Object> values) {
        return new FieldMetadata().field(field).operator(DefaultOperator.contains).valueListObject(values);
    }

    // empty

    public static FieldMetadata isEmptyMetadata(Readable field) {
        return new FieldMetadata().field(field).operator(DefaultOperator.is_empty);
    }

    public static FieldMetadata isNotEmptyMetadata(Readable field) {
        return new FieldMetadata().field(field).operator(DefaultOperator.is_not_empty);
    }

    // size

    public static FieldMetadata hasSizeMetadata(Readable field, int size) {
        return new FieldMetadata().field(field).operator(DefaultOperator.has_size).valueObject(size);
    }

    public static FieldMetadata hasNotSizeMetadata(Readable field, int size) {
        return new FieldMetadata().field(field).operator(DefaultOperator.has_not_size).valueObject(size);
    }

    // local date suppliers

    public static FieldMetadata todayMetadata() {
        return new FieldMetadata().valueString("today");
    }

    public static FieldMetadata todayPlusMetadata(int value, Object unit) {
        return new FieldMetadata().valueString("today plus " + value + " " + formatUnit(unit));
    }

    public static FieldMetadata todayMinusMetadata(int value, Object unit) {
        return new FieldMetadata().valueString("today minus " + value + " " + formatUnit(unit));
    }

    public static FieldMetadata firstDayOfThisMonthMetadata() {
        return new FieldMetadata().valueString("first day of this month");
    }

    public static FieldMetadata firstDayOfThisYearMetadata() {
        return new FieldMetadata().valueString("first day of this year");
    }

    public static FieldMetadata lastDayOfThisMonthMetadata() {
        return new FieldMetadata().valueString("last day of this month");
    }

    public static FieldMetadata lastDayOfThisYearMetadata() {
        return new FieldMetadata().valueString("last day of this year");
    }

    public static FieldMetadata dateMetadata(Object date) {
        return new FieldMetadata().valueString(date.toString());
    }

    // temporal adjusters

    public static FieldMetadata firstDayOfMonthMetadata() {
        return new FieldMetadata().valueString("first day of month");
    }

    public static FieldMetadata firstDayOfNextMonthMetadata() {
        return new FieldMetadata().valueString("first day of next month");
    }

    public static FieldMetadata firstDayOfYearMetadata() {
        return new FieldMetadata().valueString("first day of year");
    }

    public static FieldMetadata firstDayOfNextYearMetadata() {
        return new FieldMetadata().valueString("first day of next year");
    }

    public static FieldMetadata lastDayOfMonthMetadata() {
        return new FieldMetadata().valueString("last day of month");
    }

    public static FieldMetadata lastDayOfYearMetadata() {
        return new FieldMetadata().valueString("last day of year");
    }

    // utils

    private static String formatUnit(Object unit) {
        return unit.toString().toLowerCase();
    }

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

        field, operator, value, unknown

    }

}
