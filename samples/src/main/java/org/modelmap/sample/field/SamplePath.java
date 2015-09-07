package org.modelmap.sample.field;

import org.modelmap.core.Path;

import static org.modelmap.sample.field.SampleConstraint.NONE;

@Path
public @interface SamplePath {

    SampleFieldId field();

    SampleConstraint constraint() default NONE;
}