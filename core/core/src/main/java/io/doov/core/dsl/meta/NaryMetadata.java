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

import java.util.List;

import io.doov.core.dsl.meta.ast.AstVisitorUtils;

public class NaryMetadata implements Metadata {

    private final String operator;
    private final List<Metadata> values;

    private NaryMetadata(String operator, List<Metadata> values) {
        this.operator = operator;
        this.values = values;
    }

    public String getOperator() {
        return operator;
    }

    public static NaryMetadata matchAnyMetadata(List<Metadata> values) {
        return new NaryMetadata("match any", values);
    }

    public static NaryMetadata matchAllMetadata(List<Metadata> values) {
        return new NaryMetadata("match all", values);
    }

    public static NaryMetadata matchNoneMetadata(List<Metadata> values) {
        return new NaryMetadata("match none", values);
    }

    public static NaryMetadata countMetadata(List<Metadata> values) {
        return new NaryMetadata("count", values);
    }

    @Override
    public String readable() {
        return AstVisitorUtils.astToString(this);
    }

    @Override
    public void accept(MetadataVisitor visitor) {
        visitor.start(this);
        values.forEach(v -> {
            v.accept(visitor);
            visitor.visit(this);
        });
        visitor.end(this);
    }

    @Override
    public Metadata merge(Metadata other) {
        return new NaryMetadata(operator + " " + other.readable() + " for", values);
    }

}
