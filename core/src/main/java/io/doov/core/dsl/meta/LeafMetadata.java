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
import static io.doov.core.dsl.meta.Element.leftParenthesis;
import static io.doov.core.dsl.meta.Element.rightParenthesis;
import static io.doov.core.dsl.meta.ElementType.FIELD;
import static io.doov.core.dsl.meta.ElementType.OPERATOR;
import static io.doov.core.dsl.meta.ElementType.STRING_VALUE;
import static io.doov.core.dsl.meta.ElementType.TEMPORAL_UNIT;
import static io.doov.core.dsl.meta.ElementType.UNKNOWN;
import static io.doov.core.dsl.meta.ElementType.VALUE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE_MATCH_ANY;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.StepCondition;

public class LeafMetadata extends PredicateMetadata {
    private static final Collector<CharSequence, ?, String> COLLECTOR_LIST = joining(", ", " : ", "");
    private final Deque<Element> elements;
    private final MetadataType type;

    public LeafMetadata(MetadataType type) {
        this.elements = new ArrayDeque<>();
        this.type = type;
    }

    public LeafMetadata(PredicateMetadata metadata) {
        this(new ArrayDeque<>(metadata.flatten()), metadata.type());
    }

    public LeafMetadata(PredicateMetadata metadata, MetadataType type) {
        this(new ArrayDeque<>(metadata.flatten()), type);
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
        removeDuplicate(elements, other.elements);
        final Deque<Element> merge = new ArrayDeque<>(elements);
        merge.addAll(other.elements);
        return new LeafMetadata(merge, mergeType(type, other.type));
    }

    @Override
    public List<Element> flatten() {
        return elements.stream().collect(toList());
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
    public String readable(Locale locale) {
        return astToString(this, locale);
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        visitor.start(this, depth);
        visitor.visit(this, depth);
        visitor.end(this, depth);
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
            return new LeafMetadata(FIELD_PREDICATE).field(field).operator(not_equals)
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
        if (readable == null)
            return valueReadable(() -> "null");
        if (readable instanceof String)
            return valueString((String) readable);
        return valueReadable(() -> String.valueOf(readable));
    }

    public LeafMetadata valueString(String readable) {
        return add(readable == null ? null : new Element(() -> readable, STRING_VALUE));
    }

    public LeafMetadata temporalUnit(Object unit) {
        return add(unit == null ? null : new Element(() -> unit.toString().toLowerCase(), TEMPORAL_UNIT));
    }

    public LeafMetadata valueCondition(DefaultCondition<?> condition) {
        ((LeafMetadata) condition.getMetadata()).stream().forEach(e -> add(e));
        return this;
    }

    public LeafMetadata valueReadable(Readable readable) {
        return add(readable == null ? null : new Element(readable, VALUE));
    }

    public LeafMetadata valueTemporalAdjuster(Readable adjuster) {
        return add(((LeafMetadata) adjuster).elements.getFirst());
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

    public static LeafMetadata timesMetadata(PredicateMetadata metadata, int multiplier) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(times).valueObject(multiplier);
    }

    // when

    public static LeafMetadata whenMetadata(PredicateMetadata metadata, StepCondition condition) {
        final LeafMetadata exp = new LeafMetadata(metadata, FIELD_PREDICATE).operator(when);
        exp.elements.add(leftParenthesis());
        exp.elements.addAll(condition.getMetadata().flatten());
        exp.elements.add(rightParenthesis());
        return exp;
    }

    // equals

    public static LeafMetadata equalsMetadata(PredicateMetadata metadata, Object value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(equals).valueObject(value);
    }

    public static LeafMetadata equalsMetadata(PredicateMetadata metadata, Readable value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(equals).valueReadable(value);
    }

    public static LeafMetadata equalsMetadata(PredicateMetadata metadata, DefaultCondition<?> condition) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(equals).valueCondition(condition);
    }

    public static LeafMetadata notEqualsMetadata(PredicateMetadata metadata, Object value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(not_equals).valueObject(value);
    }

    public static LeafMetadata notEqualsMetadata(PredicateMetadata metadata, Readable value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(not_equals).valueReadable(value);
    }

    // null

    public static LeafMetadata nullMetadata(PredicateMetadata metadata) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(is_null);
    }

    public static LeafMetadata notNullMetadata(PredicateMetadata metadata) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(is_not_null);
    }

    // match

    public static LeafMetadata matchAnyMetadata(PredicateMetadata metadata) {
        return new LeafMetadata(metadata, FIELD_PREDICATE_MATCH_ANY).operator(match_any).valueUnknown("-function-");
    }

    public static LeafMetadata matchAnyMetadata(PredicateMetadata metadata, Collection<?> values) {
        return new LeafMetadata(metadata, FIELD_PREDICATE_MATCH_ANY).operator(match_any).valueListObject(values);
    }

    public static LeafMetadata matchAllMetadata(PredicateMetadata metadata) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(match_all).valueUnknown("-function-");
    }

    public static LeafMetadata matchAllMetadata(PredicateMetadata metadata, Collection<?> values) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(match_all).valueListObject(values);
    }

    public static LeafMetadata matchNoneMetadata(PredicateMetadata metadata) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(match_none).valueUnknown("-function-");
    }

    public static LeafMetadata matchNoneMetadata(PredicateMetadata metadata, Collection<?> values) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(match_none).valueListObject(values);
    }

    // map

    public static LeafMetadata mapToIntMetadata(PredicateMetadata metadata) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(as_a_number).valueUnknown("");
    }

    // with

    public static LeafMetadata withMetadata(PredicateMetadata metadata, Readable adjuster) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(with).valueTemporalAdjuster(adjuster);
    }

    // minus

    public static LeafMetadata minusMetadata(PredicateMetadata metadata, int value, Object unit) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(minus).valueObject(value).temporalUnit(unit);
    }

    public static LeafMetadata minusMetadata(PredicateMetadata metadata, DslField field2, Object unit) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(minus).field(field2).temporalUnit(unit);
    }

    // plus

    public static LeafMetadata plusMetadata(PredicateMetadata metadata, int value, Object unit) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(plus).valueObject(value).temporalUnit(unit);
    }

    public static LeafMetadata plusMetadata(PredicateMetadata metadata, DslField field2, Object unit) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(plus).field(field2).temporalUnit(unit);
    }

    // after

    public static LeafMetadata afterValueMetadata(DefaultCondition<?> condition, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after).valueObject(value);
    }

    public static LeafMetadata afterTemporalFieldMetadata(DefaultCondition<?> condition, DslField field) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after).field(field);
    }

    public static LeafMetadata afterTemporalConditionMetadata(DefaultCondition<?> c1, DefaultCondition<?> c2) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(c1).operator(after).valueCondition(c2);
    }

    public static LeafMetadata afterSupplierMetadata(DefaultCondition<?> condition, Supplier<?> value) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after).valueSupplier(value);
    }

    public static LeafMetadata afterOrEqualsValueMetadata(DefaultCondition<?> condition, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after_or_equals).valueObject(value);
    }

    public static LeafMetadata afterOrEqualsSupplierMetadata(DefaultCondition<?> condition, Supplier<?> value) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after_or_equals)
                .valueSupplier(value);
    }

    public static LeafMetadata afterOrEqualsTemporalConditionMetadata(DefaultCondition<?> c1, DefaultCondition<?> c2) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(c1).operator(after_or_equals).valueCondition(c2);
    }

    // before

    public static LeafMetadata beforeValueMetadata(DefaultCondition<?> condition, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before).valueObject(value);
    }

    public static LeafMetadata beforeTemporalFieldMetadata(DefaultCondition<?> condition, DslField field) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before).field(field);
    }

    public static LeafMetadata beforeTemporalConditionMetadata(DefaultCondition<?> c1, DefaultCondition<?> c2) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(c1).operator(before).valueCondition(c2);
    }

    public static LeafMetadata beforeSupplierMetadata(DefaultCondition<?> condition, Supplier<?> value) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before).valueSupplier(value);
    }

    public static LeafMetadata beforeOrEqualsValueMetadata(DefaultCondition<?> condition, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before_or_equals)
                .valueObject(value);
    }

    public static LeafMetadata beforeOrEqualsSupplierMetadata(DefaultCondition<?> condition, Supplier<?> value) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before_or_equals)
                .valueSupplier(value);
    }

    public static LeafMetadata beforeOrEqualsTemporalConditionMetadata(DefaultCondition<?> c1, DefaultCondition<?> c2) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(c1).operator(before_or_equals).valueCondition(c2);
    }

    // age at

    public static LeafMetadata ageAtValueMetadata(DefaultCondition<?> condition, Object value) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(condition).operator(age_at).valueObject(value);
    }

    public static LeafMetadata ageAtTemporalFieldMetadata(DefaultCondition<?> condition, DslField field) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(condition).operator(age_at).field(field);
    }

    public static LeafMetadata ageAtTemporalConditionMetadata(DefaultCondition<?> c1, DefaultCondition<?> c2) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(c1).operator(age_at).valueCondition(c2);
    }

    public static LeafMetadata ageAtSupplierMetadata(DefaultCondition<?> condition, Supplier<?> supplier) {
        return new LeafMetadata(FIELD_PREDICATE).valueCondition(condition).operator(age_at).valueSupplier(supplier);
    }

    // string

    public static LeafMetadata matchesMetadata(PredicateMetadata metadata, String value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(matches).valueString(value);
    }

    public static LeafMetadata containsMetadata(PredicateMetadata metadata, String value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(contains).valueString(value);
    }

    public static LeafMetadata startsWithMetadata(PredicateMetadata metadata, String value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(starts_with).valueString(value);
    }

    public static LeafMetadata endsWithMetadata(PredicateMetadata metadata, String value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(ends_with).valueString(value);
    }

    // boolean

    public static LeafMetadata notMetadata(PredicateMetadata metadata) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(not);
    }

    public static LeafMetadata andMetadata(PredicateMetadata metadata, boolean value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(and).valueObject(value);
    }

    public static LeafMetadata andMetadata(PredicateMetadata metadata, Readable value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(and).valueReadable(value);
    }

    public static LeafMetadata orMetadata(PredicateMetadata metadata, boolean value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(or).valueObject(value);
    }

    public static LeafMetadata orMetadata(PredicateMetadata metadata, Readable value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(or).valueReadable(value);
    }

    public static LeafMetadata xorMetadata(PredicateMetadata metadata, boolean value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(xor).valueObject(value);
    }

    public static LeafMetadata xorMetadata(PredicateMetadata metadata, Readable value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(xor).valueReadable(value);
    }

    // is

    public static LeafMetadata isMetadata(PredicateMetadata metadata, boolean value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(is).valueObject(value);
    }

    // lesser

    public static LeafMetadata lesserThanMetadata(PredicateMetadata metadata, Object value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(lesser_than).valueObject(value);
    }

    public static LeafMetadata lesserThanMetadata(PredicateMetadata metadata, Readable field2) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(lesser_than).valueReadable(field2);
    }

    public static LeafMetadata lesserThanMetadata(PredicateMetadata metadata, DslField field2) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(lesser_than).field(field2);
    }

    public static LeafMetadata lesserOrEqualsMetadata(PredicateMetadata metadata, Object value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(lesser_or_equals).valueObject(value);
    }

    public static LeafMetadata lesserOrEqualsMetadata(PredicateMetadata metadata, DslField field2) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(lesser_or_equals).field(field2);
    }

    // lesser

    public static LeafMetadata greaterThanMetadata(PredicateMetadata metadata, Object value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(greater_than).valueObject(value);
    }

    public static LeafMetadata greaterThanMetadata(PredicateMetadata metadata, Readable field2) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(greater_than).valueReadable(field2);
    }

    public static LeafMetadata greaterThanMetadata(PredicateMetadata metadata, DslField field2) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(greater_than).field(field2);
    }

    public static LeafMetadata greaterOrEqualsMetadata(PredicateMetadata metadata, Object value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(greater_or_equals).valueObject(value);
    }

    public static LeafMetadata greaterOrEqualsMetadata(PredicateMetadata metadata, Readable field2) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(greater_or_equals).valueReadable(field2);
    }

    public static LeafMetadata greaterOrEqualsMetadata(PredicateMetadata metadata, DslField field2) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(greater_or_equals).field(field2);
    }
    // length is

    public static LeafMetadata lengthIsMetadata(PredicateMetadata metadata) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(length_is);
    }

    // length is

    public static LeafMetadata containsMetadata(PredicateMetadata metadata, Object value) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(contains).valueObject(value);
    }

    public static LeafMetadata containsMetadata(PredicateMetadata metadata, Collection<Object> values) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(contains).valueListObject(values);
    }

    // empty

    public static LeafMetadata isEmptyMetadata(PredicateMetadata metadata) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(is_empty);
    }

    public static LeafMetadata isNotEmptyMetadata(PredicateMetadata metadata) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(is_not_empty);
    }

    // size

    public static LeafMetadata hasSizeMetadata(PredicateMetadata metadata, int size) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(has_size).valueObject(size);
    }

    public static LeafMetadata hasNotSizeMetadata(PredicateMetadata metadata, int size) {
        return new LeafMetadata(metadata, FIELD_PREDICATE).operator(has_not_size).valueObject(size);
    }

    // local date suppliers

    public static LeafMetadata todayMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).operator(today);
    }

    public static LeafMetadata todayPlusMetadata(int value, Object unit) {
        return new LeafMetadata(LEAF_PREDICATE).operator(today_plus).valueObject(value).temporalUnit(unit);
    }

    public static LeafMetadata todayMinusMetadata(int value, Object unit) {
        return new LeafMetadata(LEAF_PREDICATE).operator(today_minus).valueObject(value).temporalUnit(unit);
    }

    public static LeafMetadata firstDayOfThisMonthMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).operator(first_day_of_this_month);
    }

    public static LeafMetadata firstDayOfThisYearMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).operator(first_day_of_this_year);
    }

    public static LeafMetadata lastDayOfThisMonthMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).operator(last_day_of_this_month);
    }

    public static LeafMetadata lastDayOfThisYearMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).operator(last_day_of_this_year);
    }

    public static LeafMetadata dateMetadata(Object date) {
        return new LeafMetadata(LEAF_PREDICATE).valueString(date.toString());
    }

    // temporal adjusters

    public static LeafMetadata firstDayOfMonthMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).operator(first_day_of_month);
    }

    public static LeafMetadata firstDayOfNextMonthMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).operator(first_day_of_next_month);
    }

    public static LeafMetadata firstDayOfYearMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).operator(first_day_of_year);
    }

    public static LeafMetadata firstDayOfNextYearMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).operator(first_day_of_next_year);
    }

    public static LeafMetadata lastDayOfMonthMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).operator(last_day_of_month);
    }

    public static LeafMetadata lastDayOfYearMetadata() {
        return new LeafMetadata(LEAF_PREDICATE).operator(last_day_of_year);
    }

    private static String formatListReadable(Collection<? extends Readable> readables) {
        return readables.stream().map(Readable::readable).collect(COLLECTOR_LIST);
    }

    private static String formatListObject(Collection<?> readables) {
        return readables.stream().map(Object::toString).collect(COLLECTOR_LIST);
    }
}
