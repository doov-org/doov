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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.FieldId;
import io.doov.core.FieldModel;
import io.doov.sample.wrapper.SampleModelWrapper;

public class SampleModelIteratorTest {

    private SampleModel sample = SampleModels.sample();
    private FieldModel source = new SampleModelWrapper(sample);

    @BeforeEach
    public void before() {
        sample = SampleModels.sample();
        source = new SampleModelWrapper(sample);
    }

    @Test
    public void iterator_test() {
        for (Entry<FieldId, Object> entry : source) {
            assertThat(entry.getValue()).isEqualTo(source.get(entry.getKey()));
        }
    }

    @Test
    public void iterator_contains_all_fields() {
        List<FieldId> ids = new ArrayList<>(source.getFieldIds());
        for (Entry<FieldId, Object> entry : source) {
            ids.remove(entry.getKey());
        }
        assertThat(ids).hasSize(0);
    }

}
