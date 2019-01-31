package io.doov.core.dsl.mapping.builder;

import java.util.Optional;
import java.util.function.Function;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.*;
import io.doov.core.dsl.mapping.converter.DefaultTypeConverter;

/**
 * First step for creating mapping rule.
 * Associates the in field with type {@link I} with a converter or the out field with type {@link I}
 *
 * @param <I> in type
 */
public class SimpleStepMap<I> {

    private final MappingInput<I> input;

    public SimpleStepMap(MappingInput<I> input) {
        this.input = input;
    }

    public SimpleStepMap(DslField<I> inFieldInfo) {
        this(new FieldInput<>(inFieldInfo));
    }

    /**
     * Return the step mapping
     *
     * @param typeConverter type converter
     * @param <O>           out type
     * @return the step mapping
     */
    public <O> SimpleStepMap<O> using(TypeConverter<I, O> typeConverter) {
        return new SimpleStepMap<>(new ConverterInput<>(input, typeConverter));
    }

    /**
     * Return the step mapping
     *
     * @param conversionOp conversion operation
     * @param <O>           out type
     * @return the step mapping
     */
    public <O> SimpleStepMap<O> using(Function<I, O> conversionOp) {

        String repr = conversionOp.getClass().getSimpleName();

        TypeConverter<I,O> typeConverter = TypeConverters.converter(conversionOp,repr);
        return new SimpleStepMap<>(new ConverterInput<>(input, typeConverter));
    }

    /**
     * Return the mapping rule
     *
     * @param output consumer output
     * @return the mapping rule
     */
    public DefaultMappingRule<I> to(MappingOutput<I> output) {
        return new DefaultMappingRule<>(input, output);
    }

    /**
     * Return the mapping rule
     *
     * @param outFieldInfo out field info
     * @return the mapping rule
     */
    public DefaultMappingRule<I> to(DslField<I> outFieldInfo) {
        return this.to(new FieldOutput<>(outFieldInfo));
    }

    /**
     * Return the mapping rule
     *
     * @param consumer out field info
     * @return the mapping rule
     */
    public DefaultMappingRule<I> to(TriConsumer<DslModel, Context, I> consumer) {
        return this.to(new ConsumerOutput<>(consumer));
    }
}
