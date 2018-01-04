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

    void visit(Metadata metadata, int depth);

    // FieldMetadata

    void start(LeafMetadata fieldMetadata, int depth);

    void visit(LeafMetadata fieldMetadata, int depth);

    void end(LeafMetadata fieldMetadata, int depth);

    // UnaryMetadata

    void visit(UnaryMetadata unaryMetadata, int depth);

    // BinaryMetadata

    void start(BinaryMetadata binaryMetadata, int depth);

    void visit(BinaryMetadata binaryMetadata, int depth);

    void end(BinaryMetadata binaryMetadata, int depth);

    // NaryMetadata

    void start(NaryMetadata naryMetadata, int depth);

    void visit(NaryMetadata naryMetadata, int depth);

    void end(NaryMetadata naryMetadata, int depth);

    // ValidationRule

    void start(ValidationRule validationRule, int depth);

    void visit(ValidationRule validationRule, int depth);

    void end(ValidationRule validationRule, int depth);

    // StepWhen

    void start(StepWhen stepWhen, int depth);

    void visit(StepWhen stepWhen, int depth);

    void end(StepWhen stepWhen, int depth);

    // StepCondition

    void visit(StepCondition stepCondition, int depth);

}
