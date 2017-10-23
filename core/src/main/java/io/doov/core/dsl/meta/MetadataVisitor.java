package io.doov.core.dsl.meta;

import io.doov.core.dsl.lang.*;

public interface MetadataVisitor {

    void visit(Metadata metadata);

    void visit(FieldMetadata fieldMetadata);

    void visit(UnaryMetadata unaryMetadata);

    void start(BinaryMetadata binaryMetadata);

    void visit(BinaryMetadata binaryMetadata);

    void end(BinaryMetadata binaryMetadata);

    void start(NaryMetadata naryMetadata);

    void visit(NaryMetadata naryMetadata);

    void end(NaryMetadata naryMetadata);

    void start(ValidationRule validationRule);

    void visit(ValidationRule validationRule);

    void end(ValidationRule validationRule);

    void start(StepWhen stepWhen);

    void visit(StepWhen stepWhen);

    void end(StepWhen stepWhen);

    void visit(StepCondition stepCondition);

}
