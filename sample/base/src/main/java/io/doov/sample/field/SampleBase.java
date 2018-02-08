/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.field;

import java.util.List;

import io.doov.core.AbstractWrapper;
import io.doov.core.FieldInfo;
import io.doov.sample.model.SampleModel;

public abstract class SampleBase extends AbstractWrapper<SampleModel> {

    private String metadata;

    public SampleBase(List<FieldInfo> fieldInfos, SampleModel model) {
        this(fieldInfos, null, model);
    }

    public SampleBase(List<FieldInfo> fieldInfos, String metadata, SampleModel model) {
        super(fieldInfos, model);
        this.metadata = metadata;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

}
