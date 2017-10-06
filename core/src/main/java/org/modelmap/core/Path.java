package org.modelmap.core;

import java.lang.annotation.*;

/**
 * Annotates an annotation used to annotate a bean property (attribute, getter or setter) with a {@code FieldId}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Path {
}