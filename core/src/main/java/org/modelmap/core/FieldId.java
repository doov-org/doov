package org.modelmap.core;

public interface FieldId {

    // FIXME use name()?
    @Deprecated
    String getCode();

    int position();

}
