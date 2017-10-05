/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import java.util.List;

import org.modelmap.core.FieldId;
import org.modelmap.core.FieldInfo;

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

    @SuppressWarnings("unchecked")
    protected static abstract class BaseFieldInfoBuilder<F extends DefaultFieldInfo<?>,
                    B extends BaseFieldInfoBuilder<F, B>> {

        protected FieldId fieldId;
        protected Class<?> type;
        protected Class<?>[] genericTypes = new Class<?>[] {};
        protected FieldId[] siblings = new FieldId[] {};

        public B fieldId(FieldId fieldId) {
            this.fieldId = fieldId;
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
            DefaultFieldInfo<T> info = new DefaultFieldInfo<>(fieldId, type, genericTypes, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class BooleanFieldInfoBuilder
                    extends BaseFieldInfoBuilder<BooleanFieldInfo, BooleanFieldInfoBuilder> {

        @Override
        public BooleanFieldInfo build(List<FieldInfo> allFields) {
            BooleanFieldInfo info = new BooleanFieldInfo(fieldId, type, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class StringFieldInfoBuilder
                    extends BaseFieldInfoBuilder<StringFieldInfo, StringFieldInfoBuilder> {

        @Override
        public StringFieldInfo build(List<FieldInfo> allFields) {
            StringFieldInfo info = new StringFieldInfo(fieldId, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class CharacterFieldInfoBuilder
                    extends BaseFieldInfoBuilder<CharacterFieldInfo, CharacterFieldInfoBuilder> {

        @Override
        public CharacterFieldInfo build(List<FieldInfo> allFields) {
            CharacterFieldInfo info = new CharacterFieldInfo(fieldId, type, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class DoubleFieldInfoBuilder
                    extends BaseFieldInfoBuilder<DoubleFieldInfo, DoubleFieldInfoBuilder> {

        @Override
        public DoubleFieldInfo build(List<FieldInfo> allFields) {
            DoubleFieldInfo info = new DoubleFieldInfo(fieldId, type, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class FloatFieldInfoBuilder
                    extends BaseFieldInfoBuilder<FloatFieldInfo, FloatFieldInfoBuilder> {

        @Override
        public FloatFieldInfo build(List<FieldInfo> allFields) {
            FloatFieldInfo info = new FloatFieldInfo(fieldId, type, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class IntegerFieldInfoBuilder
                    extends BaseFieldInfoBuilder<IntegerFieldInfo, IntegerFieldInfoBuilder> {

        @Override
        public IntegerFieldInfo build(List<FieldInfo> allFields) {
            IntegerFieldInfo info = new IntegerFieldInfo(fieldId, type, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class LongFieldInfoBuilder
                    extends BaseFieldInfoBuilder<LongFieldInfo, LongFieldInfoBuilder> {

        @Override
        public LongFieldInfo build(List<FieldInfo> allFields) {
            LongFieldInfo info = new LongFieldInfo(fieldId, type, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class LocalDateFieldInfoBuilder
                    extends BaseFieldInfoBuilder<LocalDateFieldInfo, LocalDateFieldInfoBuilder> {

        @Override
        public LocalDateFieldInfo build(List<FieldInfo> allFields) {
            LocalDateFieldInfo info = new LocalDateFieldInfo(fieldId, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class LocalDateTimeFieldInfoBuilder
                    extends BaseFieldInfoBuilder<LocalDateTimeFieldInfo, LocalDateTimeFieldInfoBuilder> {

        @Override
        public LocalDateTimeFieldInfo build(List<FieldInfo> allFields) {
            LocalDateTimeFieldInfo info = new LocalDateTimeFieldInfo(fieldId, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class LocalTimeFieldInfoBuilder
                    extends BaseFieldInfoBuilder<LocalTimeFieldInfo, LocalTimeFieldInfoBuilder> {

        @Override
        public LocalTimeFieldInfo build(List<FieldInfo> allFields) {
            LocalTimeFieldInfo info = new LocalTimeFieldInfo(fieldId, siblings);
            allFields.add(info);
            return info;
        }
    }

    protected static class EnumFieldInfoBuilder<T extends Enum<T>>
                    extends BaseFieldInfoBuilder<EnumFieldInfo<T>, EnumFieldInfoBuilder<T>> {

        @Override
        public EnumFieldInfo<T> build(List<FieldInfo> allFields) {
            EnumFieldInfo<T> info = new EnumFieldInfo<>(fieldId, type, siblings);
            allFields.add(info);
            return info;
        }
    }
}
