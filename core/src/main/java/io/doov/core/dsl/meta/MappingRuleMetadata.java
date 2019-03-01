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

import java.util.stream.Stream;

public class MappingRuleMetadata extends AbstractMetadata {

    private final Metadata inputMetadata;
    private final Metadata outputMetadata;

    public MappingRuleMetadata(Metadata inputMetadata, Metadata outputMetadata) {
        this.inputMetadata = inputMetadata;
        this.outputMetadata = outputMetadata;
    }

    @Override
    public Stream<Metadata> left() {
        return Stream.of(inputMetadata);
    }

    @Override
    public Stream<Metadata> right() {
        return Stream.of(outputMetadata);
    }

    @Override
    public MetadataType type() {
        return MetadataType.SINGLE_MAPPING;
    }

}
