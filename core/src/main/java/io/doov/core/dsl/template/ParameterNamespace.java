/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.time.*;
import java.util.*;
import java.util.function.BiFunction;

import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.runtime.GenericModel;

public class ParameterNamespace {

    public static ParamProvider<Integer, IntegerFieldInfo> $Integer =
            (ns,name) -> ns.getOrCreate(Integer.class,(model,key) -> model.intField(0, key + name));

    public static ParamProvider<Long, LongFieldInfo> $Long =
            (ns,name) -> ns.getOrCreate(Long.class,(model,key) -> model.longField(0, key + name));

    public static ParamProvider<Character, CharacterFieldInfo> $Char =
            (ns,name) -> ns.getOrCreate(CharacterFieldInfo.class,(model,key) -> model.charField('$', key + name));

    public static ParamProvider<String, StringFieldInfo> $String =
            (ns,name) -> ns.getOrCreate(String.class,(model,key) -> model.stringField("", key + name));

    public static ParamProvider<Boolean, BooleanFieldInfo> $Boolean =
            (ns,name) -> ns.getOrCreate(Boolean.class,(model,key) -> model.booleanField(true,key + name));

    public static ParamProvider<Float, FloatFieldInfo> $Float =
            (ns,name) -> ns.getOrCreate(Float.class,(model,key) -> model.floatField(0,key + name));

    public static ParamProvider<Double, DoubleFieldInfo> $Double =
            (ns,name) -> ns.getOrCreate(Double.class,(model,key) -> model.doubleField(0,key + name));

    public static ParamProvider<LocalDate, LocalDateFieldInfo> $LocalDate =
            (ns,name) -> ns.getOrCreate(LocalDate.class,(model,key) -> model.localDateField(LocalDate.MIN,key + name));

    public static ParamProvider<LocalTime, LocalTimeFieldInfo> $LocalTime =
            (ns,name) -> ns.getOrCreate(LocalTime.class,(model,key) -> model.localTimeField(LocalTime.MIN,key + name));

    public static ParamProvider<LocalDateTime, LocalDateTimeFieldInfo> $LocalDateTime =
            (ns,name) -> ns.getOrCreate(LocalDate.class,(model,key) -> model.localDateTimeField(LocalDateTime.MIN,key + name));

    public static <T extends Enum<T>> ParamProvider<T, EnumFieldInfo<T>> $Enum(Class<T> cls) {
        return (ns,name) -> ns.getOrCreate(cls, (model, key) -> model.enumField(null,key + name));
    }

    public static <T,C extends Iterable<T>> ParamProvider<C, IterableFieldInfo<T,C>> $Iterable(Class<T> cls) {
        return (ns,name) -> ns.getOrCreate(cls, (model, key) -> model.iterableField(null,key + name));
    }

    private class TypedKey<T> {
        final String key;
        final Class<T> cls;

        TypedKey(String key, Class<T> cls) {
            this.key = key;
            this.cls = cls;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key,cls);
        }
    }

    private GenericModel namespace;
    private int paramCount;
    private Map<TypedKey, Object> index;


    ParameterNamespace() {
        this.namespace = new GenericModel();
        this.index = new HashMap<>();
        this.paramCount = 0;
    }

    private <K,T> T getOrCreate(Class<K> cls, BiFunction<GenericModel,String,T> generator) {
        TypedKey key = new TypedKey<>("$" + paramCount++ + ":", cls);
        if (index.containsKey(key)) {
            return (T) index.get(key);
        } else {
            T field = generator.apply(namespace,key.key);
            index.put(key,field);
            return field;
        }
    }

}
