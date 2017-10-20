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
package io.doov.sample.model;

import static io.doov.util.LoopingRule.doSomeStuff;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import io.doov.sample.model.SampleModelWrapper;
import io.doov.sample.model.SampleModels;
import io.doov.util.LoopingRule;

public class SampleStreamTest {
    @Rule
    public final TestRule looping = new LoopingRule(10);

    @Test
    public void stream_parallel_apply_wait() {
        new SampleModelWrapper(SampleModels.sample()).parallelStream().map(e -> {
            doSomeStuff();
            return e;
        }).count();
    }


    @Test
    public void stream_sequential_apply_wait() {
        new SampleModelWrapper(SampleModels.sample()).stream().map(e -> {
            doSomeStuff();
            return e;
        }).count();
    }
}
