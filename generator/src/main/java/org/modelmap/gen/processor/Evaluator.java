package org.modelmap.gen.processor;

import java.util.Map;

public interface Evaluator {

    String eval(Map<String, Object> conf, String key, String defaultReplacement, MacroParamProcessor paramProcessor);
}
