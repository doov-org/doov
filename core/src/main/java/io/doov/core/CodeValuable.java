/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core;

import java.util.Arrays;

public interface CodeValuable {

    default String getCode() {
        return ((Enum<?>) this).name();
    }

    class Helper {

        public static <C extends CodeValuable> C parseCode(Class<C> type, String value, C defaultValue) {
            return Arrays.stream(type.getEnumConstants()).filter(e -> e.getCode().equals(value)).findFirst()
                            .orElse(defaultValue);
        }
    }

}
