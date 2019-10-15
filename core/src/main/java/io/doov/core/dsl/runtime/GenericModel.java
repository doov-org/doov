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
package io.doov.core.dsl.runtime;

import static io.doov.core.dsl.runtime.FieldChainBuilder.from;

import java.time.*;
import java.util.*;
import java.util.stream.Stream;

import io.doov.core.*;
import io.doov.core.dsl.field.types.*;
import io.doov.core.serial.TypeAdapterRegistry;
import io.doov.core.serial.TypeAdapters;

public final class GenericModel implements FieldModel {

    private final List<RuntimeField<GenericModel, Object>> fields;
    private final Map<FieldId, Object> valueMap;

    private final TypeAdapterRegistry adapterRegistry;

    public GenericModel() {
        this(TypeAdapters.INSTANCE);
    }

    public GenericModel(TypeAdapterRegistry adapterRegistry) {
        this.fields = new ArrayList<>();
        this.valueMap = new HashMap<>();
        this.adapterRegistry = adapterRegistry;
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

    @Override
    public Stream<Map.Entry<FieldId, Object>> stream() {
        return valueMap.entrySet().stream();
    }

    @Override
    public Iterator<Map.Entry<FieldId, Object>> iterator() {
        return valueMap.entrySet().iterator();
    }

    @Override
    public Spliterator<Map.Entry<FieldId, Object>> spliterator() {
        return valueMap.entrySet().spliterator();
    }

    @Override
    public Stream<Map.Entry<FieldId, Object>> parallelStream() {
        return valueMap.entrySet().parallelStream();
    }

    @Override
    public List<FieldInfo> getFieldInfos() {
        return new ArrayList<>(fields);
    }

    @Override
    public TypeAdapterRegistry getTypeAdapterRegistry() {
        return adapterRegistry;
    }

    @SuppressWarnings("unchecked")
    private <T> RuntimeField<GenericModel, T> runtimeField(T value, String fieldName, Class<?>... genericTypes) {
        FieldId fieldId = () -> fieldName;
        this.set(fieldId, value);
        return from(GenericModel.class, fieldId)
                .readable(fieldName)
                .field(o -> o.get(fieldId), (o, v) -> o.set(fieldId, v),
                        (Class<T>) (value == null ? Void.TYPE : value.getClass()), genericTypes)
                .register(fields);
    }

    public BooleanFieldInfo booleanField(boolean value, String fieldName) {
        return new BooleanFieldInfo(runtimeField(value, fieldName));
    }

    public CharacterFieldInfo charField(char value, String fieldName) {
        return new CharacterFieldInfo(runtimeField(value, fieldName));
    }

    public DoubleFieldInfo doubleField(double value, String fieldName) {
        return new DoubleFieldInfo(runtimeField(value, fieldName));
    }

    public <E extends Enum<E>> EnumFieldInfo<E> enumField(E value, String fieldName) {
        return new EnumFieldInfo<>(runtimeField(value, fieldName));
    }

    public FloatFieldInfo floatField(float value, String fieldName) {
        return new FloatFieldInfo(runtimeField(value, fieldName));
    }

    public IntegerFieldInfo intField(int value, String fieldName) {
        return new IntegerFieldInfo(runtimeField(value, fieldName));
    }

    public LocalDateFieldInfo localDateField(LocalDate value, String fieldName) {
        return new LocalDateFieldInfo(runtimeField(value, fieldName));
    }

    public LocalDateTimeFieldInfo localDateTimeField(LocalDateTime value, String fieldName) {
        return new LocalDateTimeFieldInfo(runtimeField(value, fieldName));
    }

    public LocalTimeFieldInfo localTimeField(LocalTime value, String fieldName) {
        return new LocalTimeFieldInfo(runtimeField(value, fieldName));
    }

    public LongFieldInfo longField(long value, String fieldName) {
        return new LongFieldInfo(runtimeField(value, fieldName));
    }

    public StringFieldInfo stringField(String value, String fieldName) {
        return new StringFieldInfo(runtimeField(value, fieldName));
    }

    public <T, C extends Iterable<T>> IterableFieldInfo<T, C> iterableField(C value, String fieldName, Class<?>... genericTypes) {
        return new IterableFieldInfo<>(runtimeField(value, fieldName, genericTypes));
    }

}
