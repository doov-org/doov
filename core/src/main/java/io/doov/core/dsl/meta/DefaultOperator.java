/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.ReturnType.BOOLEAN;
import static io.doov.core.dsl.meta.ReturnType.OTHER;

public enum DefaultOperator implements Operator {
    no_operator("no operator"), //
    rule("rule"), //
    validate("validate"), //
    empty("empty"), //
    and("and", BOOLEAN), //
    or("or", BOOLEAN), //
    match_any("match any", BOOLEAN), //
    match_all("match all", BOOLEAN), //
    match_none("match none", BOOLEAN), //
    any_match_values("match any", BOOLEAN), //
    all_match_values("match all", BOOLEAN), //
    none_match_values("match none", BOOLEAN), //
    count("count"), //
    sum("sum"), //
    min("min"), //
    not("not", BOOLEAN), //
    always_true("always true", BOOLEAN), //
    always_false("always false", BOOLEAN), //
    times("times"), //
    when("when"), //
    equals("=", BOOLEAN), //
    tags("tags"), //
    position("position"), //
    not_equals("!=", BOOLEAN), //
    is_null("is null", BOOLEAN), //
    is_not_null("is not null", BOOLEAN), //
    as_a_number("as a number"), //
    as_string("as a string"), //
    as("as"), //
    with("with"), //
    minus("minus"), //
    plus("plus"), //
    temporal_minus("temporal_minus"), //
    after("after", BOOLEAN), //
    after_or_equals("after or equals", BOOLEAN), //
    age_at("age at"), //
    before("before", BOOLEAN), //
    before_or_equals("before or equals", BOOLEAN), //
    matches("matches", BOOLEAN), //
    contains("contains", BOOLEAN), //
    starts_with("starts with", BOOLEAN), //
    ends_with("ends with", BOOLEAN), //
    greater_than(">", BOOLEAN), //
    greater_or_equals(">=", BOOLEAN), //
    is("is", BOOLEAN), //
    lesser_than("<", BOOLEAN), //
    lesser_or_equals("<=", BOOLEAN), //
    has_not_size("has not size", BOOLEAN), //
    has_size("has size", BOOLEAN), //
    is_empty("is empty", BOOLEAN), //
    is_not_empty("is not empty", BOOLEAN), //
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
    last_day_of_year("last day of year"), //
    
    // Template
    template_field("|"), //
    ;

    private final String readable;
    private final ReturnType returnType;

    DefaultOperator(String readable) {
        this(readable, OTHER);
    }

    DefaultOperator(String readable, ReturnType returnType) {
        this.readable = readable;
        this.returnType = returnType;
    }

    @Override
    public ReturnType returnType() {
        return returnType;
    }

    @Override
    public String readable() {
        return readable;
    }
}
