package io.doov.sample.field;

import static io.doov.sample.field.SampleConstraint.NONE;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.doov.core.Path;

@Path
@Repeatable(CorePaths.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface CorePath {

    CoreFieldId field();

    SampleConstraint constraint() default NONE;

    String readable() default "";

}
