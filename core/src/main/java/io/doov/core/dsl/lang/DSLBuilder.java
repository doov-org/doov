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
package io.doov.core.dsl.lang;

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToMarkdown;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.Locale;
import java.util.Objects;

import io.doov.core.FieldId;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.ast.AstVisitorUtils;
import io.doov.core.dsl.meta.ast.FieldCollector;

public interface DSLBuilder extends Readable {

    Metadata metadata();

    /**
     * Returns the human readable version of this object.
     *
     * @param locale the locale to use
     * @return the readable string
     * @see #readable()
     */
    default String readable(Locale locale) {
        return astToString(metadata(), locale).trim();
    }

    @Override
    default String readable() {
        return readable(Locale.getDefault());
    }

    default String markdown() {
        return markdown(Locale.getDefault());
    }

    default String markdown(Locale locale) {
        return astToMarkdown(metadata(), locale);
    }

    /**
     * Returns true if the implementation is using the fieldId, false otherwise.
     * 
     * @param fieldId the field id
     * @return the fieldId usage
     */
    default boolean isUsing(FieldId fieldId) {
        return AstVisitorUtils.collect(metadata(), FieldCollector::fieldsOf).anyMatch(f -> Objects.equals(f, fieldId));
    }

}
