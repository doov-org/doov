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
package io.doov.core.dsl.field;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;

import java.util.List;

public class FieldInfoProvider {

    public static <T> DefaultFieldInfoBuilder<T> defaultField() {
        return new DefaultFieldInfoBuilder<>();
    }

    public static StringFieldInfoBuilder stringField() {
        return new StringFieldInfoBuilder();
    }

    public static CharacterFieldInfoBuilder characterField() {
        return new CharacterFieldInfoBuilder();
    }

    public static BooleanFieldInfoBuilder booleanField() {
        return new BooleanFieldInfoBuilder();
    }

    public static IntegerFieldInfoBuilder integerField() {
        return new IntegerFieldInfoBuilder();
    }

    public static LongFieldInfoBuilder longField() {
        return new LongFieldInfoBuilder();
    }

    public static DoubleFieldInfoBuilder doubleField() {
        return new DoubleFieldInfoBuilder();
    }

    public static FloatFieldInfoBuilder floatField() {
        return new FloatFieldInfoBuilder();
    }

    public static DateIsoFieldInfoBuilder dateIsoField() {
        return new DateIsoFieldInfoBuilder();
    }

    public static TimeIsoFieldInfoBuilder timeIsoField() {
        return new TimeIsoFieldInfoBuilder();
    }

    public static LocalDateFieldInfoBuilder localDateField() {
        return new LocalDateFieldInfoBuilder();
    }

    public static LocalDateTimeFieldInfoBuilder localDateTimeField() {
        return new LocalDateTimeFieldInfoBuilder();
    }

    public static LocalTimeFieldInfoBuilder localTimeField() {
        return new LocalTimeFieldInfoBuilder();
    }

    public static <T extends Enum<T>> EnumFieldInfoBuilder<T> enumField() {
        return new EnumFieldInfoBuilder<>();
    }

    public static <T, C extends Iterable<T>> IterableFieldInfoBuilder<T, C> iterableField() {
        return new IterableFieldInfoBuilder<>();
    }

    @SuppressWarnings("unchecked")
    public static abstract class BaseFieldInfoBuilder<F extends DefaultFieldInfo<?>,
            B extends BaseFieldInfoBuilder<F, B>> {

        public FieldId fieldId;
        public String readable;
        public Class<?> type;
        public boolean _transient = false;
        public boolean codeValuable = false;
        public boolean codeLookup = false;
        public Class<?>[] genericTypes = new Class<?>[]{};
        public FieldId[] siblings = new FieldId[]{};

        public B fieldId(FieldId fieldId) {
            this.fieldId = fieldId;
            return (B) this;
        }

        public B readable(String readable) {
            this.readable = readable;
            return (B) this;
        }

        public B type(Class<?> type) {
            this.type = type;
            return (B) this;
        }

        public B _transient(boolean _transient) {
            this._transient = _transient;
            return (B) this;
        }

        public B codeValuable(boolean codeValuable) {
            this.codeValuable = codeValuable;
            return (B) this;
        }

        public B codeLookup(boolean codeLookup) {
            this.codeLookup = codeLookup;
            return (B) this;
        }

        public B genericTypes(Class<?>... genericTypes) {
            this.genericTypes = genericTypes;
            return (B) this;
        }

        public B siblings(FieldId... siblings) {
            this.siblings = siblings;
            return (B) this;
        }

        public F build(List<FieldInfo> allFields) {
            F build = build();
            allFields.add(build);
            return build;
        }

        public abstract F build();

    }

    public static class DefaultFieldInfoBuilder<T>
            extends BaseFieldInfoBuilder<DefaultFieldInfo<T>, DefaultFieldInfoBuilder<T>> {

        @Override
        public DefaultFieldInfo<T> build() {
            return new DefaultFieldInfo<>(fieldId, readable, type, _transient, codeValuable,
                    codeLookup, genericTypes, siblings);
        }

    }

    public static class BooleanFieldInfoBuilder
            extends BaseFieldInfoBuilder<BooleanFieldInfo, BooleanFieldInfoBuilder> {

        @Override
        public BooleanFieldInfo build() {
            return new BooleanFieldInfo(fieldId, readable, type, _transient, siblings);
        }

    }

    public static class StringFieldInfoBuilder
            extends BaseFieldInfoBuilder<StringFieldInfo, StringFieldInfoBuilder> {

        @Override
        public StringFieldInfo build() {
            return new StringFieldInfo(fieldId, readable, _transient, siblings);

        }

    }

    public static class CharacterFieldInfoBuilder
            extends BaseFieldInfoBuilder<CharacterFieldInfo, CharacterFieldInfoBuilder> {

        @Override
        public CharacterFieldInfo build() {
            return new CharacterFieldInfo(fieldId, readable, type, _transient, siblings);

        }

    }

    public static class DoubleFieldInfoBuilder
            extends BaseFieldInfoBuilder<DoubleFieldInfo, DoubleFieldInfoBuilder> {

        @Override
        public DoubleFieldInfo build() {
            return new DoubleFieldInfo(fieldId, readable, type, _transient, siblings);

        }

    }

    public static class FloatFieldInfoBuilder
            extends BaseFieldInfoBuilder<FloatFieldInfo, FloatFieldInfoBuilder> {

        @Override
        public FloatFieldInfo build() {
            return new FloatFieldInfo(fieldId, readable, type, _transient, siblings);

        }

    }

    public static class IntegerFieldInfoBuilder
            extends BaseFieldInfoBuilder<IntegerFieldInfo, IntegerFieldInfoBuilder> {

        @Override
        public IntegerFieldInfo build() {
            return new IntegerFieldInfo(fieldId, readable, type, _transient, siblings);

        }

    }

    public static class LongFieldInfoBuilder
            extends BaseFieldInfoBuilder<LongFieldInfo, LongFieldInfoBuilder> {

        @Override
        public LongFieldInfo build() {
            return new LongFieldInfo(fieldId, readable, type, _transient, siblings);
        }

    }

    public static class DateIsoFieldInfoBuilder
            extends BaseFieldInfoBuilder<DateIsoFieldInfo, DateIsoFieldInfoBuilder> {

        @Override
        public DateIsoFieldInfo build() {
            return new DateIsoFieldInfo(fieldId, readable, _transient, siblings);

        }

    }

    public static class TimeIsoFieldInfoBuilder
            extends BaseFieldInfoBuilder<TimeIsoFieldInfo, TimeIsoFieldInfoBuilder> {

        @Override
        public TimeIsoFieldInfo build() {
            return new TimeIsoFieldInfo(fieldId, readable, _transient, siblings);

        }

    }

    public static class LocalDateFieldInfoBuilder
            extends BaseFieldInfoBuilder<LocalDateFieldInfo, LocalDateFieldInfoBuilder> {

        @Override
        public LocalDateFieldInfo build() {
            return new LocalDateFieldInfo(fieldId, readable, _transient, siblings);

        }

    }

    public static class LocalDateTimeFieldInfoBuilder
            extends BaseFieldInfoBuilder<LocalDateTimeFieldInfo, LocalDateTimeFieldInfoBuilder> {

        @Override
        public LocalDateTimeFieldInfo build() {
            return new LocalDateTimeFieldInfo(fieldId, readable, _transient, siblings);

        }

    }

    public static class LocalTimeFieldInfoBuilder
            extends BaseFieldInfoBuilder<LocalTimeFieldInfo, LocalTimeFieldInfoBuilder> {

        @Override
        public LocalTimeFieldInfo build() {
            return new LocalTimeFieldInfo(fieldId, readable, _transient, siblings);

        }

    }

    public static class EnumFieldInfoBuilder<T extends Enum<T>>
            extends BaseFieldInfoBuilder<EnumFieldInfo<T>, EnumFieldInfoBuilder<T>> {

        @Override
        public EnumFieldInfo<T> build() {
            return new EnumFieldInfo<>(fieldId, readable, type, _transient, codeValuable, codeLookup, siblings);
        }

    }

    public static class IterableFieldInfoBuilder<T, C extends Iterable<T>>
            extends BaseFieldInfoBuilder<IterableFieldInfo<T, C>, IterableFieldInfoBuilder<T, C>> {

        @Override
        public IterableFieldInfo<T, C> build() {
            return new IterableFieldInfo<>(fieldId, readable, type, _transient, genericTypes, siblings);
        }

    }
}
