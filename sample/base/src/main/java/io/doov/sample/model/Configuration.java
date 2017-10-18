package io.doov.sample.model;

import io.doov.sample.field.SampleFieldId;
import io.doov.sample.field.SamplePath;

public class Configuration {

    @SamplePath(field = SampleFieldId.EMAIL_MAX_SIZE, readable = "configuration max email size")
    private int maxEmailSize;

    public int getMaxEmailSize() {
        return maxEmailSize;
    }

    public void setMaxEmailSize(int maxEmailSize) {
        this.maxEmailSize = maxEmailSize;
    }

}
