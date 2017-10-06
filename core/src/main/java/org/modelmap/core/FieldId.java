package org.modelmap.core;

import java.util.List;

/**
 * Id representing a value of the  {@code FieldModel}
 */
public interface FieldId {

    /**
     * @return the field unique identifier
     */
    String name();

    /**
     * @return the field position, when referencing an element in a {@code Collection}
     */
    int position();

    /**
     * @return optionnal tags used to decorate this field
     */
    List<TagId> tags();

    /**
     * @param tag the tag to check
     * @return <code>true</code> if this field is tagged by <code>tag</code>
     */
    default boolean hasTag(TagId tag) {
        return tags().contains(tag);
    }
}
