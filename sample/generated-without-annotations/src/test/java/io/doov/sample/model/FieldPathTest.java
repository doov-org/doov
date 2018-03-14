package io.doov.sample.model;

import static io.doov.sample.model.FieldRegistry.account_accept_email;
import static io.doov.sample.model.FieldRegistry.favorite_site_1_name;
import static io.doov.sample.model.FieldRegistry.favorite_site_2_url;
import static io.doov.sample.model.FieldRegistry.user_id;
import static io.doov.sample.model.SampleModels.sample;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.runtime.RuntimeModel;
import io.doov.sample.field.dsl.DslSampleModel;

public class FieldPathTest {

    @Test
    void name() {
        SampleModel sample = sample();
        assertThat(account_accept_email.get(sample)).isTrue();
        assertThat(user_id.get(sample)).isEqualTo(1L);
        user_id.set(sample, 2L);
        assertThat(sample.getUser().getId()).isEqualTo(2L);
        assertThat(favorite_site_1_name.get(sample)).isEqualTo("Google");
        favorite_site_1_name.set(sample, "Lesfurets");
        assertThat(sample.getAccount().getTop3WebSite().get(0).getName()).isEqualTo("Lesfurets");
    }

    @Test
    void name2() {
        SampleModel sampleModel = new SampleModel();
        assertThat(favorite_site_1_name.get(sampleModel)).isNull();
        favorite_site_2_url.set(sampleModel, "google.com");
        assertThat(sampleModel.getAccount().getTop3WebSite().size()).isEqualTo(2);
        assertThat(sampleModel.getAccount().getTop3WebSite().get(0)).isNull();
        favorite_site_1_name.set(sampleModel, "ozan");
        assertThat(favorite_site_1_name.get(sampleModel)).isEqualTo("ozan");
    }

    @Test
    void name3() {
        FieldModel wrapper = SampleModels.wrapper();
        RuntimeModel<SampleModel> runtimeModel = new RuntimeModel<>(FieldRegistry.INSTANCE, new SampleModel());
        MappingRule to = DOOV.map(DslSampleModel.userId()).to(user_id);
        to.validate(wrapper, runtimeModel);
        to.executeOn(wrapper, runtimeModel);
        assertThat(runtimeModel.get(user_id)).isEqualTo(1L);
    }
}
