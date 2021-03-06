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
package io.doov.core.serial;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import io.doov.core.serial.adapter.*;

/**
 * Default implementation of Type adapter registry
 */
public class TypeAdapters implements TypeAdapterRegistry {

    public static TypeAdapters INSTANCE = new TypeAdapters();

    protected static List<TypeAdapter> TYPE_ADAPTERS = Arrays.asList(
                    new BooleanTypeAdapter(),
                    new IntegerTypeAdapter(),
                    new DoubleTypeAdapter(),
                    new LongTypeAdapter(),
                    new FloatTypeAdapter(),
                    new StringTypeAdapter(),
                    new DateTypeAdapter(),
                    new LocalDateTypeAdapter(),
                    new CodeValuableEnumTypeAdapter()
    );

    @Override
    public Stream<TypeAdapter> stream() {
        return TYPE_ADAPTERS.stream();
    }

}
