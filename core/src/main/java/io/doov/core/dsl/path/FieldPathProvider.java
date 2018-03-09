/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.path;

import java.util.List;

/**
 * Provider for {@link FieldPath}
 */
public interface FieldPathProvider {

    /**
     * Returns the list of field paths
     *
     * @return list of field paths
     */
    List<FieldPath> values();

}
