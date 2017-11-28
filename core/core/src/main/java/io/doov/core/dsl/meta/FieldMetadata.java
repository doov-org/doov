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

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.function.Supplier;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.ast.AstVisitorUtils;

public class FieldMetadata implements Metadata {

    private static final FieldMetadata EMPTY = new FieldMetadata();

    private final Readable field;
    private final Readable operator;
    private final Readable value;

    public FieldMetadata() {
        field = null;
        operator = null;
        value = null;
    }

    private FieldMetadata(Readable field, String operator, Object value) {
        this.field = field;
        this.operator = () -> operator;
        this.value = () -> "'" + value + "'";
    }

    private FieldMetadata(Readable field, String operator, Readable value) {
        this.field = field;
        this.operator = () -> operator;
        this.value = value;
    }

    private FieldMetadata(Readable field, Readable operator, Readable value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public Readable getField() {
        return field;
    }

    public Readable getOperator() {
        return operator;
    }

    public Readable getValue() {
        return value;
    }

    public static FieldMetadata emptyMetadata() {
        return EMPTY;
    }

    public static FieldMetadata fieldOnlyMetadata(Readable field) {
        return new FieldMetadata(field, (String) null, null);
    }

    public static FieldMetadata trueMetadata() {
        return new FieldMetadata(null, "always true", null);
    }

    public static FieldMetadata falseMetadata() {
        return new FieldMetadata(null, "always false", null);
    }

    public static FieldMetadata minMetadata(List<? extends Readable> values) {
        return new FieldMetadata(null, "min", formatReadableList(values));
    }

    public static FieldMetadata sumMetadata(List<? extends Readable> values) {
        return new FieldMetadata(null, "sum", formatReadableList(values));
    }

    public static FieldMetadata timesMetadata(Readable field, int multiplier) {
        return new FieldMetadata(field, "times", multiplier);
    }

    public static FieldMetadata whenMetadata(Readable field, Readable condition) {
        return new FieldMetadata(field, "when", condition);
    }

    public static FieldMetadata availableMetadata(Readable field) {
        return new FieldMetadata(field, "available", null);
    }

    public static FieldMetadata notAvailableMetadata(Readable field) {
        return new FieldMetadata(field, "not available", null);
    }

    public static FieldMetadata equalsMetadata(Readable field, Object value) {
        return new FieldMetadata(field, "equals", value);
    }

    public static FieldMetadata notEqualsMetadata(Readable field, Object value) {
        return new FieldMetadata(field, "not equals", value);
    }

    public static FieldMetadata nullMetadata(Readable field) {
        return new FieldMetadata(field, "is null", null);
    }

    public static FieldMetadata notNullMetadata(Readable field) {
        return new FieldMetadata(field, "is not null", null);
    }

    public static FieldMetadata matchAnyMetadata(Readable field, Object... values) {
        return new FieldMetadata(field, "match any", format(values));
    }

    public static FieldMetadata matchAllMetadata(Readable field, Object... values) {
        return new FieldMetadata(field, "match all", format(values));
    }

    public static FieldMetadata matchNoneMetadata(Readable field, Object... values) {
        return new FieldMetadata(field, "match none", format(values));
    }

    public static FieldMetadata mapToIntMetadata(Readable field) {
        return new FieldMetadata(field, "map to int", null);
    }

    public static FieldMetadata withMetadata(Readable field, Object adjuster) {
        return new FieldMetadata(field, "with", adjuster);
    }

    public static FieldMetadata minusMetadata(Readable field, int value, Object unit) {
        return new FieldMetadata(field, "minus", value + " " + unit);
    }

    public static FieldMetadata minusMetadata(Readable field1, Readable field2, Object unit) {
        return new FieldMetadata(field1, "minus", field2.readable() + " " + unit);
    }

    public static FieldMetadata plusMetadata(Readable field, int value, Object unit) {
        return new FieldMetadata(field, "plus", value + " " + unit);
    }

    public static FieldMetadata plusMetadata(Readable field1, Readable field2, Object unit) {
        return new FieldMetadata(field1, "plus", field2.readable() + " " + unit);
    }

    public static FieldMetadata afterMetadata(Readable field, Object value) {
        return new FieldMetadata(field, "after", value);
    }

    public static FieldMetadata beforeMetadata(Readable field, Object value) {
        return new FieldMetadata(field, "before", value);
    }

    public static FieldMetadata afterMetadata(Readable field1, Readable field2) {
        return new FieldMetadata(field1, "after", field2);
    }

    public static FieldMetadata beforeMetadata(Readable field1, Readable field2) {
        return new FieldMetadata(field1, "before", field2);
    }

    public static FieldMetadata afterMetadata(Readable field, Supplier<Object> value) {
        return new FieldMetadata(field, "after", () -> value.get().toString());
    }

    public static FieldMetadata beforeMetadata(Readable field, Supplier<Object> supplier) {
        return new FieldMetadata(field, "before", () -> supplier.get().toString());
    }

    public static FieldMetadata ageAtMetadata(Readable field, Object value) {
        return new FieldMetadata(field, () -> "age at " + value, null);
    }

    public static FieldMetadata ageAtMetadata(Readable field1, Readable field2) {
        return new FieldMetadata(field1, () -> "age at " + field2.readable(), null);
    }

    public static FieldMetadata ageAtMetadata(Readable field, Supplier<Object> supplier) {
        return new FieldMetadata(field, () -> "age at " + supplier.get().toString(), null);
    }

    public static FieldMetadata matchesMetadata(Readable field, String value) {
        return new FieldMetadata(field, "matches", value);
    }

    public static FieldMetadata containsMetadata(Readable field, String value) {
        return new FieldMetadata(field, "contains", value);
    }

    public static FieldMetadata startsWithMetadata(Readable field, String value) {
        return new FieldMetadata(field, "starts with", value);
    }

    public static FieldMetadata endsWithMetadata(Readable field, String value) {
        return new FieldMetadata(field, "ends with", value);
    }

    public static FieldMetadata notMetadata(Readable field) {
        return new FieldMetadata(field, "not", null);
    }

    public static FieldMetadata andMetadata(Readable field, boolean value) {
        return new FieldMetadata(field, "and", value);
    }

    public static FieldMetadata andMetadata(Readable field, Readable value) {
        return new FieldMetadata(field, "and", value);
    }

    public static FieldMetadata orMetadata(Readable field, boolean value) {
        return new FieldMetadata(field, "or", value);
    }

    public static FieldMetadata orMetadata(Readable field, Readable value) {
        return new FieldMetadata(field, "or", value);
    }

    public static FieldMetadata xorMetadata(Readable field, boolean value) {
        return new FieldMetadata(field, "xor", value);
    }

    public static FieldMetadata xorMetadata(Readable field, Readable value) {
        return new FieldMetadata(field, "xor", value);
    }

    public static FieldMetadata isMetadata(Readable field, boolean value) {
        return new FieldMetadata(field, "is", value);
    }

    public static FieldMetadata lesserThanMetadata(Readable field, Object value) {
        return new FieldMetadata(field, "lesser than", value);
    }

    public static FieldMetadata lesserThanMetadata(Readable field1, Readable field2) {
        return new FieldMetadata(field1, "lesser than", field2);
    }

    public static FieldMetadata lesserOrEqualsMetadata(Readable field, Object value) {
        return new FieldMetadata(field, "lesser or equals", value);
    }

    public static FieldMetadata lesserOrEqualsMetadata(Readable field1, Readable field2) {
        return new FieldMetadata(field1, "lesser or equals", field2);
    }

    public static FieldMetadata greaterThanMetadata(Readable field, Object value) {
        return new FieldMetadata(field, "greater than", value);
    }

    public static FieldMetadata greaterThanMetadata(Readable field1, Readable field2) {
        return new FieldMetadata(field1, "greater than", field2);
    }

    public static FieldMetadata greaterOrEqualsMetadata(Readable field, Object value) {
        return new FieldMetadata(field, "greater or equals", value);
    }

    public static FieldMetadata greaterOrEqualsMetadata(Readable field1, Readable field2) {
        return new FieldMetadata(field1, "greater or equals", field2);
    }

    public static FieldMetadata lengthIsMetadata(Readable field) {
        return new FieldMetadata(field, "length is", null);
    }

    public static FieldMetadata containsMetadata(Readable field, Object value) {
        return new FieldMetadata(field, "contains", value);
    }

    public static FieldMetadata containsMetadata(Readable field, Object... values) {
        return new FieldMetadata(field, "contains", format(values));
    }

    public static FieldMetadata isEmptyMetadata(Readable field) {
        return new FieldMetadata(field, "is empty", null);
    }

    public static FieldMetadata isNotEmptyMetadata(Readable field) {
        return new FieldMetadata(field, "is not empty", null);
    }

    public static FieldMetadata hasSizeMetadata(Readable field, int size) {
        return new FieldMetadata(field, "has size", size);
    }

    public static FieldMetadata hasNotSizeMetadata(Readable field, int size) {
        return new FieldMetadata(field, "has not size", size);
    }

    private static String formatReadableList(List<? extends Readable> readables) {
        return readables.stream().map(Object::toString).collect(joining(", ", "[", "]"));
    }

    private static String format(Object... values) {
        return stream(values).map(Object::toString).collect(joining(", ", "[", "]"));
    }

    private static String format(Readable... values) {
        return stream(values).map(Readable::readable).collect(joining(", ", "[", "]"));
    }

    @Override
    public Metadata merge(Metadata metadata) {
        if (equals(EMPTY)) {
            return metadata;
        }
        return new FieldMetadata(field, operator == null ? null : operator.readable(), metadata);
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

}
