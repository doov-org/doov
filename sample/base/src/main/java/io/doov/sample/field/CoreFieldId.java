package io.doov.sample.field;

import static io.doov.sample.field.SampleTag.USER;

import java.util.Arrays;
import java.util.List;

import io.doov.core.FieldId;
import io.doov.core.TagId;

public enum CoreFieldId implements SampleField, FieldId {
    TEL(USER), //
    ADDRESS(USER), //

    ;

    private final int position;
    private final List<TagId> tags;

    CoreFieldId(TagId... tags) {
        this(-1, tags);
    }

    CoreFieldId(int position, TagId... tags) {
        this.position = position;
        this.tags = Arrays.asList(tags);
    }

    @Override
    public String code() {
        return name();
    }

    @Override
    public int position() {
        return position;
    }

    @Override
    public List<TagId> tags() {
        return tags;
    }
}
