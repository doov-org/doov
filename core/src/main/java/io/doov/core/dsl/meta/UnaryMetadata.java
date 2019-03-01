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

import static io.doov.core.dsl.meta.MetadataType.UNARY_PREDICATE;

import java.util.stream.Stream;

public class UnaryMetadata extends AbstractMetadata {
    private final Operator operator;
    private final Metadata value;

    public UnaryMetadata(Operator operator, Metadata value) {
        this.operator = operator;
        this.value = value;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    public Metadata getValue() {
        return value;
    }

    @Override
    public MetadataType type() {
        return UNARY_PREDICATE;
    }

    @Override
    public Stream<Metadata> right() {
        return Stream.of(value);
    }
}
