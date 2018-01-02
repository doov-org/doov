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

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.ElementType.FIELD;
import static io.doov.core.dsl.meta.ElementType.OPERATOR;
import static io.doov.core.dsl.meta.ElementType.UNKNOWN;
import static io.doov.core.dsl.meta.ElementType.VALUE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE_MATCH_ANY;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;
import static java.util.stream.Collectors.joining;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.ast.AstVisitorUtils;

public class LeafMetadata extends PredicateMetadata {
    private static final Collector<CharSequence, ?, String> COLLECTOR_LIST = joining(", ", " : ", "");
    private final Deque<Element> elements;
    private final MetadataType type;

    public LeafMetadata(MetadataType type) {
        this.elements = new ArrayDeque<>();
        this.type = type;
    }

    private LeafMetadata(Deque<Element> elements, MetadataType type) {
        this.elements = elements;
        this.type = type;
    }

    public Stream<Element> stream() {
        return elements.stream();
    }

    @Override
    public PredicateMetadata merge(LeafMetadata other) {
        removeDuplicateField(elements, other.elements);
        final Deque<Element> merge = new ArrayDeque<>(elements);
        merge.addAll(other.elements);
        return new LeafMetadata(merge, mergeType(type, other.type));
    }

    private static MetadataType mergeType(MetadataType current, MetadataType merged) {
        if (current == FIELD_PREDICATE && merged == FIELD_PREDICATE_MATCH_ANY)
            return FIELD_PREDICATE_MATCH_ANY;
        return current;
    }

    private static void removeDuplicateField(Deque<Element> current, Deque<Element> merged) {
        if (current.isEmpty() || merged.isEmpty()) {
            return;
        }
        if (current.peek().getType().equals(ElementType.FIELD)
                        && merged.peek().getType().equals(ElementType.FIELD)) {
            merged.pop();
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

    @Override
    public List<Metadata> children() {
        return Collections.emptyList();
    }

    @Override
    public MetadataType type() {
        return type;
    }

    @Override
    public Metadata message(Context context) {
        if (type == FIELD_PREDICATE_MATCH_ANY) {
            final DslField field = (DslField) elements.getFirst().getReadable();
            return new LeafMetadata(FIELD_PREDICATE).field(field).operator(equals)
                            .valueObject(context.getEvalValue(field.id()));
        }
        return this;
    }

    private LeafMetadata add(Element element) {
        if (element != null) {
            elements.add(element);
        }
        return this;
    }

    // field

    public LeafMetadata field(DslField readable) {
        return add(readable == null ? null : new Element(readable, FIELD));
    }

    // operator

    public LeafMetadata operator(Operator op) {
        return add(op == null ? null : new Element(op, OPERATOR));
    }

    // value

    public LeafMetadata valueObject(Object readable) {
        return add(readable == null ? null : new Element(() -> String.valueOf(readable), VALUE));
    }

    public LeafMetadata valueString(String readable) {
        return add(readable == null ? null : new Element(() -> readable, VALUE));
    }

    public LeafMetadata valueReadable(Readable readable) {
        return add(readable == null ? null : new Element(readable, VALUE));
    }

    public LeafMetadata valueSupplier(Supplier<?> readable) {
        return add(readable == null ? null : new Element(() -> String.valueOf(readable.get()), VALUE));
    }

    public LeafMetadata valueUnknown(String readable) {
        return add(readable == null ? null : new Element(() -> "-function- " + readable, UNKNOWN));
    }

    public LeafMetadata valueListReadable(Collection<? extends Readable> readables) {
        return add(readables == null || readables.isEmpty() ? null
                        : new Element(() -> formatListReadable(readables), VALUE));
    }

    public LeafMetadata valueListObject(Collection<?> readables) {
        return add(readables == null || readables.isEmpty() ? null
                        : new Element(() -> formatListObject(readables), VALUE));
    }

    public static LeafMetadata fieldMetadata(DslField field) {
        return new LeafMetadata(FIELD_PREDICATE).field(field);
    }

    public static LeafMetadata unknownMetadata(String value) {
        return new LeafMetadata(LEAF_PREDICATE).valueUnknown(value);
    }

    // boolean

    public static LeafMetadata trueMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).operator(always_true);
    }

    public static LeafMetadata falseMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).operator(always_false);
    }

    // min

    public static LeafMetadata minMetadata(Collection<? extends Readable> values) {
        return new LeafMetadata(LEAF_PREDICATE).operator(min).valueListReadable(values);
    }

    // sum

    public static LeafMetadata sumMetadata(Collection<? extends Readable> values) {
        return new LeafMetadata(LEAF_PREDICATE).operator(sum).valueListReadable(values);
    }

    // times

    public static LeafMetadata timesMetadata(DslField field, int multiplier) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(times).valueObject(multiplier);
    }

    // when

    public static LeafMetadata whenMetadata(DslField field, Readable condition) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(when).valueReadable(condition);
    }

    // equals

    public static LeafMetadata equalsMetadata(DslField field, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(equals).valueObject(value);
    }

    public static LeafMetadata equalsMetadata(DslField field, Readable value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(equals).valueReadable(value);
    }

    public static LeafMetadata notEqualsMetadata(DslField field, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(not_equals).valueObject(value);
    }

    public static LeafMetadata notEqualsMetadata(DslField field, Readable value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(not_equals).valueReadable(value);
    }

    // null

    public static LeafMetadata nullMetadata(DslField field) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(is_null);
    }

    public static LeafMetadata notNullMetadata(DslField field) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(is_not_null);
    }

    // match

    public static LeafMetadata matchAnyMetadata(DslField field) {
        return new LeafMetadata(FIELD_PREDICATE_MATCH_ANY).field(field).operator(match_any).valueUnknown("-function-");
    }

    public static LeafMetadata matchAnyMetadata(DslField field, Collection<?> values) {
        return new LeafMetadata(FIELD_PREDICATE_MATCH_ANY).field(field).operator(match_any).valueListObject(values);
    }

    public static LeafMetadata matchAllMetadata(DslField field) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(match_all).valueUnknown("-function-");
    }

    public static LeafMetadata matchAllMetadata(DslField field, Collection<?> values) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(match_all).valueListObject(values);
    }

    public static LeafMetadata matchNoneMetadata(DslField field) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(match_none).valueUnknown("-function-");
    }

    public static LeafMetadata matchNoneMetadata(DslField field, Collection<?> values) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(match_none).valueListObject(values);
    }

    // map

    public static LeafMetadata mapToIntMetadata(DslField field) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(as_a_number);
    }

    // with

    public static LeafMetadata withMetadata(DslField field, Readable adjuster) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(with).valueReadable(adjuster);
    }

    // minus

    public static LeafMetadata minusMetadata(DslField field, int value, Object unit) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(minus)
                        .valueString(value + " " + formatUnit(unit));
    }

    public static LeafMetadata minusMetadata(DslField field1, DslField field2, Object unit) {
        return new LeafMetadata(FIELD_PREDICATE).field(field1).operator(minus).field(field2)
                        .valueObject(formatUnit(unit));
    }

    // plus

    public static LeafMetadata plusMetadata(DslField field, int value, Object unit) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(plus)
                        .valueString(value + " " + formatUnit(unit));
    }

    public static LeafMetadata plusMetadata(DslField field1, DslField field2, Object unit) {
        return new LeafMetadata(FIELD_PREDICATE).field(field1).operator(plus).field(field2)
                        .valueObject(formatUnit(unit));
    }

    // after

    public static LeafMetadata afterMetadata(DslField field, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(after).valueObject(value);
    }

    public static LeafMetadata afterMetadata(DslField field1, Readable field2) {
        return new LeafMetadata(FIELD_PREDICATE).field(field1).operator(after).valueReadable(field2);
    }

    public static LeafMetadata afterMetadata(DslField field, Supplier<?> value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(after).valueSupplier(value);
    }

    // before

    public static LeafMetadata beforeMetadata(DslField field, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(before).valueObject(value);
    }

    public static LeafMetadata beforeMetadata(DslField field1, Readable field2) {
        return new LeafMetadata(FIELD_PREDICATE).field(field1).operator(before).valueReadable(field2);
    }

    public static LeafMetadata beforeMetadata(DslField field, Supplier<?> value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(before).valueSupplier(value);
    }

    // age at

    public static LeafMetadata ageAtMetadata(DslField field, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(age_at).valueObject(value);
    }

    public static LeafMetadata ageAtMetadata(DslField field1, Readable field2) {
        return new LeafMetadata(FIELD_PREDICATE).field(field1).operator(age_at).valueReadable(field2);
    }

    public static LeafMetadata ageAtMetadata(DslField field, Supplier<?> supplier) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(age_at).valueSupplier(supplier);
    }

    // string

    public static LeafMetadata matchesMetadata(DslField field, String value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(matches).valueString(value);
    }

    public static LeafMetadata containsMetadata(DslField field, String value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(contains).valueString(value);
    }

    public static LeafMetadata startsWithMetadata(DslField field, String value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(starts_with).valueString(value);
    }

    public static LeafMetadata endsWithMetadata(DslField field, String value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(ends_with).valueString(value);
    }

    // boolean

    public static LeafMetadata notMetadata(DslField field) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(not);
    }

    public static LeafMetadata andMetadata(DslField field, boolean value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(and).valueObject(value);
    }

    public static LeafMetadata andMetadata(DslField field, Readable value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(and).valueReadable(value);
    }

    public static LeafMetadata orMetadata(DslField field, boolean value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(or).valueObject(value);
    }

    public static LeafMetadata orMetadata(DslField field, Readable value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(or).valueReadable(value);
    }

    public static LeafMetadata xorMetadata(DslField field, boolean value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(xor).valueObject(value);
    }

    public static LeafMetadata xorMetadata(DslField field, Readable value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(xor).valueReadable(value);
    }

    // is

    public static LeafMetadata isMetadata(DslField field, boolean value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(is).valueObject(value);
    }

    // lesser

    public static LeafMetadata lesserThanMetadata(DslField field, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(lesser_than).valueObject(value);
    }

    public static LeafMetadata lesserThanMetadata(DslField field1, Readable field2) {
        return new LeafMetadata(FIELD_PREDICATE).field(field1).operator(lesser_than).valueReadable(field2);
    }

    public static LeafMetadata lesserOrEqualsMetadata(DslField field, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(lesser_or_equals).valueObject(value);
    }

    public static LeafMetadata lesserOrEqualsMetadata(DslField field1, Readable field2) {
        return new LeafMetadata(FIELD_PREDICATE).field(field1).operator(lesser_or_equals).valueReadable(field2);
    }

    // lesser

    public static LeafMetadata greaterThanMetadata(DslField field, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(greater_than).valueObject(value);
    }

    public static LeafMetadata greaterThanMetadata(DslField field1, Readable field2) {
        return new LeafMetadata(FIELD_PREDICATE).field(field1).operator(greater_than).valueReadable(field2);
    }

    public static LeafMetadata greaterOrEqualsMetadata(DslField field, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(greater_or_equals).valueObject(value);
    }

    public static LeafMetadata greaterOrEqualsMetadata(DslField field1, Readable field2) {
        return new LeafMetadata(FIELD_PREDICATE).field(field1).operator(greater_or_equals).valueReadable(field2);
    }

    // length is

    public static LeafMetadata lengthIsMetadata(DslField field) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(length_is);
    }

    // length is

    public static LeafMetadata containsMetadata(DslField field, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(contains).valueObject(value);
    }

    public static LeafMetadata containsMetadata(DslField field, Collection<Object> values) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(contains).valueListObject(values);
    }

    // empty

    public static LeafMetadata isEmptyMetadata(DslField field) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(is_empty);
    }

    public static LeafMetadata isNotEmptyMetadata(DslField field) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(is_not_empty);
    }

    // size

    public static LeafMetadata hasSizeMetadata(DslField field, int size) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(has_size).valueObject(size);
    }

    public static LeafMetadata hasNotSizeMetadata(DslField field, int size) {
        return new LeafMetadata(FIELD_PREDICATE).field(field).operator(has_not_size).valueObject(size);
    }

    // local date suppliers

    public static LeafMetadata todayMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).valueString("today");
    }

    public static LeafMetadata todayPlusMetadata(int value, Object unit) {
        return new LeafMetadata(LEAF_PREDICATE).valueString("today plus " + value + " " + formatUnit(unit));
    }

    public static LeafMetadata todayMinusMetadata(int value, Object unit) {
        return new LeafMetadata(LEAF_PREDICATE).valueString("today minus " + value + " " + formatUnit(unit));
    }

    public static LeafMetadata firstDayOfThisMonthMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).valueString("first day of this month");
    }

    public static LeafMetadata firstDayOfThisYearMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).valueString("first day of this year");
    }

    public static LeafMetadata lastDayOfThisMonthMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).valueString("last day of this month");
    }

    public static LeafMetadata lastDayOfThisYearMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).valueString("last day of this year");
    }

    public static LeafMetadata dateMetadata(Object date) {
        return new LeafMetadata(LEAF_PREDICATE).valueString(date.toString());
    }

    // temporal adjusters

    public static LeafMetadata firstDayOfMonthMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).valueString("first day of month");
    }

    public static LeafMetadata firstDayOfNextMonthMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).valueString("first day of next month");
    }

    public static LeafMetadata firstDayOfYearMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).valueString("first day of year");
    }

    public static LeafMetadata firstDayOfNextYearMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).valueString("first day of next year");
    }

    public static LeafMetadata lastDayOfMonthMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).valueString("last day of month");
    }

    public static LeafMetadata lastDayOfYearMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).valueString("last day of year");
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
}
