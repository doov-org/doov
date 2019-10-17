/*
 * Copyright 2018 Courtanet
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

import static io.doov.core.dsl.meta.MetadataType.MULTIPLE_MAPPING;

import java.util.stream.Stream;

public class ConditionalMappingMetadata extends AbstractMetadata {

    private final Metadata when;
    private final Metadata then;
    private final Metadata otherwise;

    public static ConditionalMappingMetadata conditional(Metadata when, Metadata then, Metadata otherwise) {
        return new ConditionalMappingMetadata(when, then, otherwise);
    }

    public ConditionalMappingMetadata(Metadata when, Metadata then, Metadata otherwise) {
        this.when = when;
        this.then = then;
        this.otherwise = otherwise;
    }

    public Metadata when() {
        return when;
    }

    public Metadata then() {
        return then;
    }

    public Metadata otherwise() {
        return otherwise;
    }

    @Override
    public Operator getOperator() {
        return MappingOperator.conditional_mappings;
    }

    @Override
    public Stream<Metadata> children() {
        return Stream.of(when, then, otherwise);
    }

    @Override
    public MetadataType type() {
        return MULTIPLE_MAPPING;
    }

}
