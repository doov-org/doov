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
package io.doov.core.dsl.meta;

import io.doov.core.dsl.lang.*;

public interface MetadataVisitor {

    // Metadata

    void visit(Metadata metadata);

    // FieldMetadata

    void start(FieldMetadata fieldMetadata);

    void visit(FieldMetadata fieldMetadata);

    void end(FieldMetadata fieldMetadata);

    // UnaryMetadata

    void visit(UnaryMetadata unaryMetadata);

    // BinaryMetadata

    void start(BinaryMetadata binaryMetadata);

    void visit(BinaryMetadata binaryMetadata);

    void end(BinaryMetadata binaryMetadata);

    // NaryMetadata

    void start(NaryMetadata naryMetadata);

    void visit(NaryMetadata naryMetadata);

    void end(NaryMetadata naryMetadata);

    // ValidationRule

    void start(ValidationRule validationRule);

    void visit(ValidationRule validationRule);

    void end(ValidationRule validationRule);

    // StepWhen

    void start(StepWhen stepWhen);

    void visit(StepWhen stepWhen);

    void end(StepWhen stepWhen);

    // StepCondition

    void visit(StepCondition stepCondition);

}
