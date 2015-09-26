package org.modelmap.core;

public interface FieldInfo {

    FieldId id();

    FieldId[] siblings();

    Class<?> type();
}
