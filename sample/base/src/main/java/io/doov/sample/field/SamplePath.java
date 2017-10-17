package io.doov.sample.field;

import static io.doov.sample.field.SampleConstraint.NONE;

import java.lang.annotation.*;

import io.doov.core.Path;

@Path
@Repeatable(SamplePaths.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface SamplePath {

    SampleFieldId field();

    SampleConstraint constraint() default NONE;

    String readable() default "";

}

