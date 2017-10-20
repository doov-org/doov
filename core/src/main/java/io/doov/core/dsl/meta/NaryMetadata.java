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

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

import io.doov.core.dsl.lang.StepCondition;

public class NaryMetadata extends AbstractMetadata {

    private final String operator;
    private final List<StepCondition> values;

    private NaryMetadata(String operator, StepCondition... values) {
        this.operator = operator;
        this.values = asList(values);
    }

    public static NaryMetadata matchAnyMetadata(StepCondition... values) {
        return new NaryMetadata("match any", values);
    }

    public static NaryMetadata matchAllMetadata(StepCondition... values) {
        return new NaryMetadata("match all", values);
    }

    public static NaryMetadata matchNoneMetadata(StepCondition... values) {
        return new NaryMetadata("match none", values);
    }

    @Override
    public String readable() {
        String readables = values.stream().map(StepCondition::readable).collect(joining(", "));
        return "(" + operator + " [" + readables + "])";
    }

}
