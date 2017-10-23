package io.doov.core.dsl.meta.ast;

import static java.util.stream.Collectors.joining;

import java.util.stream.IntStream;

public class SyntaxTreeTextBuilder extends AbstractSyntaxTreeBuilder {

    private static final int INDENT_SIZE = 4;

    public SyntaxTreeTextBuilder(StringBuilder stringBuilder) {
        super(stringBuilder);
    }

    @Override
    public int getIndentSize() {
        return INDENT_SIZE;
    }

    @Override
    public String formatIndent(int size) {
        return IntStream.range(0, size).mapToObj(i -> " ").collect(joining(""));
    }

}