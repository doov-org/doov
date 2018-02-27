/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.core;

import static java.util.Collections.emptyList;

import java.util.List;

/**
 * Id representing a value of the  {@code FieldModel}
 */
public interface FieldId {

    /**
     * Returns the field unique identifier
     *
     * @return the name
     */
    String code();

    /**
     * Returns the field position, when referencing an element in a {@code Collection}.
     *
     * @return the position, defaults to -1
     */
    default int position() {
        return -1;
    }

    /**
     * Returns optional tags used to decorate this field.
     *
     * @return the tags, defaults to empty list
     */
    default List<TagId> tags() {
        return emptyList();
    }

    /**
     * Returns true if this field is tagged by the given tag.
     *
     * @param tag the tag to test
     * @return true if tagged
     */
    default boolean hasTag(TagId tag) {
        return tags().contains(tag);
    }

}
