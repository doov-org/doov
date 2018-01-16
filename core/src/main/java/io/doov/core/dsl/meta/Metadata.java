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

import java.util.List;

import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Readable;

/**
 * Interface for the description of a node in the syntax tree.
 */
public interface Metadata extends Readable, SyntaxTree {

    /**
     * Merges the node with the given node.
     *
     * @param other the other metadata to merge
     * @return the merged metadata
     */
    default PredicateMetadata merge(LeafMetadata other) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the tree of elements under this node in a flat list.
     *
     * @return the list of elements
     */
    List<Element> flatten();

    /**
     * Returns the direct children of this node in a flat list.
     *
     * @return the list of metadata
     */
    List<Metadata> children();

    /**
     * Returns the metadata type.
     *
     * @return the type
     */
    MetadataType type();

    /**
     * Returns the failure message from the given context. The message is returned in its shortest form by pruning
     * branches in the evaluated syntax tree that are not needed for the failure message.
     *
     * @param context the evaluated context
     * @return the metadata
     */
    Metadata message(Context context);

}
