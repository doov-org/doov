/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core;

import java.lang.annotation.*;

/**
 * A Field annotated with this annotation will <b>not be serialized</b> (client/server) and <b>not persisted</b> in the
 * persistent storage.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface FieldTransient {

}