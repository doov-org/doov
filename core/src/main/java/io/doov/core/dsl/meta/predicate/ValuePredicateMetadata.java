package io.doov.core.dsl.meta.predicate;

import static io.doov.core.dsl.meta.DefaultOperator.always_false;
import static io.doov.core.dsl.meta.DefaultOperator.always_true;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;

import java.util.concurrent.atomic.AtomicInteger;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.MetadataType;

public class ValuePredicateMetadata<M extends ValuePredicateMetadata<M>> extends LeafMetadata<M> implements PredicateMetadata {

    private final AtomicInteger evalTrue = new AtomicInteger();
    private final AtomicInteger evalFalse = new AtomicInteger();

    public ValuePredicateMetadata(MetadataType type) {
        super(type);
    }

    @Override
    public AtomicInteger evalTrue() {
        return evalTrue;
    }

    @Override
    public AtomicInteger evalFalse() {
        return evalFalse;
    }

    // field

    public static <M extends ValuePredicateMetadata<M>> M fieldMetadata(DslField<?> field) {
        return new ValuePredicateMetadata<M>(FIELD_PREDICATE).field(field);
    }

    // boolean

    public static <M extends ValuePredicateMetadata<M>> M trueMetadata() {
        return new ValuePredicateMetadata<M>(LEAF_PREDICATE).operator(always_true);
    }

    public static <M extends ValuePredicateMetadata<M>> M falseMetadata() {
        return new ValuePredicateMetadata<M>(LEAF_PREDICATE).operator(always_false);
    }

}
