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
package io.doov.core;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Enum type interface with overridable {@link #parseCode} implementation.
 *
 * @param <E> Enum type
 */
public interface CodeLookup<E extends Enum<E> & CodeValuable> extends CodeValuable {

    default E parseCode(String code) {
        return stream().filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    default Stream<E> stream() {
        return Arrays.stream((E[]) getClass().getEnumConstants());
    }

}
