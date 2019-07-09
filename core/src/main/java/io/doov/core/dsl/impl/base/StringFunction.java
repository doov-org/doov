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
package io.doov.core.dsl.impl.base;

import static io.doov.core.dsl.meta.function.StringFunctionMetadata.*;
import static io.doov.core.dsl.meta.predicate.UnaryPredicateMetadata.lengthIsMetadata;
import static io.doov.core.dsl.meta.predicate.UnaryPredicateMetadata.trimMetadata;

import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.types.ContextAccessor;
import io.doov.core.dsl.field.types.Function;
import io.doov.core.dsl.impl.DefaultFunction;
import io.doov.core.dsl.impl.LeafStepCondition;
import io.doov.core.dsl.impl.num.IntegerFunction;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.function.StringFunctionMetadata;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

/**
 * Base class for string conditions.
 * <p>
 * It contains a {@link DslField} to get the value from the model, a {@link StringFunctionMetadata} to describe this
 * node, and a {@link BiFunction} to take the value from the model and return an optional value.
 */
public class StringFunction extends DefaultFunction<String, PredicateMetadata> {

    public StringFunction(PredicateMetadata metadata, BiFunction<FieldModel, Context, Optional<String>> value) {
        super(metadata, value);
    }

    /**
     * Returns a condition checking if the node value contains the given value.
     *
     * @param value the value
     * @return the step condition
     */
    public final StepCondition contains(String value) {
        return LeafStepCondition.stepCondition(containsMetadata(metadata, value), getFunction(), value,
                String::contains);
    }

    /**
     * Returns a condition checking if the node value contains the given value.
     *
     * @param value another StringCondition
     * @return the step condition
     */
    public final StepCondition contains(StringFunction value) {
        return LeafStepCondition.stepCondition(containsMetadata(metadata, value), getFunction(), value.getFunction(),
                String::contains);
    }

    /**
     * Returns a condition checking if the node value matches the given value.
     *
     * @param value the value
     * @return the step condition
     */
    public final StepCondition matches(String value) {
        return LeafStepCondition.stepCondition(matchesMetadata(metadata, value), getFunction(), value, String::matches);
    }

    /**
     * Returns a condition checking if the node value starts with the given value.
     *
     * @param value the value
     * @return the step condition
     */
    public final StepCondition startsWith(String value) {
        return LeafStepCondition.stepCondition(startsWithMetadata(metadata, value), getFunction(), value,
                String::startsWith);
    }

    /**
     * Returns a condition checking if the node value ends with the given value.
     *
     * @param value the value
     * @return the step condition
     */
    public final StepCondition endsWith(String value) {
        return LeafStepCondition.stepCondition(endsWithMetadata(metadata, value), getFunction(), value,
                String::endsWith);
    }

    /**
     * Returns an integer condition that returns the node value length.
     *
     * @return the integer condition
     */
    public IntegerFunction length() {
        return new IntegerFunction(lengthIsMetadata(metadata),
                (model, context) -> value(model, context).map(String::length));
    }

    /**
     * Returns an integer condition that returns the node value as an integer.
     *
     * @return the integer condition
     */
    public IntegerFunction parseInt() {
        return new IntegerFunction(metadata,
                (model, context) -> value(model, context).map(Integer::parseInt));
    }

    /**
     * Returns an string function that returns the node value as trimmed.
     *
     * @return the string function
     */
    public StringFunction trim() {
        return new StringFunction(trimMetadata(metadata),
                (model, context) -> value(model, context).map(String::trim));
    }

    /**
     * Returns an string condition that returns the node value as replaced.
     *
     * @param regex regex string
     * @param replacement replacement string
     * @return the string function
     */
    public StringFunction replaceAll(String regex, String replacement) {
        return new StringFunction(replaceAllMetadata(metadata, regex, replacement),
                (model, context) -> value(model, context).map(s -> s.replaceAll(regex, replacement)));
    }

    /**
     * Returns an string condition that returns the node value as replaced.
     *
     * @param regexFunction regex function
     * @param replacementFunction replacement function
     * @return the string function
     */
    public StringFunction replaceAll(Function<String> regexFunction, Function<String> replacementFunction) {
        return new StringFunction(replaceAllMetadata(metadata, regexFunction, replacementFunction),
                (model, context) -> value(model, context).map(s -> {
                    Optional<String> regex = regexFunction.value(model, context);
                    Optional<String> replacement = replacementFunction.value(model, context);
                    if (regex.isPresent() && replacement.isPresent()) {
                        return s.replaceAll(regex.get(), replacement.get());
                    } else {
                        return s;
                    }
                }));
    }

    /**
     * Returns an string condition that returns the node value as substring.
     *
     * @param beginIndex begin index
     * @param endIndex end index
     * @return the string function
     */
    public StringFunction substring(int beginIndex, int endIndex) {
        return new StringFunction(substringMetadata(metadata, beginIndex, endIndex),
                (model, context) -> value(model, context).map(s -> s.substring(beginIndex, endIndex)));
    }

    /**
     * Returns an string condition that returns the node value as substring.
     *
     * @param beginIndexFunction begin index function
     * @param endIndexFunction end index function
     * @return the string function
     */
    public StringFunction substring(Function<Integer> beginIndexFunction, Function<Integer> endIndexFunction) {
        return new StringFunction(substringMetadata(metadata, beginIndexFunction, endIndexFunction),
                (model, context) -> value(model, context).map(s -> {
                    Optional<Integer> beginIndex = beginIndexFunction.value(model, context);
                    Optional<Integer> endIndex = endIndexFunction.value(model, context);
                    if (beginIndex.isPresent() && endIndex.isPresent()) {
                        return s.substring(beginIndex.get(), endIndex.get());
                    } else {
                        return s;
                    }
                }));
    }

    /**
     * Returns an string condition that returns the node value as upper case.
     *
     * @param locale locale
     * @return the string function
     */
    public StringFunction upperCase(Locale locale) {
        return new StringFunction(upperCaseMetadata(metadata, locale),
                (model, context) -> value(model, context).map(s -> s.toUpperCase(locale)));
    }

    /**
     * Returns an string condition that returns the node value as lower case.
     *
     * @param locale locale
     * @return the string function
     */
    public StringFunction lowerCase(Locale locale) {
        return new StringFunction(lowerCaseMetadata(metadata, locale),
                (model, context) -> value(model, context).map(s -> s.toLowerCase(locale)));
    }

    /**
     * Returns an string condition that returns the node value as concat to given string.
     *
     * @param other string to concat
     * @return the string function
     */
    public StringFunction concat(String other) {
        return new StringFunction(concatMetadata(metadata, other),
                (model, context) -> value(model, context)
                        .map(s -> s.concat(other)));
    }

    /**
     * Returns an string condition that returns the node value as concat to given string.
     *
     * @param otherStringFunction string function to concat
     * @return the string function
     */
    public StringFunction concat(Function<String> otherStringFunction) {
        return new StringFunction(concatMetadata(metadata, otherStringFunction),
                (model, context) -> value(model, context)
                        .map(s -> otherStringFunction.value(model, context).map(s::concat).orElse(s)));
    }

}
