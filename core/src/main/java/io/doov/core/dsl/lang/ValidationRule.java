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
package io.doov.core.dsl.lang;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.meta.SyntaxTree;

public interface ValidationRule extends Readable, SyntaxTree {

    ValidationRule withMessage(String message);

    ValidationRule withShortCircuit(boolean shortCircuit);

    Result executeOn(DslModel model);

    ValidationRule registerOn(RuleRegistry registry);

    String getMessage();

}
