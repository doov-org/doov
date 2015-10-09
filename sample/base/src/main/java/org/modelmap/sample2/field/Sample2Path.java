package org.modelmap.sample2.field;

import static org.modelmap.sample2.field.Sample2Constraint.NONE;

import java.lang.annotation.*;

import org.modelmap.core.Path;

@Path
@Repeatable(Sample2Paths.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sample2Path {

    Sample2FieldId field();

    Sample2Constraint constraint() default NONE;
}