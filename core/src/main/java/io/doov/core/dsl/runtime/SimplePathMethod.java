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

import java.util.function.*;

public class SimplePathMethod<T, R> implements PathMethod<T, R> {

    private final Supplier<R> supplier;
    private final Function<T, R> readMethod;
    private final BiConsumer<T, R> writeMethod;

    public SimplePathMethod(Supplier<R> supplier, Function<T, R> readMethod, BiConsumer<T, R> writeMethod) {
        this.supplier = supplier;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
    }

    @Override
    public R get(T link) {
        return readMethod.apply(link);
    }

    @Override
    public void set(T link, R value) {
        writeMethod.accept(link, value);
    }

    @Override
    public R create(T link) {
        R r = supplier.get();
        writeMethod.accept(link, r);
        return r;
    }

}
