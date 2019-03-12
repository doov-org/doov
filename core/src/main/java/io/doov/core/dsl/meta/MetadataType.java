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

public enum MetadataType {
    RULE, //
    WHEN, //
    LEAF_VALUE, //
    BINARY_PREDICATE, //
    LEAF_PREDICATE, //
    FIELD_PREDICATE, //
    FIELD_PREDICATE_MATCH_ANY, //
    NARY_PREDICATE, //
    UNARY_PREDICATE, //
    EMPTY, //
    SINGLE_MAPPING, //
    MULTIPLE_MAPPING, //
    THEN_MAPPING, //
    ELSE_MAPPING, //
    MAPPING_INPUT, //
    MAPPING_LEAF, //
    TYPE_CONVERTER, //
    TYPE_CONVERTER_IDENTITY, //
    TEMPLATE_PARAM, //
    ;
}
