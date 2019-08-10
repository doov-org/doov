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

import static io.doov.core.dsl.meta.MetadataType.MAPPING_INPUT;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class MappingInputMetadata extends AbstractMetadata {

    private final Metadata converterMetadata;
    private final List<Metadata> metadataList;

    public MappingInputMetadata(Metadata converterMetadata, List<Metadata> metadata) {
        this.converterMetadata = converterMetadata;
        this.metadataList = metadata;
    }

    public static MappingInputMetadata inputMetadata(Metadata converterMetadata, Metadata... inputs) {
        return new MappingInputMetadata(converterMetadata, Arrays.asList(inputs));
    }

    @Override
    public Stream<Metadata> left() {
        return metadataList.stream();
    }

    @Override
    public Stream<Metadata> right() {
        return Stream.of(converterMetadata);
    }

    @Override
    public MetadataType type() {
        return MAPPING_INPUT;
    }

}
