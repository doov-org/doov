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

import io.doov.sample.field.SampleFieldId;
import io.doov.sample.field.SamplePath;

public class Configuration {

    @SamplePath(field = SampleFieldId.EMAIL_MAX_SIZE, readable = "configuration max email size")
    private int maxEmailSize;

    public int getMaxEmailSize() {
        return maxEmailSize;
    }

    public void setMaxEmailSize(int maxEmailSize) {
        this.maxEmailSize = maxEmailSize;
    }

}
