package org.modelmap.sample.field;

import org.modelmap.core.Path;

import java.lang.annotation.Repeatable;

import static org.modelmap.sample.field.SampleConstraint.NONE;

@Path
@Repeatable(SamplePaths.class)
public @interface SamplePath {

    SampleFieldId field();

    SampleConstraint constraint() default NONE;
}