package org.modelmap.sample.model;

import org.modelmap.sample.field.SampleConstraint;
import org.modelmap.sample.field.SampleFieldId;
import org.modelmap.sample.field.SamplePath;

public class Identity {

    @SamplePath(field = SampleFieldId.ACCOUNT_ID, constraint = SampleConstraint.ACCOUNT)
    @SamplePath(field = SampleFieldId.USER_ID, constraint = SampleConstraint.USER)
    private long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
