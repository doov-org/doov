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

public class UnaryMetadata implements Metadata {

    private final String operator;
    private final Metadata value;

    private UnaryMetadata(String operator, Metadata value) {
        this.operator = operator;
        this.value = value;
    }

    public static UnaryMetadata notMetadata(Metadata value) {
        return new UnaryMetadata("not", value);
    }

    @Override
    public String readable() {
        return "(" + operator + " " + value.readable() + ")";
    }

    @Override
    public void accept(MetadataVisitor visitor) {
        visitor.visit(this);
        visitor.visit(value);
    }

    public String getOperator() {
        return operator;
    }

}
