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
import io.doov.core.dsl.impl.base.StringFunction;
import io.doov.core.dsl.impl.num.IntegerFunction;
import io.doov.core.dsl.lang.StepCondition;

/**
 * Base interface for text field info.
 * <p>
 * It contains default methods for common checks, which are calls to methods on {@link StringFunction}.
 */
public interface TextFieldInfo extends BaseFieldInfo<String> {

    /**
     * See {@link StringFunction#contains(String)}
     *
     * @param string the string
     * @return the step condition
     * @see StringFunction#contains(String)
     */
    default StepCondition contains(String string) {
        return getStringFunction().contains(string);
    }

    /**
     * See {@link StringFunction#contains(StringFunction)}
     *
     * @param string the string
     * @return the step condition
     * @see StringFunction#contains(String)
     */
    default StepCondition contains(StringFunction string) {
        return getStringFunction().contains(string);
    }

    /**
     *
     * @param regex the regex
     * @return the step condition
     * @see StringFunction#matches(String)
     */
    default StepCondition matches(String regex) {
        return getStringFunction().matches(regex);
    }

    /**
     * See {@link StringFunction#startsWith(String)}
     *
     * @param prefix the prefix
     * @return the step condition
     * @see StringFunction#startsWith(String)
     */
    default StepCondition startsWith(String prefix) {
        return getStringFunction().startsWith(prefix);
    }

    /**
     * See {@link StringFunction#endsWith(String)}
     *
     * @param suffix the suffix
     * @return the step condition
     * @see StringFunction#endsWith(String)
     */
    default StepCondition endsWith(String suffix) {
        return getStringFunction().endsWith(suffix);
    }

    /**
     * See {@link StringFunction#length()}
     *
     * @return the integer condition
     * @see StringFunction#length()
     */
    default IntegerFunction length() {
        return getStringFunction().length();
    }

    /**
     * See {@link StringFunction#parseInt()}
     *
     * @return the integer condition
     * @see StringFunction#parseInt()
     */
    default IntegerFunction parseInt() {
        return getStringFunction().parseInt();
    }

    /**
     * Returns a new string condition that will use this as a field.
     *
     * @return the step condition
     */
    StringFunction getStringFunction();

    @Override
    default StringFunction getDefaultFunction() {
        return getStringFunction();
    }

}
