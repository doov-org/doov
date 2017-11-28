package io.doov.core.dsl.meta;

import java.lang.reflect.Field;

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
