package org.modelmap.core;


import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

/**
 * {@code FieldModel} implementation based on {@code java.util.Map}
 */
public class BaseFieldModel implements FieldModel {

    private final Map<FieldId, Object> values;
    private final FieldInfo[] fieldInfos;

    public BaseFieldModel(FieldInfo... fieldInfos) {
        this(new HashMap<>(), fieldInfos);
    }

    public BaseFieldModel(Map<FieldId, Object> values, FieldInfo... fieldInfos) {
        this.values = values;
        this.fieldInfos = fieldInfos;
    }

    public BaseFieldModel(FieldModel fieldModel) {
        this(fieldModel.getFieldInfos());
        setAll(fieldModel);
    }

    public Map<FieldId, Object> asMap() {
        return new HashMap<>(values);
    }

    @Override
    public FieldInfo[] getFieldInfos() {
        return fieldInfos;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(FieldId fieldId) {
        return (T) values.get(fieldId);
    }

    @Override
    public void set(FieldId fieldId, Object value) {
        values.put(fieldId, value);
        Arrays.stream(siblingsOf(fieldId)).forEach(s -> values.put(s, value));
    }


    private static final FieldId[] NO_SIBLINGS = new FieldId[] {};

    private FieldId[] siblingsOf(FieldId fieldId) {
        Optional<FieldInfo> sublings = Arrays.stream(fieldInfos).filter(info -> info.id() == fieldId).findFirst();
        return sublings.isPresent() ? sublings.get().siblings() : NO_SIBLINGS;
    }

    @Override
    public Iterator<Entry<FieldId, Object>> iterator() {
        return values.entrySet().iterator();
    }

    @Override
    public Spliterator<Entry<FieldId, Object>> spliterator() {
        return values.entrySet().spliterator();
    }

    @Override
    public Stream<Entry<FieldId, Object>> stream() {
        return values.entrySet().stream();
    }

    @Override
    public Stream<Entry<FieldId, Object>> parallelStream() {
        return values.entrySet().parallelStream();
    }
}
