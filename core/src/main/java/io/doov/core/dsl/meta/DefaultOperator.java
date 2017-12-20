/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

public enum DefaultOperator implements Operator {
    and("and"), //
    or("or"), //
    match_any("match any"), //
    match_all("match all"), //
    match_none("match none"), //
    count("count"), //
    sum("sum"), //
    min("min"), //
    not("not"), //
    always_true("always true"), //
    always_false("always false"), //
    times("times"), //
    when("when"), //
    equals("equals"), //
    not_equals("not equals"), //
    is_null("is null"), //
    is_not_null("is not null"), //
    as_a_number("as a number"), //
    with("with"), //
    minus("minus"), //
    plus("plus"), //
    after("after"), //
    age_at("age at"), //
    before("before"), //
    matches("matches"), //
    contains("contains"), //
    starts_with("starts with"), //
    ends_with("ends with"), //
    greater_than("greater than"),
    greater_or_equals("greater or equals"), //
    xor("xor"), //
    is("is"), //
    lesser_than("lesser than"), //
    lesser_or_equals("lesser or equals"), //
    has_not_size("has not size"), //
    has_size("has size"), //
    is_empty("is empty"), //
    is_not_empty("is not empty"),
    length_is("length is");

    private final String readable;

    DefaultOperator(String readable) {
        this.readable = readable;
    }

    @Override
    public String readable() {
        return readable;
    }
}
