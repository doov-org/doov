/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;
import io.doov.core.dsl.impl.DefaultCondition;

public interface DelegatingFieldInfo extends FieldInfo {

    FieldInfo delegate();

    @Override
    default FieldId id() {
        return delegate().id();
    }

    @Override
    default String readable() {
        return delegate().readable();
    }

    @Override
    default FieldId[] siblings() {
        return delegate().siblings();
    }

    @Override
    default Class<?> type() {
        return delegate().type();
    }

    @Override
    default Class<?>[] genericTypes() {
        return delegate().genericTypes();
    }

    @Override
    default boolean isCodeLookup() {
        return delegate().isCodeLookup();
    }

    @Override
    default boolean isCodeValuable() {
        return delegate().isCodeValuable();
    }

    @Override
    default boolean isTransient() {
        return delegate().isTransient();
    }
}
