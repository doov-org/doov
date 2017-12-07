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
package io.doov.sample.model;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.doov.core.FieldInfo;
import io.doov.sample.field.SampleFieldIdInfo;
import io.doov.sample.field.SampleTag;

public class SampleModelWrapperTest {

    private static Collection<Object[]> data() {
        return SampleFieldIdInfo.values().stream()
                        .map(f -> new Object[] { f.id().name(), f })
                        .collect(toList());
    }

    private SampleModelWrapper wrapper;

    @BeforeEach
    public void before() {
        wrapper = new SampleModelWrapper();
    }

    @ParameterizedTest
    @MethodSource("data")
    public void should_contains_all_field_info(String name, FieldInfo field) {
        assertThat(wrapper.getFieldInfos()).contains(field);
        assertThat(wrapper.getFieldIds()).contains(field.id());
    }

    @ParameterizedTest
    @MethodSource("data")
    public void should_be_null_when_clear_all(String name, FieldInfo field) {
        if (field.type().isPrimitive()) {
            return;
        }
        wrapper.clear();
        assertValueNull(field);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void should_be_null_when_clear_tag(String name, FieldInfo field) {
        if (field.type().isPrimitive()) {
            return;
        }
        if (field.id().tags().isEmpty()) {
            return;
        }
        wrapper.clear(field.id().tags().get(0));
        assertValueNull(field);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void should_not_throw_NPE_when_null_value_set(String name, FieldInfo field) {
        wrapper.set(field.id(), null);
    }

    @ParameterizedTest
    @MethodSource("data")
    public <T> void should_return_same_value_when_updated(String name, FieldInfo field) throws Exception {
        Object value = value(field);
        wrapper.set(field.id(), value);

        if (field.id().hasTag(SampleTag.READ_ONLY)) {
            assertThat(wrapper.<T> get(field.id())).isNull();
        } else {
            assertThat(wrapper.<T> get(field.id())).isEqualTo(value);
        }
    }

    private void assertValueNull(FieldInfo field) {
        Object value = wrapper.get(field.id());
        if (Number.class.isAssignableFrom(field.type())) {
            assertThat(((Number) value).longValue()).isEqualTo(0);
        } else {
            assertThat(value).isNull();
        }
    }

    private static Object value(FieldInfo field) throws IllegalAccessException, InstantiationException {
        if (field.type().equals(Long.class) || field.type().equals(Long.TYPE)) {
            return Long.MAX_VALUE;
        } else if (field.type().equals(Integer.class) || field.type().equals(Integer.TYPE)) {
            return Integer.MAX_VALUE;
        } else if (field.type().equals(Short.class) || field.type().equals(Short.TYPE)) {
            return Short.MAX_VALUE;
        } else if (field.type().equals(Double.class) || field.type().equals(Double.TYPE)) {
            return Double.MAX_VALUE;
        } else if (field.type().equals(Byte.class) || field.type().equals(Byte.TYPE)) {
            return Byte.MAX_VALUE;
        } else if (field.type().equals(Boolean.class) || field.type().equals(Boolean.TYPE)) {
            return Boolean.FALSE;
        } else if (field.type().isEnum()) {
            return field.type().getEnumConstants()[0];
        } else if (Collection.class.isAssignableFrom(field.type())) {
            return new ArrayList<>();
        } else if (field.type().equals(LocalDate.class)) {
            return LocalDate.now();
        } else if (field.type().equals(String.class)) {
            return "foo";
        }
        return field.type().newInstance();
    }

}
