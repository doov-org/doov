package io.doov.sample2.field;

import static io.doov.sample2.field.Sample2Constraint.NONE;

import java.lang.annotation.*;

import io.doov.core.Path;

@Path
@Repeatable(Sample2Paths.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sample2Path {

    Sample2FieldId field();

    Sample2Constraint constraint() default NONE;
}