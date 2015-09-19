package org.modelmap.core;

import java.util.Collection;

public interface FieldId {

    String name();

    int position();

    Collection<TagId> tags();
}
