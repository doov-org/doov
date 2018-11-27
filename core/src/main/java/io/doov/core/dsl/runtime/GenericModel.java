package io.doov.core.dsl.runtime;

import static io.doov.core.dsl.runtime.FieldChainBuilder.from;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.doov.core.FieldId;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.types.BooleanFieldInfo;
import io.doov.core.dsl.field.types.CharacterFieldInfo;
import io.doov.core.dsl.field.types.DoubleFieldInfo;
import io.doov.core.dsl.field.types.EnumFieldInfo;
import io.doov.core.dsl.field.types.FloatFieldInfo;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.IterableFieldInfo;
import io.doov.core.dsl.field.types.LocalDateFieldInfo;
import io.doov.core.dsl.field.types.LocalDateTimeFieldInfo;
import io.doov.core.dsl.field.types.LocalTimeFieldInfo;
import io.doov.core.dsl.field.types.LongFieldInfo;
import io.doov.core.dsl.field.types.StringFieldInfo;

public final class GenericModel implements DslModel {

    private final List<RuntimeField<GenericModel, Object>> fields;
    private final HashMap<FieldId, Object> valueMap;

    public GenericModel() {
        this.fields = new ArrayList<>();
        this.valueMap = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(FieldId id) {
        return (T) valueMap.get(id);
    }

    @Override
    public <T> void set(FieldId id, T value) {
        valueMap.put(id, value);
    }

    @SuppressWarnings("unchecked")
    private <T> RuntimeField<GenericModel, T> runtimeField(T value, String readable) {
        FieldId fieldId = () -> readable;
        this.set(fieldId, value);
        return from(GenericModel.class, fieldId)
                .readable(readable)
                .field(o -> o.get(fieldId), (o, v) -> o.set(fieldId, v), (Class<T>) (value == null ?  Void.TYPE : value.getClass()))
                .register(fields);
    }

    public BooleanFieldInfo booleanField(boolean value, String readable) {
        return new BooleanFieldInfo(runtimeField(value, readable));
    }

    public CharacterFieldInfo charField(char value, String readable) {
        return new CharacterFieldInfo(runtimeField(value, readable));
    }

    public DoubleFieldInfo doubleField(double value, String readable) {
        return new DoubleFieldInfo(runtimeField(value, readable));
    }

    public <E extends Enum<E>> EnumFieldInfo<E> enumField(E value, String readable) {
        return new EnumFieldInfo<>(runtimeField(value, readable));
    }

    public FloatFieldInfo floatField(float value, String readable) {
        return new FloatFieldInfo(runtimeField(value, readable));
    }

    public IntegerFieldInfo intField(int value, String readable) {
        return new IntegerFieldInfo(runtimeField(value, readable));
    }

    public LocalDateFieldInfo localDateField(LocalDate value, String readable) {
        return new LocalDateFieldInfo(runtimeField(value, readable));
    }

    public LocalDateTimeFieldInfo localDateTimeField(LocalDateTime value, String readable) {
        return new LocalDateTimeFieldInfo(runtimeField(value, readable));
    }

    public LocalTimeFieldInfo localTimeField(LocalTime value, String readable) {
        return new LocalTimeFieldInfo(runtimeField(value, readable));
    }

    public LongFieldInfo longField(long value, String readable) {
        return new LongFieldInfo(runtimeField(value, readable));
    }

    public StringFieldInfo stringField(String value, String readable) {
        return new StringFieldInfo(runtimeField(value, readable));
    }

    public <T, C extends Iterable<T>> IterableFieldInfo<T, C> iterableField(C value, String readable) {
        return new IterableFieldInfo<>(runtimeField(value, readable));
    }

}
