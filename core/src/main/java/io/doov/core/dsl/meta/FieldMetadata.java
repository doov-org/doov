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

import static io.doov.core.dsl.meta.DefaultOperator.after;
import static io.doov.core.dsl.meta.DefaultOperator.age_at;
import static io.doov.core.dsl.meta.DefaultOperator.always_false;
import static io.doov.core.dsl.meta.DefaultOperator.always_true;
import static io.doov.core.dsl.meta.DefaultOperator.and;
import static io.doov.core.dsl.meta.DefaultOperator.as_a_number;
import static io.doov.core.dsl.meta.DefaultOperator.before;
import static io.doov.core.dsl.meta.DefaultOperator.contains;
import static io.doov.core.dsl.meta.DefaultOperator.ends_with;
import static io.doov.core.dsl.meta.DefaultOperator.equals;
import static io.doov.core.dsl.meta.DefaultOperator.greater_or_equals;
import static io.doov.core.dsl.meta.DefaultOperator.greater_than;
import static io.doov.core.dsl.meta.DefaultOperator.has_not_size;
import static io.doov.core.dsl.meta.DefaultOperator.has_size;
import static io.doov.core.dsl.meta.DefaultOperator.is;
import static io.doov.core.dsl.meta.DefaultOperator.is_empty;
import static io.doov.core.dsl.meta.DefaultOperator.is_not_empty;
import static io.doov.core.dsl.meta.DefaultOperator.is_not_null;
import static io.doov.core.dsl.meta.DefaultOperator.is_null;
import static io.doov.core.dsl.meta.DefaultOperator.length_is;
import static io.doov.core.dsl.meta.DefaultOperator.lesser_or_equals;
import static io.doov.core.dsl.meta.DefaultOperator.lesser_than;
import static io.doov.core.dsl.meta.DefaultOperator.match_all;
import static io.doov.core.dsl.meta.DefaultOperator.match_any;
import static io.doov.core.dsl.meta.DefaultOperator.match_none;
import static io.doov.core.dsl.meta.DefaultOperator.matches;
import static io.doov.core.dsl.meta.DefaultOperator.min;
import static io.doov.core.dsl.meta.DefaultOperator.minus;
import static io.doov.core.dsl.meta.DefaultOperator.not;
import static io.doov.core.dsl.meta.DefaultOperator.not_equals;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.core.dsl.meta.DefaultOperator.plus;
import static io.doov.core.dsl.meta.DefaultOperator.starts_with;
import static io.doov.core.dsl.meta.DefaultOperator.sum;
import static io.doov.core.dsl.meta.DefaultOperator.times;
import static io.doov.core.dsl.meta.DefaultOperator.when;
import static io.doov.core.dsl.meta.DefaultOperator.with;
import static io.doov.core.dsl.meta.DefaultOperator.xor;
import static io.doov.core.dsl.meta.ElementType.FIELD;
import static io.doov.core.dsl.meta.ElementType.OPERATOR;
import static io.doov.core.dsl.meta.ElementType.UNKNOWN;
import static io.doov.core.dsl.meta.ElementType.VALUE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static java.util.stream.Collectors.joining;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.ast.AstVisitorUtils;

public class FieldMetadata extends PredicateMetadata {

    private static final Collector<CharSequence, ?, String> COLLECTOR_LIST = joining(", ", " : ", "");
    private final Deque<Element> elements;

    public FieldMetadata() {
        elements = new ArrayDeque<>();
    }

    public FieldMetadata(FieldMetadata metadata) {
        elements = new ArrayDeque<>(metadata.elements);
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

    public FieldMetadata field(DslField readable) {
        return add(readable == null ? null : new Element(readable, FIELD));
    }

    // operator

    public FieldMetadata operator(Operator op) {
        return add(op == null ? null : new Element(op, OPERATOR));
    }

    // value

    public FieldMetadata valueObject(Object readable) {
        return add(readable == null ? null : new Element(() -> String.valueOf(readable), VALUE));
    }

    public FieldMetadata valueString(String readable) {
        return add(readable == null ? null : new Element(() -> readable, VALUE));
    }

    public FieldMetadata valueReadable(Readable readable) {
        return add(readable == null ? null : new Element(readable, VALUE));
    }

    public FieldMetadata valueSupplier(Supplier<?> readable) {
        return add(readable == null ? null : new Element(() -> String.valueOf(readable.get()), VALUE));
    }

    public FieldMetadata valueUnknown(String readable) {
        return add(readable == null ? null : new Element(() -> "UNKNOWN " + readable, UNKNOWN));
    }

    public FieldMetadata valueListReadable(Collection<? extends Readable> readables) {
        return add(readables == null || readables.isEmpty() ? null
                        : new Element(() -> formatListReadable(readables), VALUE));
    }

    public FieldMetadata valueListObject(Collection<?> readables) {
        return add(readables == null || readables.isEmpty() ? null
                        : new Element(() -> formatListObject(readables), VALUE));
    }

    // implementation

    @Override
    public PredicateMetadata merge(FieldMetadata other) {
        removeDuplicateField(other);
        FieldMetadata fieldMetadata = new FieldMetadata(this);
        fieldMetadata.elements.addAll(other.elements);
        return fieldMetadata;
    }

    private void removeDuplicateField(FieldMetadata other) {
        if (this.elements.isEmpty() || other.elements.isEmpty()) {
            return;
        }
        if (this.elements.peek().getType().equals(ElementType.FIELD)
                        && other.elements.peek().getType().equals(ElementType.FIELD)) {
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

    public static FieldMetadata fieldMetadata(DslField field) {
        return new FieldMetadata().field(field);
    }

    public static FieldMetadata unknownMetadata(String value) {
        return new FieldMetadata().valueUnknown(value);
    }

    // boolean

    public static FieldMetadata trueMetadata() {
        return new FieldMetadata().operator(always_true);
    }

    public static FieldMetadata falseMetadata() {
        return new FieldMetadata().operator(always_false);
    }

    // min

    public static FieldMetadata minMetadata(Collection<? extends Readable> values) {
        return new FieldMetadata().operator(min).valueListReadable(values);
    }

    // sum

    public static FieldMetadata sumMetadata(Collection<? extends Readable> values) {
        return new FieldMetadata().operator(sum).valueListReadable(values);
    }

    // times

    public static FieldMetadata timesMetadata(DslField field, int multiplier) {
        return new FieldMetadata().field(field).operator(times).valueObject(multiplier);
    }

    // when

    public static FieldMetadata whenMetadata(DslField field, Readable condition) {
        return new FieldMetadata().field(field).operator(when).valueReadable(condition);
    }

    // equals

    public static FieldMetadata equalsMetadata(DslField field, Object value) {
        return new FieldMetadata().field(field).operator(equals).valueObject(value);
    }

    public static FieldMetadata equalsMetadata(DslField field, Readable value) {
        return new FieldMetadata().field(field).operator(equals).valueReadable(value);
    }

    public static FieldMetadata notEqualsMetadata(DslField field, Object value) {
        return new FieldMetadata().field(field).operator(not_equals).valueObject(value);
    }

    public static FieldMetadata notEqualsMetadata(DslField field, Readable value) {
        return new FieldMetadata().field(field).operator(not_equals).valueReadable(value);
    }

    // null

    public static FieldMetadata nullMetadata(DslField field) {
        return new FieldMetadata().field(field).operator(is_null);
    }

    public static FieldMetadata notNullMetadata(DslField field) {
        return new FieldMetadata().field(field).operator(is_not_null);
    }

    // match

    public static FieldMetadata matchAnyMetadata(DslField field) {
        return new FieldMetadata().field(field).operator(match_any).valueUnknown("lambda");
    }

    public static FieldMetadata matchAnyMetadata(DslField field, Collection<?> values) {
        return new FieldMetadata().field(field).operator(match_any).valueListObject(values);
    }

    public static FieldMetadata matchAllMetadata(DslField field) {
        return new FieldMetadata().field(field).operator(match_all).valueUnknown("lambda");
    }

    public static FieldMetadata matchAllMetadata(DslField field, Collection<?> values) {
        return new FieldMetadata().field(field).operator(match_all).valueListObject(values);
    }

    public static FieldMetadata matchNoneMetadata(DslField field) {
        return new FieldMetadata().field(field).operator(match_none).valueUnknown("lamdba");
    }

    public static FieldMetadata matchNoneMetadata(DslField field, Collection<?> values) {
        return new FieldMetadata().field(field).operator(match_none).valueListObject(values);
    }

    // map

    public static FieldMetadata mapToIntMetadata(DslField field) {
        return new FieldMetadata().field(field).operator(as_a_number);
    }

    // with

    public static FieldMetadata withMetadata(DslField field, Readable adjuster) {
        return new FieldMetadata().field(field).operator(with).valueReadable(adjuster);
    }

    // minus

    public static FieldMetadata minusMetadata(DslField field, int value, Object unit) {
        return new FieldMetadata().field(field).operator(minus)
                        .valueString(value + " " + formatUnit(unit));
    }

    public static FieldMetadata minusMetadata(DslField field1, DslField field2, Object unit) {
        return new FieldMetadata().field(field1).operator(minus).field(field2)
                        .valueObject(formatUnit(unit));
    }

    // plus

    public static FieldMetadata plusMetadata(DslField field, int value, Object unit) {
        return new FieldMetadata().field(field).operator(plus)
                        .valueString(value + " " + formatUnit(unit));
    }

    public static FieldMetadata plusMetadata(DslField field1, DslField field2, Object unit) {
        return new FieldMetadata().field(field1).operator(plus).field(field2)
                        .valueObject(formatUnit(unit));
    }

    // after

    public static FieldMetadata afterMetadata(DslField field, Object value) {
        return new FieldMetadata().field(field).operator(after).valueObject(value);
    }

    public static FieldMetadata afterMetadata(DslField field1, Readable field2) {
        return new FieldMetadata().field(field1).operator(after).valueReadable(field2);
    }

    public static FieldMetadata afterMetadata(DslField field, Supplier<?> value) {
        return new FieldMetadata().field(field).operator(after).valueSupplier(value);
    }

    // before

    public static FieldMetadata beforeMetadata(DslField field, Object value) {
        return new FieldMetadata().field(field).operator(before).valueObject(value);
    }

    public static FieldMetadata beforeMetadata(DslField field1, Readable field2) {
        return new FieldMetadata().field(field1).operator(before).valueReadable(field2);
    }

    public static FieldMetadata beforeMetadata(DslField field, Supplier<?> value) {
        return new FieldMetadata().field(field).operator(before).valueSupplier(value);
    }

    // age at

    public static FieldMetadata ageAtMetadata(DslField field, Object value) {
        return new FieldMetadata().field(field).operator(age_at).valueObject(value);
    }

    public static FieldMetadata ageAtMetadata(DslField field1, Readable field2) {
        return new FieldMetadata().field(field1).operator(age_at).valueReadable(field2);
    }

    public static FieldMetadata ageAtMetadata(DslField field, Supplier<?> supplier) {
        return new FieldMetadata().field(field).operator(age_at).valueSupplier(supplier);
    }

    // string

    public static FieldMetadata matchesMetadata(DslField field, String value) {
        return new FieldMetadata().field(field).operator(matches).valueString(value);
    }

    public static FieldMetadata containsMetadata(DslField field, String value) {
        return new FieldMetadata().field(field).operator(contains).valueString(value);
    }

    public static FieldMetadata startsWithMetadata(DslField field, String value) {
        return new FieldMetadata().field(field).operator(starts_with).valueString(value);
    }

    public static FieldMetadata endsWithMetadata(DslField field, String value) {
        return new FieldMetadata().field(field).operator(ends_with).valueString(value);
    }

    // boolean

    public static FieldMetadata notMetadata(DslField field) {
        return new FieldMetadata().field(field).operator(not);
    }

    public static FieldMetadata andMetadata(DslField field, boolean value) {
        return new FieldMetadata().field(field).operator(and).valueObject(value);
    }

    public static FieldMetadata andMetadata(DslField field, Readable value) {
        return new FieldMetadata().field(field).operator(and).valueReadable(value);
    }

    public static FieldMetadata orMetadata(DslField field, boolean value) {
        return new FieldMetadata().field(field).operator(or).valueObject(value);
    }

    public static FieldMetadata orMetadata(DslField field, Readable value) {
        return new FieldMetadata().field(field).operator(or).valueReadable(value);
    }

    public static FieldMetadata xorMetadata(DslField field, boolean value) {
        return new FieldMetadata().field(field).operator(xor).valueObject(value);
    }

    public static FieldMetadata xorMetadata(DslField field, Readable value) {
        return new FieldMetadata().field(field).operator(xor).valueReadable(value);
    }

    // is

    public static FieldMetadata isMetadata(DslField field, boolean value) {
        return new FieldMetadata().field(field).operator(is).valueObject(value);
    }

    // lesser

    public static FieldMetadata lesserThanMetadata(DslField field, Object value) {
        return new FieldMetadata().field(field).operator(lesser_than).valueObject(value);
    }

    public static FieldMetadata lesserThanMetadata(DslField field1, Readable field2) {
        return new FieldMetadata().field(field1).operator(lesser_than).valueReadable(field2);
    }

    public static FieldMetadata lesserOrEqualsMetadata(DslField field, Object value) {
        return new FieldMetadata().field(field).operator(lesser_or_equals).valueObject(value);
    }

    public static FieldMetadata lesserOrEqualsMetadata(DslField field1, Readable field2) {
        return new FieldMetadata().field(field1).operator(lesser_or_equals).valueReadable(field2);
    }

    // lesser

    public static FieldMetadata greaterThanMetadata(DslField field, Object value) {
        return new FieldMetadata().field(field).operator(greater_than).valueObject(value);
    }

    public static FieldMetadata greaterThanMetadata(DslField field1, Readable field2) {
        return new FieldMetadata().field(field1).operator(greater_than).valueReadable(field2);
    }

    public static FieldMetadata greaterOrEqualsMetadata(DslField field, Object value) {
        return new FieldMetadata().field(field).operator(greater_or_equals).valueObject(value);
    }

    public static FieldMetadata greaterOrEqualsMetadata(DslField field1, Readable field2) {
        return new FieldMetadata().field(field1).operator(greater_or_equals).valueReadable(field2);
    }

    // length is

    public static FieldMetadata lengthIsMetadata(DslField field) {
        return new FieldMetadata().field(field).operator(length_is);
    }

    // length is

    public static FieldMetadata containsMetadata(DslField field, Object value) {
        return new FieldMetadata().field(field).operator(contains).valueObject(value);
    }

    public static FieldMetadata containsMetadata(DslField field, Collection<Object> values) {
        return new FieldMetadata().field(field).operator(contains).valueListObject(values);
    }

    // empty

    public static FieldMetadata isEmptyMetadata(DslField field) {
        return new FieldMetadata().field(field).operator(is_empty);
    }

    public static FieldMetadata isNotEmptyMetadata(DslField field) {
        return new FieldMetadata().field(field).operator(is_not_empty);
    }

    // size

    public static FieldMetadata hasSizeMetadata(DslField field, int size) {
        return new FieldMetadata().field(field).operator(has_size).valueObject(size);
    }

    public static FieldMetadata hasNotSizeMetadata(DslField field, int size) {
        return new FieldMetadata().field(field).operator(has_not_size).valueObject(size);
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

    @Override
    public List<Metadata> childs() {
        return Collections.emptyList();
    }

    @Override
    public MetadataType type() {
        return FIELD_PREDICATE;
    }

    @Override
    public Metadata message(Context context) {
        return this;
    }

}
