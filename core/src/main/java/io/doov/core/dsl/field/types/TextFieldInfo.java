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
package io.doov.core.dsl.field.types;

import io.doov.core.dsl.field.BaseFieldInfo;
import io.doov.core.dsl.impl.IntegerCondition;
import io.doov.core.dsl.impl.StringCondition;
import io.doov.core.dsl.lang.StepCondition;

/**
 * Base interface for text field info.
 * <p>
 * It contains default methods for common checks, which are calls to methods on {@link StringCondition}.
 */
public interface TextFieldInfo extends BaseFieldInfo<String> {

    /**
     * See {@link StringCondition#contains(String)}
     *
     * @param string the string
     * @return the step condition
     * @see StringCondition#contains(String)
     */
    default StepCondition contains(String string) {
        return getStringCondition().contains(string);
    }

    /**
     * See {@link StringCondition#matches(String)}
     *
     * @param regex the regex
     * @return the step condition
     * @see StringCondition#matches(String)
     */
    default StepCondition matches(String regex) {
        return getStringCondition().matches(regex);
    }

    /**
     * See {@link StringCondition#startsWith(String)}
     *
     * @param prefix the prefix
     * @return the step condition
     * @see StringCondition#startsWith(String)
     */
    default StepCondition startsWith(String prefix) {
        return getStringCondition().startsWith(prefix);
    }

    /**
     * See {@link StringCondition#endsWith(String)}
     *
     * @param suffix the suffix
     * @return the step condition
     * @see StringCondition#endsWith(String)
     */
    default StepCondition endsWith(String suffix) {
        return getStringCondition().endsWith(suffix);
    }

    /**
     * See {@link StringCondition#length()}
     *
     * @return the integer condition
     * @see StringCondition#length()
     */
    default IntegerCondition length() {
        return getStringCondition().length();
    }

    /**
     * See {@link StringCondition#parseInt()}
     *
     * @return the integer condition
     * @see StringCondition#parseInt()
     */
    default IntegerCondition parseInt() {
        return getStringCondition().parseInt();
    }

    /**
     * Returns a new string condition that will use this as a field.
     *
     * @return the step condition
     */
    StringCondition getStringCondition();

    @Override
    default StringCondition getDefaultCondition() {
        return getStringCondition();
    }

}
