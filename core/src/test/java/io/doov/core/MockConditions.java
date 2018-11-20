/*
 * Copyright 2018 Courtanet
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
package io.doov.core;

import static io.doov.core.dsl.runtime.FieldChainBuilder.from;

import java.time.*;
import java.util.Arrays;
import java.util.List;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.impl.DefaultStepCondition;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;
import io.doov.core.dsl.runtime.*;

class MockConditions {

    private static RuntimeFieldRegistry FIELDS = new RuntimeFieldRegistry(Arrays.asList());

    public static StepCondition trueCondition(String readable) {
        return new DefaultStepCondition(LeafPredicateMetadata.trueMetadata().valueReadable(() -> readable), (model, context) -> true);
    }

    public static StepCondition falseCondition(String readable) {
        return new DefaultStepCondition(LeafPredicateMetadata.trueMetadata().valueReadable(() -> readable), (model, context) -> false);
    }

    private static <T> RuntimeField<Object, T> runtimeModel(T value, String readable) {
        RuntimeField<Object, T> field = from(Object.class, () -> readable).readable(readable).field(o -> value, (o, v) -> { }, (Class<T>) value.getClass());
        List<RuntimeField> list = FIELDS.runtimeFields();
        list.add(field);
        FIELDS = new RuntimeFieldRegistry(list);
        return field;
    }

    public static BooleanFieldInfo booleanField(boolean value, String readable) {
        return new BooleanFieldInfo(runtimeModel(value, readable));
    }

    public static CharacterFieldInfo charField(char value, String readable) {
        return new CharacterFieldInfo(runtimeModel(value, readable));
    }

    public static DoubleFieldInfo doubleField(double value, String readable) {
        return new DoubleFieldInfo(runtimeModel(value, readable));
    }

    public static <E extends Enum<E>> EnumFieldInfo<E> enumField(E value, String readable) {
        return new EnumFieldInfo<>(runtimeModel(value, readable));
    }

    public static FloatFieldInfo floatField(float value, String readable) {
        return new FloatFieldInfo(runtimeModel(value, readable));
    }

    public static IntegerFieldInfo intField(int value, String readable) {
        return new IntegerFieldInfo(runtimeModel(value, readable));
    }

    public static LocalDateFieldInfo localDateField(LocalDate value, String readable) {
        return new LocalDateFieldInfo(runtimeModel(value, readable));
    }

    public static LocalDateTimeFieldInfo lodalDateTimeField(LocalDateTime value, String readable) {
        return new LocalDateTimeFieldInfo(runtimeModel(value, readable));
    }

    public static LocalTimeFieldInfo localTimeField(LocalTime value, String readable) {
        return new LocalTimeFieldInfo(runtimeModel(value, readable));
    }

    public static LongFieldInfo longField(long value, String readable) {
        return new LongFieldInfo(runtimeModel(value, readable));
    }

    public static StringFieldInfo stringField(String value, String readable) {
        return new StringFieldInfo(runtimeModel(value, readable));
    }

    public static <T, C extends Iterable<T>> IterableFieldInfo<T, C> iterableField(C value, String readable) {
        return new IterableFieldInfo<>(runtimeModel(value, readable));
    }

    public static DslModel model() {
        return new RuntimeModel<>(FIELDS, new Object());
    }
}
