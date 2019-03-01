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

import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;

import java.util.stream.Stream;

public class BinaryMetadata extends AbstractMetadata {
    private final Metadata left;
    private final Operator operator;
    private final Metadata right;

    public BinaryMetadata(Metadata left, Operator operator, Metadata right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Metadata getLeft() {
        return left;
    }

    public Metadata getRight() {
        return right;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    @Override
    public Stream<Metadata> left() {
        return Stream.of(left);
    }

    @Override
    public Stream<Metadata> right() {
        return Stream.of(right);
    }

    @Override
    public MetadataType type() {
        return BINARY_PREDICATE;
    }

}
