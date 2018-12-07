/*
 * Copyright 2018 Courtanet
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
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.DOOV.min;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static io.doov.core.dsl.lang.ReduceType.SUCCESS;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.collectMetadata;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

public class MinFunctionTest {
    private Result result;
    private Metadata reduce;
    private GenericModel model;
    private IntegerFieldInfo zeroField, oneField;

    @BeforeEach
    void beforeEach() {
        this.model = new GenericModel();
        this.zeroField = model.intField(0, "0");
        this.oneField = model.intField(1, "1");
    }

    @Test
    void reduce_min_success() {
        result = when(min(zeroField, oneField).eq(0)).validate().withShortCircuit(false).executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(collectMetadata(reduce));
    }
    
    
    @Test
    void reduce_min_failure() {
        result = when(min(zeroField, oneField).eq(1)).validate().withShortCircuit(false).executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(collectMetadata(reduce));
    }
    
    

    @AfterEach
    void afterEach() {
        System.out.println("> " + reduce);
    }
}
