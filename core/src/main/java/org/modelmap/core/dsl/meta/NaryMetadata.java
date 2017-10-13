/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.meta;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

import org.modelmap.core.dsl.lang.StepCondition;

public class NaryMetadata implements Metadata {

    public static final String MATCH_ANY = "match any";
    public static final String MATCH_ALL = "match all";
    public static final String MATCH_NONE = "match none";

    public final String operator;
    public final List<StepCondition> values;

    public NaryMetadata(String operator, StepCondition... values) {
        this.operator = operator;
        this.values = asList(values);
    }

    @Override
    public String readable() {
        return "(" + operator + " [" + values.stream().map(StepCondition::readable).collect(joining(", ")) + "])";
    }

}
