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
import static io.doov.core.dsl.meta.ElementType.*;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE_MATCH_ANY;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;
import static java.util.stream.Collectors.joining;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collector;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.*;

public class LeafPredicateMetadata extends LeafMetadata implements PredicateMetadata {
    private static final Collector<CharSequence, ?, String> COLLECTOR_LIST = joining(", ", " : ", "");
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
    public PredicateMetadata merge(LeafPredicateMetadata other) {
        removeDuplicate(elements(), other.elements());
        final Deque<Element> merge = new ArrayDeque<>(elements());
        merge.addAll(other.elements());
        return new LeafPredicateMetadata(merge, mergeType(type(), other.type()));
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
    public Metadata message(Context context) {
        if (type() == FIELD_PREDICATE_MATCH_ANY) {
            final DslField field = (DslField) elements().getFirst().getReadable();
            return new LeafPredicateMetadata(FIELD_PREDICATE).field(field).operator(not_equals)
                            .valueObject(context.getEvalValue(field.id()));
        }
        return this;
    }

    private LeafPredicateMetadata add(Element element) {
        if (element != null) {
            elements().add(element);
        }
        return this;
    }

    // field

    private LeafPredicateMetadata field(DslField readable) {
        return add(readable == null ? null : new Element(readable, FIELD));
    }

    // operator

    private LeafPredicateMetadata operator(Operator op) {
        return add(op == null ? null : new Element(op, OPERATOR));
    }

    // value

    private LeafPredicateMetadata valueObject(Object readable) {
        if (readable == null)
            return valueReadable(() -> "null");
        if (readable instanceof String)
            return valueString((String) readable);
        return valueReadable(() -> String.valueOf(readable));
    }

    private LeafPredicateMetadata valueString(String readable) {
        return add(readable == null ? null : new Element(() -> readable, STRING_VALUE));
    }

    private LeafPredicateMetadata temporalUnit(Object unit) {
        return add(unit == null ? null : new Element(() -> unit.toString().toLowerCase(), TEMPORAL_UNIT));
    }

    private LeafPredicateMetadata valueCondition(DefaultCondition<?> condition) {
        ((LeafPredicateMetadata) condition.getMetadata()).elements().stream().forEach(e -> add(e));
        return this;
    }

    public LeafPredicateMetadata valueReadable(Readable readable) {
        return add(readable == null ? null : new Element(readable, VALUE));
    }

    private LeafPredicateMetadata valueTemporalAdjuster(Readable adjuster) {
        return add(((LeafPredicateMetadata) adjuster).elements().getFirst());
    }

    private LeafPredicateMetadata valueSupplier(Supplier<?> readable) {
        return add(readable == null ? null : new Element(() -> String.valueOf(readable.get()), VALUE));
    }

    private LeafPredicateMetadata valueUnknown(String readable) {
        return add(readable == null ? null : new Element(() -> "-function- " + readable, UNKNOWN));
    }

    private LeafPredicateMetadata valueListReadable(Collection<? extends Readable> readables) {
        return add(readables == null || readables.isEmpty() ? null
                        : new Element(() -> formatListReadable(readables), VALUE));
    }

    private LeafPredicateMetadata valueListObject(Collection<?> readables) {
        return add(readables == null || readables.isEmpty() ? null
                        : new Element(() -> formatListObject(readables), VALUE));
    }

    public static LeafPredicateMetadata fieldMetadata(DslField field) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).field(field);
    }

    public static LeafPredicateMetadata unknownMetadata(String value) {
        return new LeafPredicateMetadata(LEAF_PREDICATE).valueUnknown(value);
    }

    // boolean

    public static LeafPredicateMetadata trueMetadata() {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(always_true);
    }

    public static LeafPredicateMetadata falseMetadata() {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(always_false);
    }

    // min

    public static LeafPredicateMetadata minMetadata(Collection<? extends Readable> values) {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(min).valueListReadable(values);
    }

    // sum

    public static LeafPredicateMetadata sumMetadata(Collection<? extends Readable> values) {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(sum).valueListReadable(values);
    }

    // times

    public static LeafPredicateMetadata timesMetadata(Metadata metadata, int multiplier) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(times).valueObject(multiplier);
    }

    // when

    public static LeafPredicateMetadata whenMetadata(Metadata metadata, StepCondition condition) {
        final LeafPredicateMetadata exp = new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(when);
        exp.elements().add(leftParenthesis());
        exp.elements().addAll(condition.metadata().flatten());
        exp.elements().add(rightParenthesis());
        return exp;
    }

    // equals

    public static LeafPredicateMetadata equalsMetadata(Metadata metadata, Object value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(equals).valueObject(value);
    }

    public static LeafPredicateMetadata equalsMetadata(Metadata metadata, Readable value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(equals).valueReadable(value);
    }

    public static LeafPredicateMetadata equalsMetadata(Metadata metadata, DefaultCondition<?> condition) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(equals).valueCondition(condition);
    }

    public static LeafPredicateMetadata notEqualsMetadata(Metadata metadata, Object value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(not_equals).valueObject(value);
    }

    public static LeafPredicateMetadata notEqualsMetadata(Metadata metadata, Readable value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(not_equals).valueReadable(value);
    }

    // null

    public static LeafPredicateMetadata nullMetadata(Metadata metadata) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(is_null);
    }

    public static LeafPredicateMetadata notNullMetadata(Metadata metadata) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(is_not_null);
    }

    // match

    public static LeafPredicateMetadata matchAnyMetadata(Metadata metadata) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE_MATCH_ANY).operator(match_any)
                        .valueUnknown("-function-");
    }

    public static LeafPredicateMetadata matchAnyMetadata(Metadata metadata, Collection<?> values) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE_MATCH_ANY).operator(match_any)
                        .valueListObject(values);
    }

    public static LeafPredicateMetadata matchAllMetadata(Metadata metadata) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(match_all).valueUnknown("-function-");
    }

    public static LeafPredicateMetadata matchAllMetadata(Metadata metadata, Collection<?> values) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(match_all).valueListObject(values);
    }

    public static LeafPredicateMetadata matchNoneMetadata(Metadata metadata) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(match_none).valueUnknown("-function-");
    }

    public static LeafPredicateMetadata matchNoneMetadata(Metadata metadata, Collection<?> values) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(match_none).valueListObject(values);
    }

    // map

    public static LeafPredicateMetadata mapToIntMetadata(Metadata metadata) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(as_a_number).valueUnknown("");
    }

    public static LeafPredicateMetadata mapToStringMetadata(Metadata metadata) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(as_string).valueUnknown("");
    }

    public static LeafPredicateMetadata mapAsMetadata(Metadata metadata, String readable) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(as).valueReadable(() -> readable);
    }

    public static LeafPredicateMetadata mapUsingMetadata(Metadata metadata, String readable, Readable condition) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(as).valueReadable(() -> readable)
                        .operator(DefaultOperator.with).valueReadable(condition);
    }

    // with

    public static LeafPredicateMetadata withMetadata(Metadata metadata, Readable adjuster) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(with).valueTemporalAdjuster(adjuster);
    }

    // minus

    public static LeafPredicateMetadata minusMetadata(Metadata metadata, int value, Object unit) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(minus).valueObject(value)
                        .temporalUnit(unit);
    }

    public static LeafPredicateMetadata minusMetadata(Metadata metadata, DslField field2, Object unit) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(minus).field(field2).temporalUnit(unit);
    }

    // plus

    public static LeafPredicateMetadata plusMetadata(Metadata metadata, int value, Object unit) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(plus).valueObject(value)
                        .temporalUnit(unit);
    }

    public static LeafPredicateMetadata plusMetadata(Metadata metadata, DslField field2, Object unit) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(plus).field(field2).temporalUnit(unit);
    }

    // after

    public static LeafPredicateMetadata afterValueMetadata(DefaultCondition<?> condition, Object value) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after).valueObject(value);
    }

    public static LeafPredicateMetadata afterTemporalFieldMetadata(DefaultCondition<?> condition, DslField field) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after).field(field);
    }

    public static LeafPredicateMetadata afterTemporalConditionMetadata(DefaultCondition<?> c1, DefaultCondition<?> c2) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(c1).operator(after).valueCondition(c2);
    }

    public static LeafPredicateMetadata afterSupplierMetadata(DefaultCondition<?> condition, Supplier<?> value) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after)
                        .valueSupplier(value);
    }

    public static LeafPredicateMetadata afterOrEqualsValueMetadata(DefaultCondition<?> condition, Object value) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after_or_equals)
                        .valueObject(value);
    }

    public static LeafPredicateMetadata afterOrEqualsSupplierMetadata(DefaultCondition<?> condition,
                    Supplier<?> value) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(condition).operator(after_or_equals)
                        .valueSupplier(value);
    }

    public static LeafPredicateMetadata afterOrEqualsTemporalConditionMetadata(DefaultCondition<?> c1,
                    DefaultCondition<?> c2) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(c1).operator(after_or_equals)
                        .valueCondition(c2);
    }

    // before

    public static LeafPredicateMetadata beforeValueMetadata(DefaultCondition<?> condition, Object value) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before).valueObject(value);
    }

    public static LeafPredicateMetadata beforeTemporalFieldMetadata(DefaultCondition<?> condition, DslField field) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before).field(field);
    }

    public static LeafPredicateMetadata beforeTemporalConditionMetadata(DefaultCondition<?> c1,
                    DefaultCondition<?> c2) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(c1).operator(before).valueCondition(c2);
    }

    public static LeafPredicateMetadata beforeSupplierMetadata(DefaultCondition<?> condition, Supplier<?> value) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before)
                        .valueSupplier(value);
    }

    public static LeafPredicateMetadata beforeOrEqualsValueMetadata(DefaultCondition<?> condition, Object value) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before_or_equals)
                        .valueObject(value);
    }

    public static LeafPredicateMetadata beforeOrEqualsSupplierMetadata(DefaultCondition<?> condition,
                    Supplier<?> value) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(condition).operator(before_or_equals)
                        .valueSupplier(value);
    }

    public static LeafPredicateMetadata beforeOrEqualsTemporalConditionMetadata(DefaultCondition<?> c1,
                    DefaultCondition<?> c2) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(c1).operator(before_or_equals)
                        .valueCondition(c2);
    }

    // age at

    public static LeafPredicateMetadata ageAtValueMetadata(DefaultCondition<?> condition, Object value) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(condition).operator(age_at).valueObject(value);
    }

    public static LeafPredicateMetadata ageAtTemporalFieldMetadata(DefaultCondition<?> condition, DslField field) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(condition).operator(age_at).field(field);
    }

    public static LeafPredicateMetadata ageAtTemporalConditionMetadata(DefaultCondition<?> c1, DefaultCondition<?> c2) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(c1).operator(age_at).valueCondition(c2);
    }

    public static LeafPredicateMetadata ageAtSupplierMetadata(DefaultCondition<?> condition, Supplier<?> supplier) {
        return new LeafPredicateMetadata(FIELD_PREDICATE).valueCondition(condition).operator(age_at)
                        .valueSupplier(supplier);
    }

    // string

    public static LeafPredicateMetadata matchesMetadata(Metadata metadata, String value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(matches).valueString(value);
    }

    public static LeafPredicateMetadata containsMetadata(Metadata metadata, String value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(contains).valueString(value);
    }

    public static LeafPredicateMetadata startsWithMetadata(Metadata metadata, String value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(starts_with).valueString(value);
    }

    public static LeafPredicateMetadata endsWithMetadata(Metadata metadata, String value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(ends_with).valueString(value);
    }

    // boolean

    public static LeafPredicateMetadata notMetadata(Metadata metadata) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(not);
    }

    public static LeafPredicateMetadata andMetadata(Metadata metadata, boolean value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(and).valueObject(value);
    }

    public static LeafPredicateMetadata andMetadata(Metadata metadata, Readable value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(and).valueReadable(value);
    }

    public static LeafPredicateMetadata orMetadata(Metadata metadata, boolean value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(or).valueObject(value);
    }

    public static LeafPredicateMetadata orMetadata(Metadata metadata, Readable value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(or).valueReadable(value);
    }

    public static LeafPredicateMetadata xorMetadata(Metadata metadata, boolean value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(xor).valueObject(value);
    }

    public static LeafPredicateMetadata xorMetadata(Metadata metadata, Readable value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(xor).valueReadable(value);
    }

    // is

    public static LeafPredicateMetadata isMetadata(Metadata metadata, boolean value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(is).valueObject(value);
    }

    // lesser

    public static LeafPredicateMetadata lesserThanMetadata(Metadata metadata, Object value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(lesser_than).valueObject(value);
    }

    public static LeafPredicateMetadata lesserThanMetadata(Metadata metadata, Readable field2) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(lesser_than).valueReadable(field2);
    }

    public static LeafPredicateMetadata lesserThanMetadata(Metadata metadata, DslField field2) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(lesser_than).field(field2);
    }

    public static LeafPredicateMetadata lesserOrEqualsMetadata(Metadata metadata, Object value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(lesser_or_equals).valueObject(value);
    }

    public static LeafPredicateMetadata lesserOrEqualsMetadata(Metadata metadata, DslField field2) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(lesser_or_equals).field(field2);
    }

    // lesser

    public static LeafPredicateMetadata greaterThanMetadata(Metadata metadata, Object value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(greater_than).valueObject(value);
    }

    public static LeafPredicateMetadata greaterThanMetadata(Metadata metadata, Readable field2) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(greater_than).valueReadable(field2);
    }

    public static LeafPredicateMetadata greaterThanMetadata(Metadata metadata, DslField field2) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(greater_than).field(field2);
    }

    public static LeafPredicateMetadata greaterOrEqualsMetadata(Metadata metadata, Object value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(greater_or_equals).valueObject(value);
    }

    public static LeafPredicateMetadata greaterOrEqualsMetadata(Metadata metadata, Readable field2) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(greater_or_equals).valueReadable(field2);
    }

    public static LeafPredicateMetadata greaterOrEqualsMetadata(Metadata metadata, DslField field2) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(greater_or_equals).field(field2);
    }
    // length is

    public static LeafPredicateMetadata lengthIsMetadata(Metadata metadata) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(length_is);
    }

    // length is

    public static LeafPredicateMetadata containsMetadata(Metadata metadata, Object value) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(contains).valueObject(value);
    }

    public static LeafPredicateMetadata containsMetadata(Metadata metadata, Collection<Object> values) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(contains).valueListObject(values);
    }

    // empty

    public static LeafPredicateMetadata isEmptyMetadata(Metadata metadata) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(is_empty);
    }

    public static LeafPredicateMetadata isNotEmptyMetadata(Metadata metadata) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(is_not_empty);
    }

    // size

    public static LeafPredicateMetadata hasSizeMetadata(Metadata metadata, int size) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(has_size).valueObject(size);
    }

    public static LeafPredicateMetadata hasNotSizeMetadata(Metadata metadata, int size) {
        return new LeafPredicateMetadata(metadata, FIELD_PREDICATE).operator(has_not_size).valueObject(size);
    }

    // local date suppliers

    public static LeafPredicateMetadata todayMetadata() {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(today);
    }

    public static LeafPredicateMetadata todayPlusMetadata(int value, Object unit) {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(today_plus).valueObject(value).temporalUnit(unit);
    }

    public static LeafPredicateMetadata todayMinusMetadata(int value, Object unit) {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(today_minus).valueObject(value).temporalUnit(unit);
    }

    public static LeafPredicateMetadata firstDayOfThisMonthMetadata() {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(first_day_of_this_month);
    }

    public static LeafPredicateMetadata firstDayOfThisYearMetadata() {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(first_day_of_this_year);
    }

    public static LeafPredicateMetadata lastDayOfThisMonthMetadata() {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(last_day_of_this_month);
    }

    public static LeafPredicateMetadata lastDayOfThisYearMetadata() {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(last_day_of_this_year);
    }

    public static LeafPredicateMetadata dateMetadata(Object date) {
        return new LeafPredicateMetadata(LEAF_PREDICATE).valueString(date.toString());
    }

    // temporal adjusters

    public static LeafPredicateMetadata firstDayOfMonthMetadata() {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(first_day_of_month);
    }

    public static LeafPredicateMetadata firstDayOfNextMonthMetadata() {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(first_day_of_next_month);
    }

    public static LeafPredicateMetadata firstDayOfYearMetadata() {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(first_day_of_year);
    }

    public static LeafPredicateMetadata firstDayOfNextYearMetadata() {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(first_day_of_next_year);
    }

    public static LeafPredicateMetadata lastDayOfMonthMetadata() {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(last_day_of_month);
    }

    public static LeafPredicateMetadata lastDayOfYearMetadata() {
        return new LeafPredicateMetadata(LEAF_PREDICATE).operator(last_day_of_year);
    }

    private static String formatListReadable(Collection<? extends Readable> readables) {
        return readables.stream().map(Readable::readable).collect(COLLECTOR_LIST);
    }

    private static String formatListObject(Collection<?> readables) {
        return readables.stream().map(Object::toString).collect(COLLECTOR_LIST);
    }
}
