package io.doov.sample.model;

import static io.doov.sample.field.dsl.DslSampleModel.userBirthDate;
import static io.doov.sample.field.dsl.DslSampleModel.userId;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.base.Stopwatch;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.runtime.RuntimeModel;
import io.doov.sample.field.RuntimePath;

public class RuntimeFieldTest {

    private FieldModel wrapper;
    private MappingRule mappingRule;
    private MappingRule mappingRule2;
    private MappingRule mappingRule3;

    @BeforeEach
    void setUp() {
        wrapper = SampleModels.wrapper();
        mappingRule = DOOV.map(userId).to(userId);
        mappingRule2 = DOOV.map(userBirthDate).to(userBirthDate);
        mappingRule3 = DOOV.map(RuntimePath.TEL).to(RuntimePath.TEL);
    }

    @Test
    void map_runtime() {
        RuntimeModel<SampleModel> runtimeModel = new RuntimeModel<>(RuntimePath.INSTANCE, new SampleModel());
        assertThat(mappingRule.executeOn(wrapper, runtimeModel).isSuccess()).isTrue();
        mappingRule.executeOn(wrapper, runtimeModel);
        assertThat(runtimeModel.get(userId)).isEqualTo(1L);

        assertThat(mappingRule.executeOn(wrapper, runtimeModel).isSuccess()).isTrue();
        mappingRule3.executeOn(wrapper, runtimeModel);
        assertThat(runtimeModel.get(RuntimePath.TEL)).isNull();

        assertThat(mappingRule.executeOn(wrapper, runtimeModel).isSuccess()).isTrue();
        mappingRule2.executeOn(wrapper, runtimeModel);
        assertThat(runtimeModel.get(RuntimePath.BIRTHDATE)).isEqualTo(LocalDate.of(1980, 8, 1));

    }

    @Test
    void map_codegen() {
        SampleModelWrapper modelWrapper = new SampleModelWrapper();

        Stopwatch started = Stopwatch.createStarted();
        assertThat(mappingRule.executeOn(wrapper, modelWrapper).isSuccess()).isTrue();
        mappingRule.executeOn(wrapper, modelWrapper);
        System.out.println(started.elapsed(TimeUnit.NANOSECONDS));
        assertThat(modelWrapper.get(userId)).isEqualTo(1L);

        mappingRule.executeOn(wrapper, modelWrapper);
        assertThat(modelWrapper.get(RuntimePath.USER_ID)).isEqualTo(1L);

        assertThat(mappingRule.executeOn(wrapper, modelWrapper).isSuccess()).isTrue();
        mappingRule3.executeOn(wrapper, modelWrapper);
        assertThat(modelWrapper.get(RuntimePath.TEL)).isNull();

        assertThat(mappingRule.executeOn(wrapper, modelWrapper).isSuccess()).isTrue();
        mappingRule2.executeOn(wrapper, modelWrapper);
        assertThat(modelWrapper.get(RuntimePath.BIRTHDATE)).isEqualTo(LocalDate.of(1980, 8, 1));

    }
}
