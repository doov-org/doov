/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl;

import io.doov.benchmark.model.BenchmarkModel;
import io.doov.benchmark.model.Driver;
import io.doov.benchmark.model.Friend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.doov.benchmark.model.RuntimePaths.friend_name_1;
import static io.doov.benchmark.model.RuntimePaths.quote_1;
import static org.assertj.core.api.Assertions.assertThat;

public class BenchmarkRuntimeFieldTest {

    private BenchmarkModel model;

    private Driver driver;

    private List<Double> quotes;

    private List<Friend> friends;

    private Friend friend1;

    @BeforeEach
    void setUp() {
        model = new BenchmarkModel();
        driver = new Driver();
        model.setDriver(driver);

        quotes = new ArrayList<>();
        quotes.add(3.2);
        driver.setQuotes(quotes);

        friends = new ArrayList<>();
        friend1 = new Friend("Bob");
        friends.add(friend1);
        driver.setFriends(friends);
    }

    @Test
    void list_field() {
        assertThat(friend_name_1.get(model)).isEqualTo("Bob");
        friend_name_1.set(model, "Kate");
        assertThat(friends.get(0).getName()).isEqualTo("Kate");
    }

    @Test
    void field_in_list() {
        assertThat(quote_1.get(model)).isEqualTo(3.2);
        quote_1.set(model, 2.0);
        assertThat(quotes.get(0)).isEqualTo(2.0);
    }

}
