package io.doov.core.dsl.meta;

public class TextMetadata implements Metadata {

    private final String text;

    public TextMetadata(String text) {
        this.text = text;
    }

    @Override
    public String readable() {
        return text;
    }

    @Override
    public void accept(MetadataVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Metadata merge(Metadata other) {
        return new TextMetadata(readable() + " " + other.readable());
    }

}
