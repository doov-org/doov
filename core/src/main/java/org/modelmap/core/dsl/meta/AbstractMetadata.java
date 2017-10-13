package org.modelmap.core.dsl.meta;

public abstract class AbstractMetadata implements Metadata {

    private Boolean result;

    @Override
    public void capturePredicateResult(boolean result) {
        this.result = result;
    }

    boolean nodeFails() {
        return Boolean.FALSE.equals(result);
    }

}
