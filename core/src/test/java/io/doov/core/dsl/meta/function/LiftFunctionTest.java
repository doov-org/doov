/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.DOOV.lift;
import static io.doov.core.dsl.DOOV.map;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.impl.base.StringFunction;
import io.doov.core.dsl.impl.num.IntegerFunction;
import io.doov.core.dsl.impl.num.LongFunction;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.runtime.GenericModel;

class LiftFunctionTest {

    private GenericModel model;

    @Test
    void lift_long() {
        this.model = new GenericModel();
        LongFieldInfo longField = model.longField(2L, "long field");
        assertThat(model.get(longField)).isEqualTo(2L);
        MappingRule rule = map(lift(1L, LongFunction::new)).to(longField);
        rule.executeOn(model);
        assertThat(model.get(longField)).isEqualTo(1L);
        System.out.println(rule.metadata().readable());
    }

    @Test
    void lift_integer() {
        this.model = new GenericModel();
        IntegerFieldInfo intField = model.intField(2, "int field");
        assertThat(model.get(intField)).isEqualTo(2);
        MappingRule rule = map(lift(1, IntegerFunction::new)).to(intField);
        rule.executeOn(model);
        assertThat(model.get(intField)).isEqualTo(1);
        System.out.println(rule.metadata().readable());
    }

    @Test
    void lift_string() {
        this.model = new GenericModel();
        StringFieldInfo stringField = model.stringField(null, "int field");
        assertThat(model.get(stringField)).isNull();
        MappingRule rule = map(lift("new string", StringFunction::new)).to(stringField);
        rule.executeOn(model);
        assertThat(model.get(stringField)).isEqualTo("new string");
        System.out.println(rule.metadata().readable());
    }

    @Test
    void lift_list() {
        this.model = new GenericModel();
        ArrayList<Boolean> objects = new ArrayList<>();
        IterableFieldInfo<Boolean, List<Boolean>> iterableField = model.iterableField(objects, "iterable field");
        assertThat(model.get(iterableField)).isEmpty();
        MappingRule rule = map(lift(true, false, true)).to(iterableField);
        rule.executeOn(model);
        assertThat(model.get(iterableField)).containsExactly(true, false, true);
        System.out.println(rule.metadata().readable());
    }
}
