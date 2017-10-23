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

    void visit(ValidationRule validationRule);

    void visit(StepWhen stepWhen);

    void visit(StepCondition stepCondition);

}
