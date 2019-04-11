/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.string;

import io.doov.core.dsl.grammar.Apply1;
import io.doov.core.dsl.grammar.Value;

public class ParseInt extends Apply1<String,Integer> {

    public ParseInt(Value<String> input) {
        super(Integer.class, input);
    }
}
