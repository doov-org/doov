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

import java.util.ArrayList;
import java.util.List;

import io.doov.core.FieldId;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.Element;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.function.TemplateParamMetadata;
import io.doov.core.dsl.meta.predicate.FieldMetadata;

public class FieldCollector {

    public static List<FieldId> collect(Metadata metadata) {
        final List<FieldId> fields = new ArrayList<FieldId>();
        collect(metadata, fields);
        return fields;
    }

    private static void collect(Metadata metadata, List<FieldId> fields) {
        switch (metadata.type()) {
            case RULE:
            case NARY_PREDICATE:
            case MULTIPLE_MAPPING:
            case THEN_MAPPING:
            case ELSE_MAPPING:
            case MAPPING_INPUT:
            case UNARY_PREDICATE:
            case WHEN:
            case BINARY_PREDICATE:
            case SINGLE_MAPPING:
                metadata.children().forEach(m -> collect(m, fields));
                break;
            case TEMPLATE_PARAM:
                templateParam((TemplateParamMetadata) metadata, fields);
                break;
            case LEAF_PREDICATE:
            case FIELD_PREDICATE:
            case LEAF_VALUE:
            case MAPPING_LEAF:
            case TEMPLATE_IDENTIFIER:
                leaf(metadata, fields);
                break;
            case MAPPING_LEAF_ITERABLE:
            case TYPE_CONVERTER:
            case FIELD_PREDICATE_MATCH_ANY:
                break;
            default:
                throw new IllegalStateException(metadata.type().name());
        }
    }

    private static void leaf(Metadata metadata, List<FieldId> fields) {
        final List<Element> elts = new ArrayList<Element>(((LeafMetadata<?>) metadata).elements());
        for (Element e : elts) {
            if (e.getType() == FIELD) {
                final Metadata fieldMetadata = ((DslField<?>) e.getReadable()).getMetadata();
                if (fieldMetadata.type() == TEMPLATE_PARAM) {
                    templateParam((TemplateParamMetadata) fieldMetadata, fields);
                } else {
                    fields.add(((FieldMetadata<?>) fieldMetadata).field().id());
                }
            }
        }
    }

    private static void templateParam(TemplateParamMetadata metadata, List<FieldId> fields) {
        if (metadata.getRight().type() == EMPTY)
            return;
        fields.add(((FieldMetadata<?>) metadata.childAt(1)).field().id());
    }
}
