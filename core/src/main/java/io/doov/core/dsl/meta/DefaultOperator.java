/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

public enum DefaultOperator implements Operator {
    rule("rule"), //
    validate("validate"), //
    empty("empty"), //
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
    equals("="), //
    not_equals("!="), //
    is_null("is null"), //
    is_not_null("is not null"), //
    as_a_number("as a number"), //
    as_string("as a string"), //
    as("as"), //
    with("with"), //
    minus("minus"), //
    plus("plus"), //
    after("after"), //
    after_or_equals("after or equals"), //
    age_at("age at"), //
    before("before"), //
    before_or_equals("before or equals"), //
    matches("matches"), //
    contains("contains"), //
    starts_with("starts with"), //
    ends_with("ends with"), //
    greater_than(">"), greater_or_equals(">="), //
    is("is"), //
    lesser_than("<"), //
    lesser_or_equals("<="), //
    has_not_size("has not size"), //
    has_size("has size"), //
    is_empty("is empty"), //
    is_not_empty("is not empty"), //
    length_is("length is"), //
    lambda("-function-"), //

    // TemporalAdjuster
    today("today"), //
    today_plus("today plus"), //
    today_minus("today minus"), //
    first_day_of_this_month("first day of this month"), //
    first_day_of_this_year("first day of this year"), //
    last_day_of_this_month("last day of this month"), //
    last_day_of_this_year("last day of this year"), //
    first_day_of_month("first day of month"), //
    first_day_of_next_month("first day of next month"), //
    first_day_of_year("first day of year"), //
    first_day_of_next_year("first day of next year"), //
    last_day_of_month("last day of month"), //
    last_day_of_year("last day of year");

    private final String readable;

    DefaultOperator(String readable) {
        this.readable = readable;
    }

    @Override
    public String readable() {
        return readable;
    }
}
