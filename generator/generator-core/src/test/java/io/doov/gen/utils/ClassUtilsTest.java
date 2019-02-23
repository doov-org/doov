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
package io.doov.gen.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class ClassUtilsTest {

    @Test
    void should_return_methods_when_called_with_object() {
        Method getter = ClassUtils.getReferencedMethod(Sample.class, Sample::getProperty);
        assertThat(getter.getDeclaringClass()).isEqualTo(Sample.class);
        assertThat(getter.getName()).isEqualTo("getProperty");

        Method setter = ClassUtils.getReferencedMethod(Sample.class, Sample::setProperty);
        assertThat(setter.getDeclaringClass()).isEqualTo(Sample.class);
        assertThat(setter.getName()).isEqualTo("setProperty");
    }

    @Test
    void should_return_methods_when_called_with_primitive_integer() {
        Method getter = ClassUtils.getReferencedMethod(Sample.class, Sample::getAnInt);
        assertThat(getter.getDeclaringClass()).isEqualTo(Sample.class);
        assertThat(getter.getName()).isEqualTo("getAnInt");

        Method setter = ClassUtils.getReferencedMethod(Sample.class, Sample::setAnInt);
        assertThat(setter.getDeclaringClass()).isEqualTo(Sample.class);
        assertThat(setter.getName()).isEqualTo("setAnInt");
    }

    @Test
    void should_return_methods_when_called_with_primitive_byte() {
        Method getter = ClassUtils.getReferencedMethod(Sample.class, Sample::getaByte);
        assertThat(getter.getDeclaringClass()).isEqualTo(Sample.class);
        assertThat(getter.getName()).isEqualTo("getaByte");

        Method setter = ClassUtils.getReferencedMethod(Sample.class, Sample::setaByte);
        assertThat(setter.getDeclaringClass()).isEqualTo(Sample.class);
        assertThat(setter.getName()).isEqualTo("setaByte");
    }

    @Test
    void should_return_methods_when_called_with_primitive_char() {
        Method getter = ClassUtils.getReferencedMethod(Sample.class, Sample::getaChar);
        assertThat(getter.getDeclaringClass()).isEqualTo(Sample.class);
        assertThat(getter.getName()).isEqualTo("getaChar");

        Method setter = ClassUtils.getReferencedMethod(Sample.class, Sample::setaChar);
        assertThat(setter.getDeclaringClass()).isEqualTo(Sample.class);
        assertThat(setter.getName()).isEqualTo("setaChar");
    }

    @Test
    void should_return_methods_when_called_with_primitive_float() {
        Method getter = ClassUtils.getReferencedMethod(Sample.class, Sample::getaFloat);
        assertThat(getter.getDeclaringClass()).isEqualTo(Sample.class);
        assertThat(getter.getName()).isEqualTo("getaFloat");

        Method setter = ClassUtils.getReferencedMethod(Sample.class, Sample::setaFloat);
        assertThat(setter.getDeclaringClass()).isEqualTo(Sample.class);
        assertThat(setter.getName()).isEqualTo("setaFloat");
    }

    @Test
    void should_return_methods_when_called_with_primitive_short() {
        Method getter = ClassUtils.getReferencedMethod(Sample.class, Sample::getaShort);
        assertThat(getter.getDeclaringClass()).isEqualTo(Sample.class);
        assertThat(getter.getName()).isEqualTo("getaShort");

        Method setter = ClassUtils.getReferencedMethod(Sample.class, Sample::setaShort);
        assertThat(setter.getDeclaringClass()).isEqualTo(Sample.class);
        assertThat(setter.getName()).isEqualTo("setaShort");
    }

    @Test
    void should_return_methods_when_called_with_primitive_long() {
        Method getter = ClassUtils.getReferencedMethod(Sample.class, Sample::getaLong);
        assertThat(getter.getDeclaringClass()).isEqualTo(Sample.class);
        assertThat(getter.getName()).isEqualTo("getaLong");

        Method setter = ClassUtils.getReferencedMethod(Sample.class, Sample::setaLong);
        assertThat(setter.getDeclaringClass()).isEqualTo(Sample.class);
        assertThat(setter.getName()).isEqualTo("setaLong");
    }

}