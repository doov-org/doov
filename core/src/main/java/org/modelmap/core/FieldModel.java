package org.modelmap.core;

import java.awt.image.SampleModel;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Generated;

interface FieldModel extends Iterable<Map.Entry<FieldId, Object>> {

    <T> T get(FieldId fieldId);

    <T> void set(FieldId fieldId, T value);

}

@Generated(value = "org.modelmap.gen.ModelMapGenMojo", comments = "generated from org.modelmap.sample.model" +
                ".SampleModel")
class SampleModelWrapper implements FieldModel {

    private final SampleModel model;

    public SampleModelWrapper(SampleModel model) {
        this.model = model;
    }

    @Override
    public <T> T get(FieldId fieldId) {
        // key-value mapping code here
        return null;
    }

    @Override
    public <T> void set(FieldId fieldId, T value) {
        // key-value mapping code here
    }

    @Override
    public Iterator<Map.Entry<FieldId, Object>> iterator() {
        return null;
    }
}


