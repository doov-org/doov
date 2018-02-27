package io.doov.core.dsl.lang;

import java.util.stream.Stream;

public interface MappingRegistry {

    void register(MappingRule rule);

    void registerAll(MappingRegistry registry);

    Stream<MappingRule> stream();
}
