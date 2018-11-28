/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.core.dsl.meta;

import java.util.Iterator;

public interface MetadataVisitor {

    // Metadata

    default void start(Metadata metadata, int depth) {
    }

    default void beforeChild(Metadata metadata, Metadata child, int depth) {
    }

    default void afterChild(Metadata metadata, Metadata child, boolean hasNext, int depth) {
    }

    default void end(Metadata metadata, int depth) {
    }

    default void browse(Metadata root, int depth) {
        start(root, depth);
        final Iterator<Metadata> it = root.children().iterator();
        while (it.hasNext()) {
            final Metadata child = it.next();
            beforeChild(root, child, depth);
            browse(child, depth + 1);
            afterChild(root, child, it.hasNext(), depth);
        }
        end(root, depth);
    }

}
