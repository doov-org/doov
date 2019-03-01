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

import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;

import java.util.List;
import java.util.stream.Stream;

public class NaryMetadata extends AbstractMetadata {
    private final Operator operator;
    private final List<Metadata> values;

    public NaryMetadata(Operator operator, List<Metadata> values) {
        this.operator = operator;
        this.values = values;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    public List<Metadata> getValues() {
        return values;
    }

    @Override
    public MetadataType type() {
        return NARY_PREDICATE;
    }

    @Override
    public Stream<Metadata> right() {
        return values.stream();
    }
}
