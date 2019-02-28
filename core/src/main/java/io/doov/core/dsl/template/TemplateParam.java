/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.DelegatingFieldInfo;

import java.util.function.Function;

public class TemplateParam<T extends DslField<?>> {

    @SuppressWarnings("unchecked")
    public static <T> Class<T> generify(Class<?> cls) {
        return (Class<T>) cls;
    }

    public final Class<T> type;
    private ProxyFieldInfo fieldInfo;
    private Function<FieldInfo, T> constructorRef;

    public TemplateParam(Class<T> type, String parameterIdentifier, Function<FieldInfo, T> constructorRef) {
        this.type = type;
        this.fieldInfo = new ProxyFieldInfo(parameterIdentifier);
        this.constructorRef = constructorRef;
    }

    public T create() {
        return constructorRef.apply(fieldInfo);
    }

    public TemplateParam<T> bind(T field) {
        // TODO remove this cast
        fieldInfo.setFieldInfo((FieldInfo) field);
        return this;
    }

    private static class ProxyFieldInfo implements DelegatingFieldInfo {

        private FieldInfo fieldInfo;
        private String unInitReadable;
        private FieldId unInitFieldId;

        ProxyFieldInfo(String readable) {
            this.unInitReadable = readable;
            this.unInitFieldId = () -> readable;
        }

        public void setFieldInfo(FieldInfo fieldInfo) {
            this.fieldInfo = fieldInfo;
        }

        @Override
        public FieldInfo delegate() {
            return fieldInfo;
        }

        @Override
        public FieldId id() {
            return fieldInfo != null ? fieldInfo.id() : unInitFieldId;
        }

        @Override
        public String readable() {
            return fieldInfo != null ? fieldInfo.readable() : unInitReadable;
        }
    }
}
