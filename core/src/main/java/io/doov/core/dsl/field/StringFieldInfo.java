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

import static io.doov.core.dsl.meta.FieldMetadata.fieldOnlyMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.lengthIsMetadata;

import java.util.Optional;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.IntegerCondition;
import io.doov.core.dsl.impl.StringCondition;
import io.doov.core.dsl.lang.StepCondition;

public class StringFieldInfo extends DefaultFieldInfo<String> {

    public StringFieldInfo(FieldId fieldId, String readable, FieldId[] siblings) {
        super(fieldId, readable, String.class, new Class[] {}, siblings);
    }

    public StepCondition contains(String regex) {
        return new StringCondition(this).contains(regex);
    }

    public StepCondition matches(String regex) {
        return new StringCondition(this).matches(regex);
    }

    public StepCondition startsWith(String prefix) {
        return new StringCondition(this).startsWith(prefix);
    }

    public StepCondition endsWith(String suffix) {
        return new StringCondition(this).endsWith(suffix);
    }

    public IntegerCondition length() {
        return new IntegerCondition(lengthIsMetadata(this),
                        (model, context) -> Optional.ofNullable(model.<String> get(id()))
                                        .map(String::length));
    }

    public IntegerCondition parseInt() {
        return new IntegerCondition(fieldOnlyMetadata(this),
                        (model, context) -> Optional.ofNullable(model.<String> get(id()))
                                        .map(Integer::parseInt));
    }

}
