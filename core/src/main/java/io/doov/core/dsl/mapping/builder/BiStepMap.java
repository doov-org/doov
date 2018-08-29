package io.doov.core.dsl.mapping.builder;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.*;

/**
 * First step for creating mapping rule.
 * Associates field with type {@link I} and {@link J} with a bi-converter.
 *
 * @param <I> 1st in type
 * @param <J> 2nd in type
 */
public class BiStepMap<I, J> {

    private final DslField<I> inFieldInfo;
    private final DslField<J> in2FieldInfo;

    public BiStepMap(DslField<I> inFieldInfo, DslField<J> in2FieldInfo) {
        this.inFieldInfo = inFieldInfo;
        this.in2FieldInfo = in2FieldInfo;
    }

    /**
     * Returns bi step mapping
     *
     * @param typeConverter bi type converter
     * @param <O>           out type
     * @return bi step mapping
     */
    public <O> BiStepMapping<I, J, O> using(BiTypeConverter<I, J, O> typeConverter) {
        return new BiStepMapping<>(inFieldInfo, in2FieldInfo, typeConverter);
    }

    /**
     * Intermediary step for creating a bi mapping rule.
     * Associates in fields with type {@link I} and {@link J} with the out field with type {@link O}
     *
     * @param <I> 1st in type
     * @param <J> 2nd in type
     * @param <O> out type
     */
    public class BiStepMapping<I, J, O> {

        private final MappingInput<O> input;

        BiStepMapping(DslField<I> inFieldInfo, DslField<J> in2FieldInfo, BiTypeConverter<I, J, O> typeConverter) {
            this.input = new BiConverterInput<>(
                    new FieldInput<>(inFieldInfo),
                    new FieldInput<>(in2FieldInfo),
                    typeConverter);
        }

        /**
         * Return the bi mapping rule
         *
         * @param outFieldInfo out field
         * @return bi mapping rule
         */
        public DefaultMappingRule<O> to(DslField<O> outFieldInfo) {
            return new DefaultMappingRule<>(input, new FieldOutput<>(outFieldInfo));
        }

        /**
         * Return the bi mapping rule
         *
         * @param output consumer output
         * @return bi mapping rule
         */
        public DefaultMappingRule<O> to(ConsumerOutput<O> output) {
            return new DefaultMappingRule<>(input, output);
        }

        /**
         * Return the bi mapping rule
         *
         * @param consumer consumer
         * @return bi mapping rule
         */
        public DefaultMappingRule<O> to(TriConsumer<DslModel, Context, O> consumer) {
            return new DefaultMappingRule<>(input, new ConsumerOutput<>(consumer));
        }

    }


}
