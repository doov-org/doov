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

import java.util.Collection;
import java.util.List;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;

public class FieldInfoProvider {

    protected static <T> DefaultFieldInfoBuilder<T> defaultField() {
        return new DefaultFieldInfoBuilder<>();
    }

    protected static StringFieldInfoBuilder stringField() {
        return new StringFieldInfoBuilder();
    }

    protected static CharacterFieldInfoBuilder characterField() {
        return new CharacterFieldInfoBuilder();
    }

    protected static BooleanFieldInfoBuilder booleanField() {
        return new BooleanFieldInfoBuilder();
    }

    protected static IntegerFieldInfoBuilder integerField() {
        return new IntegerFieldInfoBuilder();
    }

    protected static LongFieldInfoBuilder longField() {
        return new LongFieldInfoBuilder();
    }

    protected static DoubleFieldInfoBuilder doubleField() {
        return new DoubleFieldInfoBuilder();
    }

    protected static FloatFieldInfoBuilder floatField() {
        return new FloatFieldInfoBuilder();
    }

    protected static LocalDateFieldInfoBuilder localDateField() {
        return new LocalDateFieldInfoBuilder();
    }

    protected static LocalDateTimeFieldInfoBuilder localDateTimeField() {
        return new LocalDateTimeFieldInfoBuilder();
    }

    protected static LocalTimeFieldInfoBuilder localTimeField() {
        return new LocalTimeFieldInfoBuilder();
    }

    protected static <T extends Enum<T>> EnumFieldInfoBuilder<T> enumField() {
        return new EnumFieldInfoBuilder<>();
    }

    protected static <T, C extends Collection<T>> CollectionFieldInfoBuilder<T, C> collectionField() {
        return new CollectionFieldInfoBuilder<>();
    }

    @SuppressWarnings("unchecked")
    protected static abstract class BaseFieldInfoBuilder<F extends DefaultFieldInfo<?>, B extends BaseFieldInfoBuilder<F, B>> {

        protected FieldId fieldId;
        protected String readable;
        protected Class<?> type;
        protected Class<?>[] genericTypes = new Class<?>[] {};
        protected FieldId[] siblings = new FieldId[] {};

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

        public B genericTypes(Class<?>... genericTypes) {
            this.genericTypes = genericTypes;
            return (B) this;
        }

        public B siblings(FieldId... siblings) {
            this.siblings = siblings;
            return (B) this;
        }

        public abstract F build(List<FieldInfo> allFields);
    }

    protected static class DefaultFieldInfoBuilder<T>
                    extends BaseFieldInfoBuilder<DefaultFieldInfo<T>, DefaultFieldInfoBuilder<T>> {

        @Override
        public DefaultFieldInfo<T> build(List<FieldInfo> allFields) {
            DefaultFieldInfo<T> info = new DefaultFieldInfo<>(fieldId, readable, type, genericTypes, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class BooleanFieldInfoBuilder
                    extends BaseFieldInfoBuilder<BooleanFieldInfo, BooleanFieldInfoBuilder> {

        @Override
        public BooleanFieldInfo build(List<FieldInfo> allFields) {
            BooleanFieldInfo info = new BooleanFieldInfo(fieldId, readable, type, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class StringFieldInfoBuilder
                    extends BaseFieldInfoBuilder<StringFieldInfo, StringFieldInfoBuilder> {

        @Override
        public StringFieldInfo build(List<FieldInfo> allFields) {
            StringFieldInfo info = new StringFieldInfo(fieldId, readable, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class CharacterFieldInfoBuilder
                    extends BaseFieldInfoBuilder<CharacterFieldInfo, CharacterFieldInfoBuilder> {

        @Override
        public CharacterFieldInfo build(List<FieldInfo> allFields) {
            CharacterFieldInfo info = new CharacterFieldInfo(fieldId, readable, type, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class DoubleFieldInfoBuilder
                    extends BaseFieldInfoBuilder<DoubleFieldInfo, DoubleFieldInfoBuilder> {

        @Override
        public DoubleFieldInfo build(List<FieldInfo> allFields) {
            DoubleFieldInfo info = new DoubleFieldInfo(fieldId, readable, type, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class FloatFieldInfoBuilder
                    extends BaseFieldInfoBuilder<FloatFieldInfo, FloatFieldInfoBuilder> {

        @Override
        public FloatFieldInfo build(List<FieldInfo> allFields) {
            FloatFieldInfo info = new FloatFieldInfo(fieldId, readable, type, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class IntegerFieldInfoBuilder
                    extends BaseFieldInfoBuilder<IntegerFieldInfo, IntegerFieldInfoBuilder> {

        @Override
        public IntegerFieldInfo build(List<FieldInfo> allFields) {
            IntegerFieldInfo info = new IntegerFieldInfo(fieldId, readable, type, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class LongFieldInfoBuilder
                    extends BaseFieldInfoBuilder<LongFieldInfo, LongFieldInfoBuilder> {

        @Override
        public LongFieldInfo build(List<FieldInfo> allFields) {
            LongFieldInfo info = new LongFieldInfo(fieldId, readable, type, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class LocalDateFieldInfoBuilder
                    extends BaseFieldInfoBuilder<LocalDateFieldInfo, LocalDateFieldInfoBuilder> {

        @Override
        public LocalDateFieldInfo build(List<FieldInfo> allFields) {
            LocalDateFieldInfo info = new LocalDateFieldInfo(fieldId, readable, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class LocalDateTimeFieldInfoBuilder
                    extends BaseFieldInfoBuilder<LocalDateTimeFieldInfo, LocalDateTimeFieldInfoBuilder> {

        @Override
        public LocalDateTimeFieldInfo build(List<FieldInfo> allFields) {
            LocalDateTimeFieldInfo info = new LocalDateTimeFieldInfo(fieldId, readable, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class LocalTimeFieldInfoBuilder
                    extends BaseFieldInfoBuilder<LocalTimeFieldInfo, LocalTimeFieldInfoBuilder> {

        @Override
        public LocalTimeFieldInfo build(List<FieldInfo> allFields) {
            LocalTimeFieldInfo info = new LocalTimeFieldInfo(fieldId, readable, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class EnumFieldInfoBuilder<T extends Enum<T>>
                    extends BaseFieldInfoBuilder<EnumFieldInfo<T>, EnumFieldInfoBuilder<T>> {

        @Override
        public EnumFieldInfo<T> build(List<FieldInfo> allFields) {
            EnumFieldInfo<T> info = new EnumFieldInfo<>(fieldId, readable, type, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class CollectionFieldInfoBuilder<T, C extends Collection<T>>
                    extends BaseFieldInfoBuilder<CollectionFieldInfo<T, C>, CollectionFieldInfoBuilder<T, C>> {
        @Override
        public CollectionFieldInfo<T, C> build(List<FieldInfo> allFields) {
            CollectionFieldInfo<T, C> info = new CollectionFieldInfo<>(fieldId, readable, type, genericTypes, siblings);
            allFields.add(info);
            return info;
        }

    }
}
