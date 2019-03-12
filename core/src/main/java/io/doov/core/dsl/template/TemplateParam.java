/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import static io.doov.core.dsl.meta.function.TemplateParamMetadata.templateParamMetadata;

import java.util.function.Function;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.DelegatingFieldInfo;
import io.doov.core.dsl.meta.Metadata;

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
        private FieldId templateFieldId;

        ProxyFieldInfo(String readable) {
            this.unInitReadable = readable;
            this.templateFieldId = () -> readable;
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
            return fieldInfo != null ? fieldInfo.id() : templateFieldId;
        }

        @Override
        public Metadata getMetadata() {
            return templateParamMetadata(unInitReadable, fieldInfo);
        }
    }
}
