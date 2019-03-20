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
package io.doov.core.dsl.field;

import static io.doov.core.dsl.meta.predicate.FieldMetadata.fieldMetadata;

import java.io.Serializable;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.meta.Metadata;

@SuppressWarnings("serial")
public class DefaultFieldInfo<T> implements FieldInfo, BaseFieldInfo<T>, Serializable {

    private final FieldId fieldId;
    private final String readable;
    private final Metadata metadata;
    private final Class<?> type;
    private boolean _transient;
    private boolean codeValuable;
    private boolean codeLookup;
    private final Class<?>[] genericTypes;
    private final FieldId[] siblings;

    public DefaultFieldInfo(FieldId fieldId, String readable, Class<?> type, boolean _transient, boolean codeValuable,
                            boolean codeLookup, Class<?>[] genericTypes, FieldId... siblings) {
        this.fieldId = fieldId;
        this.readable = readable;
        this.type = type;
        this._transient = _transient;
        this.codeValuable = codeValuable;
        this.codeLookup = codeLookup;
        this.genericTypes = genericTypes;
        this.siblings = siblings;
        this.metadata = fieldMetadata(this, readable);
    }

    public DefaultFieldInfo(FieldInfo fieldInfo) {
        this(fieldInfo.id(),
                fieldInfo.readable(),
                fieldInfo.type(),
                fieldInfo.isTransient(),
                fieldInfo.isCodeValuable(),
                fieldInfo.isCodeLookup(),
                fieldInfo.genericTypes(),
                fieldInfo.siblings());
    }

    @Override
    public FieldId id() {
        return fieldId;
    }

    @Override
    public Class<?> type() {
        return type;
    }

    @Override
    public boolean isTransient() {
        return _transient;
    }

    @Override
    public boolean isCodeValuable() {
        return codeValuable;
    }

    @Override
    public boolean isCodeLookup() {
        return codeLookup;
    }

    @Override
    public FieldId[] siblings() {
        return siblings;
    }

    @Override
    public Class<?>[] genericTypes() {
        return genericTypes;
    }

    @Override
    public DefaultCondition<T> getDefaultFunction() {
        return new DefaultCondition<>(this);
    }

    @Override
    public String readable() {
        return readable;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

}
