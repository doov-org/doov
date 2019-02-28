/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import io.doov.core.dsl.field.types.*;

import java.util.function.Supplier;

public class ParameterTypes {

    public static Supplier<TemplateParam<BooleanFieldInfo>> $Boolean = () ->
            new TemplateParam<>(BooleanFieldInfo.class, "$Boolean", BooleanFieldInfo::new);

    public static Supplier<TemplateParam<CharacterFieldInfo>> $Char = () ->
            new TemplateParam<>(CharacterFieldInfo.class, "$Char", CharacterFieldInfo::new);

    public static Supplier<TemplateParam<IntegerFieldInfo>> $Integer = () ->
            new TemplateParam<>(IntegerFieldInfo.class, "$Integer", IntegerFieldInfo::new);

    public static Supplier<TemplateParam<DoubleFieldInfo>> $Double = () ->
            new TemplateParam<>(DoubleFieldInfo.class, "$Double", DoubleFieldInfo::new);

    public static Supplier<TemplateParam<FloatFieldInfo>> $Float = () ->
            new TemplateParam<>(FloatFieldInfo.class, "$Float", FloatFieldInfo::new);

    public static Supplier<TemplateParam<StringFieldInfo>> $String = () ->
            new TemplateParam<>(StringFieldInfo.class, "$String", StringFieldInfo::new);

    public static Supplier<TemplateParam<LocalDateFieldInfo>> $LocalDate = () ->
            new TemplateParam<>(LocalDateFieldInfo.class, "$LocalDate", LocalDateFieldInfo::new);

    public static Supplier<TemplateParam<LocalDateTimeFieldInfo>> $LocalDateTime = () ->
            new TemplateParam<>(LocalDateTimeFieldInfo.class, "$LocalDateTime", LocalDateTimeFieldInfo::new);

    public static Supplier<TemplateParam<LocalTimeFieldInfo>> $LocalTime = () ->
            new TemplateParam<>(LocalTimeFieldInfo.class, "$LocalTime", LocalTimeFieldInfo::new);

    public static Supplier<TemplateParam<LongFieldInfo>> $Long = () ->
            new TemplateParam<>(LongFieldInfo.class, "$Long", LongFieldInfo::new);


    public static <T extends Enum<T>> Supplier<TemplateParam<EnumFieldInfo<T>>> $Enum(Class<T> cls) {
        return () ->
                new TemplateParam<>(TemplateParam.generify(EnumFieldInfo.class), "$Enum(" + cls + ")", EnumFieldInfo::new);
    }

    public static <T, C extends Iterable<T>> Supplier<TemplateParam<IterableFieldInfo<T, C>>> $Iterable(Class<T> cls) {
        return () ->
                new TemplateParam<>(TemplateParam.generify(IterableFieldInfo.class), "$Iterable(" + cls + ")", IterableFieldInfo::new);
    }
}
