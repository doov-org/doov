package org.modelmap.sample.model;

import static org.modelmap.sample.model.SampleModelWrapper.toConcurrentFieldModel;
import static org.modelmap.sample.model.SampleModelWrapper.toFieldModel;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.modelmap.core.FieldModel;
import org.modelmap.sample.field.SampleFieldId;
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
        SampleModelWrapper target = source.stream().collect(toFieldModel());
        should_collect_all_values_when_collect(target, source);
    }

    @Test
    public void should_collect_all_values_when_collect_parallel() {
        SampleModelWrapper target = source.parallelStream().collect(toConcurrentFieldModel());
        should_collect_all_values_when_collect(target, source);
    }

    @Test
    public void shoudl() {
        final SampleModelWrapper wrapper = new SampleModelWrapper();
        wrapper.set(SampleFieldId.FAVORITE_SITE_URL_3, "foo");
        Assertions.assertThat(wrapper.<String> get(SampleFieldId.FAVORITE_SITE_URL_3)).isEqualTo("foo");
    }

    private static void should_collect_all_values_when_collect(FieldModel target, FieldModel source) {
        SoftAssertions softly = new SoftAssertions();
        SampleFieldIdInfo.values().forEach(info -> {
            Object after = target.get(info.id());
            Object before = source.get(info.id());
            softly.assertThat(after).describedAs(info.id().name()).isEqualTo(before);
        });
        softly.assertAll();
    }

}
