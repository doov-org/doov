package io.doov.core.dsl.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import io.doov.core.dsl.lang.MappingRegistry;
import io.doov.core.dsl.lang.MappingRule;

public class DefaultMappingRegistry implements MappingRegistry {

    private final List<MappingRule> mappingRules;

    public DefaultMappingRegistry() {
        mappingRules = new ArrayList<>();
    }

    public static MappingRegistry mappings(MappingRule... mappingRules) {
        DefaultMappingRegistry defaultMappingRegistry = new DefaultMappingRegistry();
        for (MappingRule mappingRule : mappingRules) {
            defaultMappingRegistry.register(mappingRule);
        }
        return defaultMappingRegistry;
    }

    @Override
    public void register(MappingRule rule) {
        mappingRules.add(rule);
    }

    @Override
    public void registerAll(MappingRegistry registry) {
        registry.stream().forEach(m -> m.registerOn(this)) ;
    }

    @Override
    public Stream<MappingRule> stream() {
        return mappingRules.stream();
    }
}
