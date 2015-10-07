package org.modelmap.sample.model;

import static java.util.Arrays.stream;

import java.util.Map.Entry;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.modelmap.core.*;
import org.modelmap.sample.field.SampleFieldIdInfo;

public class SampleModelCollectorTest {

    private SampleModel sample = SampleModels.sample();
    private FieldModel source = new SampleModelWrapper(sample);

    @Before
    public void before() {
        sample = SampleModels.sample();
        source = new SampleModelWrapper(sample);
    }

    @Test
    public void should_collect_all_values_when_collect_sequential() {
        should_collect_all_values_when_collect(FieldModels.stream(source, false), source);
    }

    @Test
    public void should_collect_all_values_when_collect_parallel() {
        should_collect_all_values_when_collect(FieldModels.stream(source, true), source);
    }

    private static void should_collect_all_values_when_collect(Stream<Entry<FieldId, Object>> stream,
                    FieldModel source) {
        FieldModel target = stream.collect(FieldModels.toFieldModel(SampleModelWrapper::new));

        SoftAssertions softly = new SoftAssertions();
        stream(SampleFieldIdInfo.values()).forEach(info -> {
            Object after = target.get(info.id());
            Object before = source.get(info.id());
            softly.assertThat(after).describedAs(info.name()).isEqualTo(before);
        });
        softly.assertAll();
    }
}
