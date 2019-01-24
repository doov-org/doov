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
package io.doov.core.dsl.runtime;

import java.util.*;
import java.util.function.*;

public class ListPathMethod<T, R> implements PathMethod<T, R> {

    private final Supplier<R> supplier;
    private final Function<T, List<R>> readMethod;
    private final BiConsumer<T, List<R>> writeMethod;
    private final int position;

    public ListPathMethod(Supplier<R> supplier, Function<T, List<R>> readMethod,
                          BiConsumer<T, List<R>> writeMethod, int position) {
        this.supplier = supplier;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.position = position;
    }

    @Override
    public R get(T link) {
        return Optional.ofNullable(readMethod.apply(link))
                        .filter(l -> l.size() >= position)
                        .map(l -> l.get(position - 1))
                        .orElse(null);
    }

    @Override
    public void set(T link, R value) {
        readMethod.apply(link).add(position - 1, value);
    }

    @Override
    public R create(T link) {
        List<R> list = readMethod.apply(link);
        if (list == null) {
            list = new ArrayList<>();
            writeMethod.accept(link, list);
        }
        for (int i = 0; i <= position - 1; i++) {
            if (list.size() <= i) {
                list.add(i, null);
            }
        }
        R r = supplier.get();
        list.set(position - 1, r);
        return r;
    }

}
