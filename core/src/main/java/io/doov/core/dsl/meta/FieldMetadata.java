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

import static java.util.stream.Collectors.joining;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.field.*;

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

    public static FieldMetadata equalsMetadata(FieldInfo field, Object value) {
        return new FieldMetadata(field, "equals", value);
    }

    public static FieldMetadata notEqualsMetadata(FieldInfo field, Object value) {
        return new FieldMetadata(field, "not equals", value);
    }

    public static FieldMetadata nullMetadata(FieldInfo field, Object value) {
        return new FieldMetadata(field, "is", value);
    }

    public static FieldMetadata notNullMetadata(FieldInfo field, Object value) {
        return new FieldMetadata(field, "is not", value);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Temporal> FieldMetadata minusMetadata(
                    F field, int value, TemporalUnit unit) {
        return new FieldMetadata(field, "minus", value + " " + unit);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Temporal> FieldMetadata plusMetadata(
                    F field, int value, TemporalUnit unit) {
        return new FieldMetadata(field, "plus", value + " " + unit);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Temporal> FieldMetadata afterMetadata(F field, N value) {
        return new FieldMetadata(field, "after", value);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Temporal> FieldMetadata beforeMetadata(F field, N value) {
        return new FieldMetadata(field, "before", value);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Temporal> FieldMetadata afterMetadata(F field1, F field2) {
        return new FieldMetadata(field1, "after", field2);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Temporal> FieldMetadata beforeMetadata(F field1, F field2) {
        return new FieldMetadata(field1, "before", field2);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Temporal> FieldMetadata afterMetadata(
                    F field, Supplier<N> value) {
        return new FieldMetadata(field, "after", () -> value.get().toString());
    }

    public static <F extends DefaultFieldInfo<N>, N extends Temporal> FieldMetadata afterMetadata(
                    F field, Readable value) {
        return new FieldMetadata(field, "after", value);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Temporal> FieldMetadata beforeMetadata(
                    F field, Supplier<N> supplier) {
        return new FieldMetadata(field, "before", () -> supplier.get().toString());
    }

    public static <F extends DefaultFieldInfo<N>, N extends Temporal> FieldMetadata beforeMetadata(
                    F field, Readable value) {
        return new FieldMetadata(field, "before", value);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Temporal> FieldMetadata ageAtMetadata(
                    F field, N value) {
        return new FieldMetadata(field, () -> "age at " + value, null);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Temporal> FieldMetadata ageAtMetadata(
                    F field1, F field2) {
        return new FieldMetadata(field1, () -> "age at " + field2.readable(), null);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Temporal> FieldMetadata ageAtMetadata(
                    F field, Supplier<N> supplier) {
        return new FieldMetadata(field, () -> "age at " + supplier.get().toString(), null);
    }

    public static FieldMetadata matchesMetadata(StringFieldInfo field, String value) {
        return new FieldMetadata(field, "matches", value);
    }

    public static FieldMetadata containsMetadata(StringFieldInfo field, String value) {
        return new FieldMetadata(field, "contains", value);
    }

    public static FieldMetadata startsWithMetadata(StringFieldInfo field, String value) {
        return new FieldMetadata(field, "starts with", value);
    }

    public static FieldMetadata endsWithMetadata(StringFieldInfo field, String value) {
        return new FieldMetadata(field, "ends with", value);
    }

    public static FieldMetadata isMetadata(BooleanFieldInfo field, boolean value) {
        return new FieldMetadata(field, "is", value);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Number> FieldMetadata lesserThanMetadata(
                    F field, N value) {
        return new FieldMetadata(field, "lesser than", value);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Number> FieldMetadata lesserThanMetadata(
                    F field1, F field2) {
        return new FieldMetadata(field1, "lesser than", field2);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Number> FieldMetadata lesserOrEqualsMetadata(
                    F field, N value) {
        return new FieldMetadata(field, "lesser or equals", value);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Number> FieldMetadata lesserOrEqualsMetadata(
                    F field1, F field2) {
        return new FieldMetadata(field1, "lesser or equals", field2);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Number> FieldMetadata greaterThanMetadata(
                    F field, N value) {
        return new FieldMetadata(field, "greater than", value);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Number> FieldMetadata greaterThanMetadata(
                    F field1, F field2) {
        return new FieldMetadata(field1, "greater than", field2);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Number> FieldMetadata greaterOrEqualsMetadata(
                    F field, N value) {
        return new FieldMetadata(field, "greater or equals", value);
    }

    public static <F extends DefaultFieldInfo<N>, N extends Number> FieldMetadata greaterOrEqualsMetadata(
                    F field1, F field2) {
        return new FieldMetadata(field1, "greater or equals", field2);
    }

    public static FieldMetadata lengthIsMetadata(StringFieldInfo field) {
        return new FieldMetadata(field, "length is", null);
    }

    public FieldMetadata merge(FieldMetadata metadata) {
        if (equals(EMPTY)) {
            return metadata;
        }
        return new FieldMetadata(field, operator.readable(), metadata);
    }

    @Override
    public String readable() {
        return Stream.of(field, operator, value)
                        .filter(Objects::nonNull)
                        .map(Readable::readable)
                        .map(Objects::toString)
                        .collect(joining(" "));
    }

    @Override
    public void accept(MetadataVisitor visitor) {
        visitor.visit(this);
    }

}
