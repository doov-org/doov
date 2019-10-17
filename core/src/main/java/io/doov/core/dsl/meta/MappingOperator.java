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

import static io.doov.core.dsl.meta.ReturnType.OTHER;

public enum MappingOperator implements Operator {
    // other
    fields("fields"),
    and("and"),
    then("then"),
    _else("else"),

    // mappings
    map("map"),
    map_null_tag("map_null_tag"),
    mappings("mappings"),
    conditional_mappings("mappings"),
    to("to"),
    using("using"),
    ;
    private final String readable;

    MappingOperator(String readable) {
        this.readable = readable;
    }

    @Override
    public String readable() {
        return readable;
    }

    @Override
    public ReturnType returnType() {
        return OTHER;
    }
}
