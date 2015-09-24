package org.modelmap.sample.field;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SamplePaths {

    SamplePath[] value();
}