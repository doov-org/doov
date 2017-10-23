package io.doov.core.dsl.meta.ast;

import java.util.stream.IntStream;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public class SyntaxTreePrinter implements MetadataVisitor {

    private static final int INDENT = 4;

    private int indent = 0;

    private int indentSize() {
        return indent * INDENT;
    }

    @Override
    public void visit(Metadata metadata) {
        IntStream.range(0, indent * INDENT).forEach(i -> System.out.print(" "));
        System.out.println("visit Metadata " + metadata);
    }

    @Override
    public void visit(FieldMetadata fieldMetadata) {
        IntStream.range(0, indent * INDENT).forEach(i -> System.out.print(" "));
        System.out.println("visit FieldMetadata " + fieldMetadata);
    }

    @Override
    public void visit(UnaryMetadata unaryMetadata) {
        IntStream.range(0, indent * INDENT).forEach(i -> System.out.print(" "));
        System.out.println("visit UnaryMetadata " + unaryMetadata);
    }

    @Override
    public void start(BinaryMetadata binaryMetadata) {
        IntStream.range(0, indent * INDENT).forEach(i -> System.out.print(" "));
        System.out.println("start BinaryMetadata " + binaryMetadata);
        indent++;
    }

    @Override
    public void visit(BinaryMetadata binaryMetadata) {
        IntStream.range(0, indent * INDENT).forEach(i -> System.out.print(" "));
        System.out.println("visit BinaryMetadata " + binaryMetadata);
    }

    @Override
    public void end(BinaryMetadata binaryMetadata) {
        indent--;
        IntStream.range(0, indent * INDENT).forEach(i -> System.out.print(" "));
        System.out.println("end BinaryMetadata " + binaryMetadata);
    }

    @Override
    public void start(NaryMetadata naryMetadata) {
        IntStream.range(0, indent * INDENT).forEach(i -> System.out.print(" "));
        System.out.println("start NaryMetadata " + naryMetadata);
        indent++;
    }

    @Override
    public void visit(NaryMetadata naryMetadata) {
        IntStream.range(0, indent * INDENT).forEach(i -> System.out.print(" "));
        System.out.println("visit NaryMetadata " + naryMetadata);
    }

    @Override
    public void end(NaryMetadata naryMetadata) {
        indent--;
        IntStream.range(0, indent * INDENT).forEach(i -> System.out.print(" "));
        System.out.println("end NaryMetadata " + naryMetadata);
    }

    @Override
    public void visit(ValidationRule validationRule) {
        IntStream.range(0, indent * INDENT).forEach(i -> System.out.print(" "));
        System.out.println("visit ValidationRule " + validationRule);
    }

    @Override
    public void visit(StepWhen stepWhen) {
        IntStream.range(0, indent * INDENT).forEach(i -> System.out.print(" "));
        System.out.println("visit StepWhen " + stepWhen);
    }

    @Override
    public void visit(StepCondition stepCondition) {
        IntStream.range(0, indent * INDENT).forEach(i -> System.out.print(" "));
        System.out.println("visit StepCondition " + stepCondition);
    }

}