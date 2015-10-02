package org.modelmap.core;

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
    TagId[] tags();
}
