/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.utils;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonGrammar {

    public static abstract class JNode {

        abstract String json();

        @Override
        public String toString() {
            return json();
        }
    }

    static abstract class JValue<T> extends JNode {

        public final T value;

        protected JValue(T value) {
            this.value = value;
        }

        abstract String format();

        @Override
        String json() {
            return value != null ? format() : "\"null\"";
        }
    }

    public static class JNumber extends JValue<Number> {

        public JNumber(Number value) {
            super(value);
        }

        @Override
        String format() {
            return value.toString();
        }
    }

    public static class JString extends JValue<CharSequence> {

        public JString(CharSequence value) {
            super(value);
        }

        @Override
        String format() {
            return "\"" + value.toString().replace('\"', ' ') + "\"";
        }
    }

    public static class JBool extends JValue<Boolean> {

        public JBool(Boolean value) {
            super(value);
        }

        @Override
        String format() {
            return value.toString();
        }
    }

    public static class JArray extends JNode {

        public final Stream<JNode> values;

        public JArray(JNode... values) {
            this(Arrays.stream(values));
        }

        public JArray(Stream<JNode> values) {
            this.values = values;
        }

        @Override
        String json() {
            return "[" + values.map(JNode::json).collect(Collectors.joining(", ")) + "]";
        }
    }

    public static class JBind {

        public final String label;
        public final JNode bound;

        public JBind(String label, JNode bound) {
            this.label = label;
            this.bound = bound;
        }
    }

    public static class JObject extends JNode {

        public final Stream<JBind> bindings;

        public JObject(JBind... bindings) {
            this(Arrays.stream(bindings));
        }

        public JObject(Stream<JBind> bindings) {
            this.bindings = bindings;
        }

        @Override
        String json() {
            return "{" + bindings
                    .map(b -> "\"" + b.label + "\": " + b.bound.json())
                    .collect(Collectors.joining(", ")) + "}";
        }
    }
}
