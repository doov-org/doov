package org.modelmap.sample.model;

import static java.util.Arrays.stream;

import java.util.stream.StreamSupport;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.modelmap.core.FieldModel;
import org.modelmap.core.FieldModels;
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
        FieldModel target = StreamSupport.stream(source.spliterator(), false)
                        .collect(FieldModels.toFieldModel(new SampleModelWrapper()));
        should_collect_all_values_when_collect(target, source);
    }

    @Test
    public void should_collect_all_values_when_collect_parallel() {
        FieldModel target = StreamSupport.stream(source.spliterator(), true)
                        .collect(FieldModels.toMapThenFieldModel(SampleModelWrapper::new));
        should_collect_all_values_when_collect(target, source);
    }

    private static void should_collect_all_values_when_collect(FieldModel target, FieldModel source) {
        SoftAssertions softly = new SoftAssertions();
        stream(SampleFieldIdInfo.values()).forEach(info -> {
            Object after = target.get(info.id());
            Object before = source.get(info.id());
            softly.assertThat(after).describedAs(info.name()).isEqualTo(before);
        });
        softly.assertAll();
    }
}
