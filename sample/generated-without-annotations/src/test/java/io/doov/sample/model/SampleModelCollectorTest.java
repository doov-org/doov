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

import static io.doov.sample.model.SampleModelWrapper.toConcurrentFieldModel;
import static io.doov.sample.model.SampleModelWrapper.toFieldModel;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.FieldModel;
import io.doov.sample.field.SampleFieldId;
import io.doov.sample.field.SampleFieldIdInfo;

public class SampleModelCollectorTest {

    private SampleModel sample = SampleModels.sample();
    private FieldModel source = new SampleModelWrapper(sample);

    @BeforeEach
    public void before() {
        sample = SampleModels.sample();
        source = new SampleModelWrapper(sample);
    }

    @Test
    public void should_collect_all_values_when_collect_sequential() {
        SampleModelWrapper target = source.stream().collect(toFieldModel());
        should_collect_all_values_when_collect(target, source);
    }

    @Test
    public void should_collect_all_values_when_collect_parallel() {
        SampleModelWrapper target = source.parallelStream().collect(toConcurrentFieldModel());
        should_collect_all_values_when_collect(target, source);
    }

    @Test
    public void should_setter_and_getter_work() {
        final SampleModelWrapper wrapper = new SampleModelWrapper();
        wrapper.set(SampleFieldId.FAVORITE_SITE_URL_3, "foo");
        Assertions.assertThat(wrapper.<String> get(SampleFieldId.FAVORITE_SITE_URL_3)).isEqualTo("foo");
    }

    private static void should_collect_all_values_when_collect(FieldModel target, FieldModel source) {
        SoftAssertions softly = new SoftAssertions();
        SampleFieldIdInfo.stream().forEach(info -> {
            Object after = target.get(info.id());
            Object before = source.get(info.id());
            softly.assertThat(after).describedAs(info.id().code()).isEqualTo(before);
        });
        softly.assertAll();
    }

}
