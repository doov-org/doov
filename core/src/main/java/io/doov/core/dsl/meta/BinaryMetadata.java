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

import io.doov.core.dsl.lang.StepCondition;

public class BinaryMetadata extends AbstractMetadata {

    private final StepCondition left;
    private final String operator;
    private final StepCondition right;

    private BinaryMetadata(StepCondition left, String operator, StepCondition right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public static BinaryMetadata andMetadata(StepCondition left, StepCondition right) {
        return new BinaryMetadata(left, "and", right);
    }

    public static BinaryMetadata orMetadata(StepCondition left, StepCondition right) {
        return new BinaryMetadata(left, "or", right);
    }

    @Override
    public String readable() {
        return "(" + (left.readable() + " " + operator + " " + right.readable()) + ")";
    }

}
