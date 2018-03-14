package io.doov.core.dsl.mapping;

import java.util.*;
import java.util.stream.Stream;

import io.doov.core.dsl.lang.MappingRegistry;
import io.doov.core.dsl.lang.MappingRule;

public class DefaultMappingRegistry implements MappingRegistry {

    private final List<MappingRule> mappingRules;

    public static MappingRegistry mappings(MappingRule... mappingRules) {
        return new DefaultMappingRegistry(mappingRules);
    }

    public static MappingRegistry mappings(MappingRegistry... mappingRegistries) {
        return new DefaultMappingRegistry().with(mappingRegistries);
    }

    private DefaultMappingRegistry(MappingRule... mappingRules) {
        this.mappingRules = Arrays.asList(mappingRules);
    }

    @Override
    public MappingRegistry with(MappingRegistry... mappingRegistries) {
        return new DefaultMappingRegistry(Stream.concat(this.stream(),
                Arrays.stream(mappingRegistries).flatMap(MappingRegistry::stream)).toArray(MappingRule[]::new));
    }

    @Override
    public MappingRegistry with(MappingRule... mappingRules) {
        return new DefaultMappingRegistry(Stream.concat(this.stream(),
                Arrays.stream(mappingRules)).toArray(MappingRule[]::new));
    }

    @Override
    public Stream<MappingRule> stream() {
        return mappingRules.stream();
    }
}
