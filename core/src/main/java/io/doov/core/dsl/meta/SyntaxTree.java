package io.doov.core.dsl.meta;

public interface SyntaxTree {

    void accept(MetadataVisitor visitor);

}
