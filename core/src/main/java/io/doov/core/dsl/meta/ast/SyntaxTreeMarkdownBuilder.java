package io.doov.core.dsl.meta.ast;

import static java.util.stream.Collectors.joining;

import java.util.stream.IntStream;

import io.doov.core.dsl.meta.Readable;

public class SyntaxTreeMarkdownBuilder extends SyntaxTreeBuilder {

    private static final int INDENT_SIZE = 2;

    public SyntaxTreeMarkdownBuilder(StringBuilder stringBuilder) {
        super(stringBuilder);
    }

    @Override
    public int getIndentSize() {
        return INDENT_SIZE;
    }

    @Override
    public String formatIndent(int size) {
        return IntStream.range(0, size).mapToObj(i -> " ").collect(joining("")) + "* ";
    }

    @Override
    public String formatField(Readable field) {
        return "_" + field.readable() + "_";
    }

    @Override
    public String formatOperator(Readable operator) {
        return "__" + operator.readable() + "__";
    }

    @Override
    public String formatValue(Readable value) {
        return value.readable();
    }

    @Override
    public String formatWhen() {
        return "When\n\n";
    }

    @Override
    public String formatValidateWithMessage() {
        return "\nvalidate with message\n\n";
    }

}