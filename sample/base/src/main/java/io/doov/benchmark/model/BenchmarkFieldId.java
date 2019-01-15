package io.doov.benchmark.model;

import io.doov.core.FieldId;

public enum BenchmarkFieldId implements FieldId {

    NAME(),
    AGE(),
    DRIVING_LICENSE(),

    FRIEND_NAME_1(1),
    FRIEND_NAME_2(2),
    FRIEND_NAME_3(3),
    FRIEND_NAME_4(4),
    FRIEND_NAME_5(5),
    FRIEND_NAME_6(6),
    FRIEND_NAME_7(7),
    FRIEND_NAME_8(8),
    FRIEND_NAME_9(9),

    QUOTE_1(1),
    QUOTE_2(2),
    QUOTE_3(3),
    QUOTE_4(4),

    //
    ;

    private final int position;

    BenchmarkFieldId() {
        this(-1);
    }

    BenchmarkFieldId(int position) {
        this.position = position;
    }

    @Override
    public String code() {
        return name();
    }

    @Override
    public int position() {
        return position;
    }

}
