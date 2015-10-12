package org.modelmap.sample.model;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.modelmap.sample.field.SampleFieldIdInfo;
import org.modelmap.sample.field.SampleTag;

@RunWith(Parameterized.class)
public class SampleModelWrapperTest {

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return stream(SampleFieldIdInfo.values()).map(f -> new Object[] { f.name(), f }).collect(Collectors.toList());
    }

    private final SampleModelWrapper wrapper;
    private final SampleFieldIdInfo field;

    public SampleModelWrapperTest(String name, SampleFieldIdInfo field) {
        this.wrapper = new SampleModelWrapper();
        this.field = field;
    }

    @Test
    public void should_contains_all_field_info() {
        assertThat(wrapper.getFieldInfos()).contains(field);
        assertThat(wrapper.getFieldIds()).contains(field.id());
    }

    @Test
    public void should_be_null_when_clear_all() {
        wrapper.clear();
        assertValueNull();
    }

    @Test
    public void should_be_null_when_clear_tag() {
        if (field.id().tags().length == 0) {
            return;
        }
        wrapper.clear(field.id().tags()[0]);
        assertValueNull();
    }

    @Test
    public void should_not_throw_NPE_when_null_value_set() throws Exception {
        wrapper.set(field.id(), null);
    }

    @Test
    public <T> void should_return_same_value_when_updated() throws Exception {
        Object value = value(field);
        wrapper.set(field.id(), value);

        if (asList(field.id().tags()).contains(SampleTag.READ_ONLY)) {
            assertThat(wrapper.<T> get(field.id())).isNull();
        } else {
            assertThat(wrapper.<T> get(field.id())).isEqualTo(value);
        }
    }

    private void assertValueNull() {
        Object value = wrapper.get(field.id());
        if (Number.class.isAssignableFrom(field.type())) {
            assertThat(((Number) value).longValue()).isEqualTo(0);
        } else {
            assertThat(value).isNull();
        }
    }

    private static Object value(SampleFieldIdInfo field) throws IllegalAccessException, InstantiationException {
        if (field.type().equals(Long.class)) {
            return Long.MAX_VALUE;
        } else if (field.type().equals(Integer.class)) {
            return Integer.MAX_VALUE;
        } else if (field.type().equals(Short.class)) {
            return Short.MAX_VALUE;
        } else if (field.type().equals(Double.class)) {
            return Double.MAX_VALUE;
        } else if (field.type().equals(Byte.class)) {
            return Byte.MAX_VALUE;
        } else if (field.type().equals(Boolean.class)) {
            return Boolean.FALSE;
        } else if (field.type().isEnum()) {
            return field.type().getEnumConstants()[0];
        } else if (Collection.class.isAssignableFrom(field.type())) {
            return new ArrayList<>();
        } else if (field.type().equals(LocalDate.class)) {
            return LocalDate.now();
        } else if (field.type().equals(String.class)) {
            return "foo";
        }
        return field.type().newInstance();
    }
}
