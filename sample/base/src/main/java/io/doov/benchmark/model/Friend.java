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
package io.doov.benchmark.model;

import static io.doov.benchmark.model.BenchmarkFieldId.*;

public class Friend {

    @BenchmarkPath(field = FRIEND_NAME_1)
    @BenchmarkPath(field = FRIEND_NAME_2)
    @BenchmarkPath(field = FRIEND_NAME_3)
    @BenchmarkPath(field = FRIEND_NAME_4)
    @BenchmarkPath(field = FRIEND_NAME_5)
    @BenchmarkPath(field = FRIEND_NAME_6)
    @BenchmarkPath(field = FRIEND_NAME_7)
    @BenchmarkPath(field = FRIEND_NAME_8)
    @BenchmarkPath(field = FRIEND_NAME_9)
    private String name;

    public Friend() {
    }

    public Friend(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
