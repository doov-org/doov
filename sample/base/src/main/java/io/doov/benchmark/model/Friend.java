package io.doov.benchmark.model;

import static io.doov.benchmark.model.BenchmarkFieldId.*;

public class Friend {

    @BenchmarkPath(field = FRIEND_NAME_1)
    @BenchmarkPath(field = FRIEND_NAME_2)
    @BenchmarkPath(field = FRIEND_NAME_3)
    @BenchmarkPath(field = FRIEND_NAME_4)
    @BenchmarkPath(field = FRIEND_NAME_5)
    @BenchmarkPath(field = FRIEND_NAME_6)
    @BenchmarkPath(field = FRIEND_NAME_7)
    @BenchmarkPath(field = FRIEND_NAME_8)
    @BenchmarkPath(field = FRIEND_NAME_9)
    private String name;

    public Friend() {
    }

    public Friend(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
