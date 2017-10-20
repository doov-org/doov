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

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import io.doov.core.FieldInfo;
import io.doov.sample.field.SampleFieldId;
import io.doov.sample.field.SampleFieldIdInfo;

@RunWith(Parameterized.class)
public class SampleFieldIdInfoTest {

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return stream(SampleFieldId.values()).map(f -> new Object[] { f.name(), f }).collect(Collectors.toList());
    }

    private final SampleFieldId field;

    public SampleFieldIdInfoTest(String name, SampleFieldId field) {
        this.field = field;
    }

    private Optional<FieldInfo> fieldInfo() {
        return SampleFieldIdInfo.values().stream().filter(info -> info.id() == field).findFirst();
    }

    @Test
    public void should_have_field_info() {
        assertThat(fieldInfo()).isPresent();
    }

    @Test
    public void should_have_field_type() {
        assertThat(fieldInfo()).isPresent();
        assertThat(fieldInfo().orElse(null).type()).isNotNull();
    }
}
