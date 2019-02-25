/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.time.*;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.runtime.GenericModel;

public class TemplateParam<T extends DslField<?>> {

    public static TemplateParam<IntegerFieldInfo> $Integer = new TemplateParam<>(IntegerFieldInfo.class,
            model -> model.intField(0,"$Integer"));

    public static TemplateParam<LongFieldInfo> $Long = new TemplateParam<>(LongFieldInfo.class,
            model -> model.longField(0,"$Long"));

    public static TemplateParam<CharacterFieldInfo> $Char = new TemplateParam<>(CharacterFieldInfo.class,
            model -> model.charField('$',"$Char"));

    public static TemplateParam<StringFieldInfo> $String = new TemplateParam<>(StringFieldInfo.class,
            model -> model.stringField("","$String"));

    public static TemplateParam<BooleanFieldInfo> $Boolean = new TemplateParam<>(BooleanFieldInfo.class,
            model -> model.booleanField(false,"$Boolean"));

    public static TemplateParam<FloatFieldInfo> $Float = new TemplateParam<>(FloatFieldInfo.class,
            model -> model.floatField(0,"$Float"));

    public static TemplateParam<DoubleFieldInfo> $Double = new TemplateParam<>(DoubleFieldInfo.class,
            model -> model.doubleField(0,"$Double"));

    public static TemplateParam<LocalDateFieldInfo> $LocalDate = new TemplateParam<>(LocalDateFieldInfo.class,
            model -> model.localDateField(LocalDate.MIN,"$LocalDate"));

    public static TemplateParam<LocalTimeFieldInfo> $LocalTime = new TemplateParam<>(LocalTimeFieldInfo.class,
            model -> model.localTimeField(LocalTime.MIN,"$LocalTime"));

    public static TemplateParam<LocalDateTimeFieldInfo> $LocalDateTime = new TemplateParam<>(LocalDateTimeFieldInfo.class,
            model -> model.localDateTimeField(LocalDateTime.MIN,"$LocalDateTime"));

    public static <T extends Enum<T>> TemplateParam<EnumFieldInfo<T>> $Enum(Class<T> cls) {
        EnumFieldInfo<T> ph = new GenericModel().enumField(null, "");
        return new TemplateParam<>((Class<EnumFieldInfo<T>>) ph.getClass(),
                model -> model.enumField(null,"$Enum(" + cls + ")"));
    }

    public static <T, C extends Iterable<T>> TemplateParam<IterableFieldInfo<T, C>> $Iterable(Class<T> cls) {
        IterableFieldInfo<T, C> ph = new GenericModel().iterableField(null, "");
        return new TemplateParam<>((Class<IterableFieldInfo<T, C>>) ph.getClass(),
                model -> model.iterableField(null,"$Iterable(" + cls + ")"));
    }

    public final Class<T> type;
    public final Function<GenericModel,T> generator;

    public TemplateParam(Class<T> type, Function<GenericModel, T> generator) {
        this.type = type;
        this.generator = generator;
    }

}
