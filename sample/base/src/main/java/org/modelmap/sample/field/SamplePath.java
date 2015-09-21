package org.modelmap.sample.field;

import org.modelmap.core.Path;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.modelmap.sample.field.SampleConstraint.NONE;

@Path
@Repeatable(SamplePaths.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface SamplePath {

    SampleFieldId field();

    SampleConstraint constraint() default NONE;
}