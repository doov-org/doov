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

import static java.util.Collections.newSetFromMap;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import io.doov.core.FieldId;
import io.doov.core.FieldModel;
import io.doov.sample.field.SampleFieldId;

public class SampleModelStreamTest {

    private SampleModel sample = SampleModels.sample();
    private FieldModel source = new SampleModelWrapper(sample);

    @Before
    public void before() {
        sample = SampleModels.sample();
        source = new SampleModelWrapper(sample);
    }

    @Test
    public void should_peek_fields_values_when_using_stream_sequential() {
        should_peek_fields_values_when_using_stream(source.stream());
    }

    @Test
    public void should_peek_fields_values_when_using_stream_parallel() {
        should_peek_fields_values_when_using_stream(source.parallelStream());
    }

    private static void should_peek_fields_values_when_using_stream(Stream<Entry<FieldId, Object>> stream) {
        Set<FieldId> peeked = newSetFromMap(new ConcurrentHashMap<>());
        stream.forEach(e -> peeked.add(e.getKey()));
        assertThat(peeked).containsAll(EnumSet.allOf(SampleFieldId.class));
    }
}
