/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.lang;

import io.doov.core.dsl.mapping.MappingRegistry;

public interface ContextAudit {
    default void beforeMapping(MappingRule rule) {
    }
    
    default void afterMapping(MappingRule rule) {
    }
    
    default void beforeConditionalMapping(ConditionalMappingRule rule) {
    }
    
    default void afterConditionalMapping(ConditionalMappingRule rule) {
    }
    
    default void beforeMappingRegistry(MappingRegistry registry) {
    }
    
    default void afterMappingRegistry(MappingRegistry registry) {
    }
    
    default void beforeValidate(ValidationRule rule) {
    }
    
    default void afterValidate(ValidationRule rule) {
    }
}
