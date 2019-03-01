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
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.DefaultOperator.no_operator;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.ReduceType;
import io.doov.core.dsl.meta.ast.AstVisitorUtils;

/**
 * Interface for the description of a node in the syntax tree.
 */
public interface Metadata extends Readable {
    
    default Operator getOperator() {
        return no_operator;
    }

    /**
     * Returns the human readable version of this object.
     *
     * @param locale the locale to use
     * @return the readable string
     * @see #readable()
     */
    default String readable(Locale locale) {
        return astToString(this, locale).trim();
    }

    @Override
    default String readable() {
        return readable(Locale.getDefault());
    }

    default String markdown() {
        return markdown(Locale.getDefault());
    }

    default String markdown(Locale locale) {
        return AstVisitorUtils.astToMarkdown(this, locale);
    }

    /**
     * Returns the direct left children of this node in a flat list.
     *
     * @return the list of metadata
     */
    default Stream<Metadata> left() {
        return Stream.empty();
    }

    /**
     * Returns the direct right children of this node in a flat list.
     *
     * @return the list of metadata
     */
    default Stream<Metadata> right() {
        return Stream.empty();
    }

    /**
     * Returns the direct children of this node in a flat list.
     *
     * @return the list of metadata
     */
    default Stream<Metadata> children() {
        return Stream.concat(left(), right());
    }

    default Metadata childAt(int... positions) {
        if (positions == null)
            return null;
        if (positions.length == 0)
            return null;
        return childAt(positions, 0);
    }

    default Metadata childAt(int[] positions, int level) {
        final List<Metadata> childrens = children().collect(toList());
        final Metadata child = childrens.get(positions[level]);
        if (level < positions.length - 1)
            return child.childAt(positions, level + 1);
        return child;
    }

    /**
     * Returns the metadata type.
     *
     * @return the type
     */
    MetadataType type();

    /**
     * Returns the reduce metadata tree from the given context. The metadata tree is returned in its shortest form by pruning
     * branches in the evaluated syntax tree.
     *
     * @param context the evaluated context
     * @param type the type of reduction
     * @return the metadata
     */
    default Metadata reduce(Context context, ReduceType type) {
        return this;
    }
}
