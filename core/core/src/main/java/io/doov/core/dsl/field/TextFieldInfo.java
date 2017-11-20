package io.doov.core.dsl.field;

import io.doov.core.dsl.impl.IntegerCondition;
import io.doov.core.dsl.impl.StringCondition;
import io.doov.core.dsl.lang.StepCondition;

public interface TextFieldInfo extends BaseFieldInfo<String> {

    default StepCondition contains(String regex) {
        return getStringCondition().contains(regex);
    }

    default StepCondition matches(String regex) {
        return getStringCondition().matches(regex);
    }

    default StepCondition startsWith(String prefix) {
        return getStringCondition().startsWith(prefix);
    }

    default StepCondition endsWith(String suffix) {
        return getStringCondition().endsWith(suffix);
    }

    default IntegerCondition length() {
        return getStringCondition().length();
    }

    default IntegerCondition parseInt() {
        return getStringCondition().parseInt();
    }

    StringCondition getStringCondition();

}
