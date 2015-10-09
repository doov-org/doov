package org.modelmap.sample.model;

import static java.util.Collections.newSetFromMap;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.modelmap.core.FieldId;
import org.modelmap.core.FieldModel;
import org.modelmap.sample.field.SampleFieldId;

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
