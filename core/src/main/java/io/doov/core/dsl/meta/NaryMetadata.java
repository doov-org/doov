/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

import io.doov.core.dsl.lang.StepCondition;

public class NaryMetadata extends AbstractMetadata {

    private final String operator;
    private final List<StepCondition> values;

    private NaryMetadata(String operator, StepCondition... values) {
        this.operator = operator;
        this.values = asList(values);
    }

    public static NaryMetadata matchAnyMetadata(StepCondition... values) {
        return new NaryMetadata("match any", values);
    }

    public static NaryMetadata matchAllMetadata(StepCondition... values) {
        return new NaryMetadata("match all", values);
    }

    public static NaryMetadata matchNoneMetadata(StepCondition... values) {
        return new NaryMetadata("match none", values);
    }

    @Override
    public String readable() {
        String readables = values.stream().map(StepCondition::readable).collect(joining(", "));
        return "(" + operator + " [" + readables + "])";
    }

}
