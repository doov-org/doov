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
package io.doov.sample2.model;

import org.junit.jupiter.api.RepeatedTest;

import io.doov.core.FieldModels;

public class Sample2StreamTest {

    @RepeatedTest(1)
    public void stream_parallel_apply_count() {
        new Sample2ModelWrapper(Sample2Models.sample()).parallelStream().peek(e -> doSomeStuff()).count();
    }

    @RepeatedTest(1)
    public void stream_sequential_apply_count() {
        new Sample2ModelWrapper(Sample2Models.sample()).stream().peek(e -> doSomeStuff()).count();
    }

    @RepeatedTest(1)
    public void stream_parallel_apply_wait_collect_map() {
        new Sample2ModelWrapper(Sample2Models.sample()).parallelStream()
                .peek(e -> doSomeStuff())
                .collect(FieldModels.toConcurrentFieldModel(new Sample2ModelWrapper()));
    }

    @RepeatedTest(1)
    public void stream_parallel_apply_wait_collect_concurrent() {
        new Sample2ModelWrapper(Sample2Models.sample()).parallelStream()
                .peek(e -> doSomeStuff())
                .collect(FieldModels.toConcurrentFieldModel(new Sample2ModelWrapper()));
    }

    @RepeatedTest(1)
    public void stream_sequential_apply_wait_collect() {
        new Sample2ModelWrapper(Sample2Models.sample()).stream()
                .peek(e -> doSomeStuff())
                .collect(FieldModels.toFieldModel(new Sample2ModelWrapper()));
    }

    private static void doSomeStuff() {
        try {
            Thread.sleep(2);
        } catch (Exception ex) {
            // ignored
        }
    }

}
