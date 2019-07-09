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

import java.util.Locale;

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
     * See {@link StringFunction#trim()}
     *
     * @return the string function
     * @see StringFunction#trim()
     */
    default StringFunction trim() {
        return getStringFunction().trim();
    }

    /**
     * See {@link StringFunction#replaceAll(String, String)}
     *
     * @param regex regex string
     * @param replacement replacement function
     * @return the string function
     * @see StringFunction#replaceAll(String, String)
     */
    default StringFunction replaceAll(String regex, String replacement) {
        return getStringFunction().replaceAll(regex, replacement);
    }

    /**
     * See {@link StringFunction#replaceAll(Function, Function)}
     *
     * @param regex regex function
     * @param replacement replacement function
     * @return the string function
     * @see StringFunction#replaceAll(Function, Function)
     */
    default StringFunction replaceAll(Function<String> regex, Function<String> replacement) {
        return getStringFunction().replaceAll(regex, replacement);
    }

    /**
     * See {@link StringFunction#substring(Function, Function)}
     *
     * @param beginIndex begin index
     * @param endIndex enc index
     * @return the string function
     * @see StringFunction#substring(Function, Function)
     */
    default StringFunction substring(int beginIndex, int endIndex) {
        return getStringFunction().substring(beginIndex, endIndex);
    }

    /**
     * See {@link StringFunction#substring(Function, Function)}
     *
     * @param beginIndexFunction regex function
     * @param endIndexFunction replacement function
     * @return the string function
     * @see StringFunction#substring(Function, Function)
     */
    default StringFunction substring(Function<Integer> beginIndexFunction, Function<Integer> endIndexFunction) {
        return getStringFunction().substring(beginIndexFunction, endIndexFunction);
    }

    /**
     * See {@link StringFunction#upperCase(Locale)}
     *
     * @param locale locale
     * @return the string function
     * @see StringFunction#upperCase(Locale)
     */
    default StringFunction upperCase(Locale locale) {
        return getStringFunction().upperCase(locale);
    }

    /**
     * See {@link StringFunction#lowerCase(Locale)}
     *
     * @param locale locale
     * @return the string function
     * @see StringFunction#lowerCase(Locale)
     */
    default StringFunction lowerCase(Locale locale) {
        return getStringFunction().lowerCase(locale);
    }

    /**
     * See {@link StringFunction#concat(String)}
     *
     * @param other string to concat
     * @return the string function
     * @see StringFunction#concat(String)
     */
    default StringFunction concat(String other) {
        return getStringFunction().concat(other);
    }

    /**
     * See {@link StringFunction#concat(Function)}
     *
     * @param other string function to concat
     * @return the string function
     * @see StringFunction#concat(Function)
     */
    default StringFunction concat(Function<String> other) {
        return getStringFunction().concat(other);
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
