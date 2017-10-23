package io.doov.core.dsl.meta.ast;

import java.util.stream.IntStream;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public class SyntaxTreePrinter extends AbstractSyntaxTree {

    private static final int INDENT_SIZE = 4;

    @Override
    public void visit(Metadata metadata) {
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("visit Metadata " + metadata);
    }

    @Override
    public void visit(FieldMetadata fieldMetadata) {
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("visit FieldMetadata " + fieldMetadata + " = " + fieldMetadata.readable());
    }

    @Override
    public void visit(UnaryMetadata unaryMetadata) {
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("visit UnaryMetadata " + unaryMetadata + " = " + "-");
    }

    @Override
    public void start(BinaryMetadata binaryMetadata) {
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("start BinaryMetadata " + binaryMetadata + " = " + "-");
        super.start(binaryMetadata);
    }

    @Override
    public void visit(BinaryMetadata binaryMetadata) {
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("visit BinaryMetadata " + binaryMetadata + " = " + "-");
    }

    @Override
    public void end(BinaryMetadata binaryMetadata) {
        super.end(binaryMetadata);
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("end BinaryMetadata " + binaryMetadata + " = " + "-");
    }

    @Override
    public void start(NaryMetadata naryMetadata) {
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("start NaryMetadata " + naryMetadata + " = " + naryMetadata.getOperator());
        super.start(naryMetadata);
    }

    @Override
    public void visit(NaryMetadata naryMetadata) {
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("visit NaryMetadata " + naryMetadata + " = " + "-");
    }

    @Override
    public void end(NaryMetadata naryMetadata) {
        super.end(naryMetadata);
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("end NaryMetadata " + naryMetadata + " = " + "-");
    }

    @Override
    public void start(ValidationRule validationRule) {
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("start ValidationRule " + validationRule + " = " + "rule");
        super.start(validationRule);
    }

    @Override
    public void visit(ValidationRule validationRule) {
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("visit ValidationRule " + validationRule + " = " + "-");
    }

    @Override
    public void end(ValidationRule validationRule) {
        super.end(validationRule);
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("end ValidationRule " + validationRule + " = " + "-");
    }

    @Override
    public void start(StepWhen stepWhen) {
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("start StepWhen " + stepWhen + " = " + "when");
        super.start(stepWhen);
    }

    @Override
    public void visit(StepWhen stepWhen) {
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("visit StepWhen " + stepWhen + " = " + "-");
    }

    @Override
    public void end(StepWhen stepWhen) {
        super.end(stepWhen);
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("end StepWhen " + stepWhen + " = " + "-");
    }

    @Override
    public void visit(StepCondition stepCondition) {
        IntStream.range(0, getCurrentIndentSize()).forEach(i -> System.out.print(" "));
        System.out.println("visit StepCondition " + stepCondition + " = " + "-");
    }

    @Override
    int getIndentSize() {
        return INDENT_SIZE;
    }

}
