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
package io.doov.core.dsl.meta.predicate;

import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.Element;
import io.doov.core.dsl.meta.ElementType;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.MetadataType;

public class FieldMetadata<M extends FieldMetadata<M>> extends LeafMetadata<M>
        implements PredicateMetadata {

    private final AtomicInteger evalTrue = new AtomicInteger();
    private final AtomicInteger evalFalse = new AtomicInteger();

    public FieldMetadata(MetadataType type) {
        super(type);
    }

    @Override
    public AtomicInteger evalTrue() {
        return evalTrue;
    }

    @Override
    public AtomicInteger evalFalse() {
        return evalFalse;
    }

    // field

    public static <M extends FieldMetadata<M>> M fieldMetadata(DslField<?> field) {
        return new FieldMetadata<M>(FIELD_PREDICATE).field(field);
    }

    public static <M extends FieldMetadata<M>> M fieldMetadata(String field) {
        return new FieldMetadata<M>(FIELD_PREDICATE) {
            @Override
            public String readable(Locale locale) {
                // avoid i18n in that case #hack
                // @see io.doov.core.dsl.meta.ast.AstTextVisitor.formatLeafMetadata(LeafMetadata<?>)
                return field;
            }
        }.add(new Element(() -> field, ElementType.FIELD));
    }
}