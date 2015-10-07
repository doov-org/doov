package org.modelmap.sample.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.modelmap.core.FieldId;
import org.modelmap.core.FieldModel;

public class SampleModelIteratorTest {
    private SampleModel sample = SampleModels.sample();
    private FieldModel source = new SampleModelWrapper(sample);

    @Before
    public void before() {
        sample = SampleModels.sample();
        source = new SampleModelWrapper(sample);
    }

    @Test
    public void iterator_test() {
        for (Entry<FieldId, Object> entry : source)
            assertEquals(entry.getValue(), source.get(entry.getKey()));
    }

    @Test
    public void iterator_contains_all_fields() {
        List<FieldId> ids = new ArrayList<>(source.getFieldIds());
        for (Entry<FieldId, Object> entry : source)
            ids.remove(entry.getKey());
        assertEquals(0, ids.size());
    }
}
