/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.lang;

import static io.doov.core.dsl.DOOV.map;
import static io.doov.core.dsl.DOOV.mappings;
import static io.doov.core.dsl.DOOV.template;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.template.ParameterTypes.$String;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.impl.DefaultContext;
import io.doov.core.dsl.mapping.MappingRegistry;
import io.doov.core.dsl.runtime.GenericModel;

public class ContextAuditTest {
    private GenericModel model = new GenericModel();
    private StringFieldInfo stringField = model.stringField("s1", "stringField");
    private StringFieldInfo stringField2 = model.stringField("s2", "stringField2");
    private MappingRegistry rule = template($String, $String)
                    .mappings((site, url) -> mappings(when(site.eq("bing")).then(map("www.bingue.com").to(url)),
                                    when(site.eq("Google")).then(map("www.gougeule.com").to(url)),
                                    when(site.eq("Yahoo")).then(map("www.yahou.com").to(url))))
                    .bind(stringField, stringField2);
    private Context context = Mockito.spy(new DefaultContext(rule.metadata()));

    @Test
    void beforeValidate() {
        model.set(stringField, "bing");
        rule.executeOn(model, model, context);
        verify(context, times(3)).afterValidate(Mockito.any(ValidationRule.class));
    }

    @Test
    void afterValidate() {
        model.set(stringField, "bing");
        rule.executeOn(model, model, context);
        verify(context, times(3)).afterValidate(Mockito.any(ValidationRule.class));
    }

    @Test
    void beforeMapping() {
        model.set(stringField, "bing");
        rule.executeOn(model, model, context);
        verify(context, times(1)).beforeMapping(Mockito.any(MappingRule.class));
    }

    @Test
    void afterMapping() {
        model.set(stringField, "bing");
        rule.executeOn(model, model, context);
        verify(context, times(1)).afterMapping(Mockito.any(MappingRule.class));
    }

    @Test
    void beforeConditionalMapping() {
        model.set(stringField, "bing");
        rule.executeOn(model, model, context);
        verify(context, times(3)).beforeConditionalMapping(Mockito.any(ConditionalMappingRule.class));
    }

    @Test
    void afterConditionalMapping() {
        model.set(stringField, "bing");
        rule.executeOn(model, model, context);
        verify(context, times(3)).afterConditionalMapping(Mockito.any(ConditionalMappingRule.class));
    }

    @Test
    void beforeMappingRegistry() {
        model.set(stringField, "bing");
        rule.executeOn(model, model, context);
        verify(context, times(2)).beforeMappingRegistry(Mockito.any(MappingRegistry.class));
    }

    @Test
    void afterMappingRegistry() {
        model.set(stringField, "bing");
        rule.executeOn(model, model, context);
        verify(context, times(2)).afterMappingRegistry(Mockito.any(MappingRegistry.class));
    }
}
