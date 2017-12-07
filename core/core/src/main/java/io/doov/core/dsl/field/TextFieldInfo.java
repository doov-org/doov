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

import io.doov.core.dsl.impl.IntegerCondition;
import io.doov.core.dsl.impl.StringCondition;
import io.doov.core.dsl.lang.StepCondition;

public interface TextFieldInfo extends BaseFieldInfo<String> {

    default StepCondition contains(String string) {
        return getStringCondition().contains(string);
    }

    default StepCondition matches(String regex) {
        return getStringCondition().matches(regex);
    }

    default StepCondition startsWith(String prefix) {
        return getStringCondition().startsWith(prefix);
    }

    default StepCondition endsWith(String suffix) {
        return getStringCondition().endsWith(suffix);
    }

    default IntegerCondition length() {
        return getStringCondition().length();
    }

    default IntegerCondition parseInt() {
        return getStringCondition().parseInt();
    }

    StringCondition getStringCondition();

}
