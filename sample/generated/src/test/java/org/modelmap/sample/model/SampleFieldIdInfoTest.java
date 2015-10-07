package org.modelmap.sample.model;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.modelmap.sample.field.SampleFieldId;
import org.modelmap.sample.field.SampleFieldIdInfo;

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

    private Optional<SampleFieldIdInfo> fieldInfo() {
        return stream(SampleFieldIdInfo.values()).filter(info -> info.id() == field).findFirst();
    }

    @Test
    public void should_have_field_info() {
        assertThat(fieldInfo()).isPresent();
    }

    @Test
    public void should_have_field_type() {
        assertThat(fieldInfo().get().type()).isNotNull();
    }
}
