/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.gen.processor;

import java.util.Map;

public class DefaultEvaluator implements Evaluator {

    @Override
    public String eval(Map<String, Object> conf, String key, String replacement, MacroParamProcessor paramProcessor) {
        if (conf == null) {
            return replacement;
        }
        Object value = conf.get(key);
        if (value == null) {
            // If the key is unknown we try to call a MacroParamProcessor to process it.
            if (paramProcessor != null) {
                value = paramProcessor.processParam(key);
                if (value != null) {
                    conf.put(key, value);
                }
            }
        }
        if (value == null && replacement != null) {
            return replacement;
        } else if (value == null) {
            return MacroProcessor.REF_PREFIX + key + MacroProcessor.REF_SUFFIX;
        }
        return value.toString();
    }
}
