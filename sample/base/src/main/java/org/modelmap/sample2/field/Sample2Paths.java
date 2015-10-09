package org.modelmap.sample2.field;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Sample2Paths {

    Sample2Path[] value();
}