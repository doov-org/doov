package org.modelmap.sample.field;

import static org.modelmap.sample.field.SampleConstraint.NONE;

import java.lang.annotation.*;

import org.modelmap.core.Path;

@Path
@Repeatable(SamplePaths.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface SamplePath {

    SampleFieldId field();

    SampleConstraint constraint() default NONE;
}

