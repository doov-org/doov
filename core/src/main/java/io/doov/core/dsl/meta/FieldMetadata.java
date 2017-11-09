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

import io.doov.core.dsl.DslField;
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

    public static FieldMetadata fieldOnlyMetadata(DslField field) {
        return new FieldMetadata(field, (String) null, null);
    }

    public static FieldMetadata trueMetadata() {
        return new FieldMetadata(null, "always true", null);
    }

    public static FieldMetadata minMetadata(List<DslField> values) {
        return new FieldMetadata(null, "min", formatReadableList(values));
    }

    public static FieldMetadata sumMetadata(List<DslField> values) {
        return new FieldMetadata(null, "sum", formatReadableList(values));
    }

    public static FieldMetadata availableMetadata(DslField field) {
        return new FieldMetadata(field, "available", null);
    }

    public static FieldMetadata notAvailableMetadata(DslField field) {
        return new FieldMetadata(field, "not available", null);
    }

    public static FieldMetadata equalsMetadata(DslField field, Object value) {
        return new FieldMetadata(field, "equals", value);
    }

    public static FieldMetadata notEqualsMetadata(DslField field, Object value) {
        return new FieldMetadata(field, "not equals", value);
    }

    public static FieldMetadata nullMetadata(DslField field, Object value) {
        return new FieldMetadata(field, "is", value);
    }

    public static FieldMetadata notNullMetadata(DslField field, Object value) {
        return new FieldMetadata(field, "is not", value);
    }

    public static FieldMetadata matchAnyMetadata(DslField field, Object... values) {
        return new FieldMetadata(field, "match any", format(values));
    }

    public static FieldMetadata matchAllMetadata(DslField field, Object... values) {
        return new FieldMetadata(field, "match all", format(values));
    }

    public static FieldMetadata matchNoneMetadata(DslField field, Object... values) {
        return new FieldMetadata(field, "match none", format(values));
    }

    public static FieldMetadata minusMetadata(DslField field, int value, Object unit) {
        return new FieldMetadata(field, "minus", value + " " + unit);
    }

    public static FieldMetadata minusMetadata(DslField field1, DslField field2, Object unit) {
        return new FieldMetadata(field1, "minus", field2.readable() + " " + unit);
    }

    public static FieldMetadata plusMetadata(DslField field, int value, Object unit) {
        return new FieldMetadata(field, "plus", value + " " + unit);
    }

    public static FieldMetadata plusMetadata(DslField field1, DslField field2, Object unit) {
        return new FieldMetadata(field1, "plus", field2.readable() + " " + unit);
    }

    public static FieldMetadata afterMetadata(DslField field, Object value) {
        return new FieldMetadata(field, "after", value);
    }

    public static FieldMetadata beforeMetadata(DslField field, Object value) {
        return new FieldMetadata(field, "before", value);
    }

    public static FieldMetadata afterMetadata(DslField field1, DslField field2) {
        return new FieldMetadata(field1, "after", field2);
    }

    public static FieldMetadata beforeMetadata(DslField field1, DslField field2) {
        return new FieldMetadata(field1, "before", field2);
    }

    public static FieldMetadata afterMetadata(DslField field, Supplier<Object> value) {
        return new FieldMetadata(field, "after", () -> value.get().toString());
    }

    public static FieldMetadata afterMetadata(DslField field, Readable value) {
        return new FieldMetadata(field, "after", value);
    }

    public static FieldMetadata beforeMetadata(DslField field, Supplier<Object> supplier) {
        return new FieldMetadata(field, "before", () -> supplier.get().toString());
    }

    public static FieldMetadata beforeMetadata(DslField field, Readable value) {
        return new FieldMetadata(field, "before", value);
    }

    public static FieldMetadata ageAtMetadata(DslField field, Object value) {
        return new FieldMetadata(field, () -> "age at " + value, null);
    }

    public static FieldMetadata ageAtMetadata(DslField field1, DslField field2) {
        return new FieldMetadata(field1, () -> "age at " + field2.readable(), null);
    }

    public static FieldMetadata ageAtMetadata(DslField field, Supplier<Object> supplier) {
        return new FieldMetadata(field, () -> "age at " + supplier.get().toString(), null);
    }

    public static FieldMetadata matchesMetadata(DslField field, String value) {
        return new FieldMetadata(field, "matches", value);
    }

    public static FieldMetadata containsMetadata(DslField field, String value) {
        return new FieldMetadata(field, "contains", value);
    }

    public static FieldMetadata startsWithMetadata(DslField field, String value) {
        return new FieldMetadata(field, "starts with", value);
    }

    public static FieldMetadata endsWithMetadata(DslField field, String value) {
        return new FieldMetadata(field, "ends with", value);
    }

    public static FieldMetadata isMetadata(DslField field, boolean value) {
        return new FieldMetadata(field, "is", value);
    }

    public static FieldMetadata lesserThanMetadata(DslField field, Object value) {
        return new FieldMetadata(field, "lesser than", value);
    }

    public static FieldMetadata lesserThanMetadata(DslField field1, DslField field2) {
        return new FieldMetadata(field1, "lesser than", field2);
    }

    public static FieldMetadata lesserOrEqualsMetadata(DslField field, Object value) {
        return new FieldMetadata(field, "lesser or equals", value);
    }

    public static FieldMetadata lesserOrEqualsMetadata(DslField field1, DslField field2) {
        return new FieldMetadata(field1, "lesser or equals", field2);
    }

    public static FieldMetadata greaterThanMetadata(DslField field, Object value) {
        return new FieldMetadata(field, "greater than", value);
    }

    public static FieldMetadata greaterThanMetadata(DslField field1, DslField field2) {
        return new FieldMetadata(field1, "greater than", field2);
    }

    public static FieldMetadata greaterOrEqualsMetadata(DslField field, Object value) {
        return new FieldMetadata(field, "greater or equals", value);
    }

    public static FieldMetadata greaterOrEqualsMetadata(DslField field1, DslField field2) {
        return new FieldMetadata(field1, "greater or equals", field2);
    }

    public static FieldMetadata lengthIsMetadata(DslField field) {
        return new FieldMetadata(field, "length is", null);
    }

    public static FieldMetadata containsMetadata(DslField field, Object value) {
        return new FieldMetadata(field, "contains", value);
    }

    public static FieldMetadata containsMetadata(DslField field, Object... values) {
        return new FieldMetadata(field, "contains", format(values));
    }

    public static FieldMetadata isEmptyMetadata(DslField field) {
        return new FieldMetadata(field, "is empty", null);
    }

    public static FieldMetadata isNotEmptyMetadata(DslField field) {
        return new FieldMetadata(field, "is not empty", null);
    }

    public static FieldMetadata hasSizeMetadata(DslField field, int size) {
        return new FieldMetadata(field, "has size", size);
    }

    public static FieldMetadata hasNotSizeMetadata(DslField field, int size) {
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
        visitor.visit(this);
    }

}
