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
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.ElementType.FIELD;
import static io.doov.core.dsl.meta.MetadataType.EMPTY;
import static io.doov.core.dsl.meta.MetadataType.TEMPLATE_PARAM;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.empty;

import java.util.List;
import java.util.stream.Stream;

import io.doov.core.FieldId;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.function.TemplateParamMetadata;
import io.doov.core.dsl.meta.predicate.FieldMetadata;

public class FieldCollector {

    public static List<FieldId> collect(Metadata metadata) {
        return AstVisitorUtils.collect(metadata, FieldCollector::fieldsOf).collect(toList());
    }

    public static Stream<FieldId> fieldsOf(Metadata metadata) {
        if (metadata instanceof LeafMetadata) {
            return ((LeafMetadata<?>) metadata).elements().stream()
                    .filter(e -> e.getType() == FIELD)
                    .map(e -> {
                        final Metadata fieldMetadata = ((DslField<?>) e.getReadable()).getMetadata();
                        if (fieldMetadata.type() == TEMPLATE_PARAM) {
                            return templateParam((TemplateParamMetadata) fieldMetadata);
                        } else {
                            return ((FieldMetadata<?>) fieldMetadata).field().id();
                        }
                    });
        } else {
            return empty();
        }
    }

    private static FieldId templateParam(TemplateParamMetadata metadata) {
        if (metadata.getRight().type() == EMPTY)
            return null;
        return ((FieldMetadata<?>) metadata.childAt(1)).field().id();
    }
}
