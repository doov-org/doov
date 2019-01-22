package io.doov.core.dsl.meta.predicate;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.MetadataType.*;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;
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

    // values

    public static <M extends ValuePredicateMetadata<M>> M valueMetadata(Supplier<?> value) {
        return new ValuePredicateMetadata<M>(LEAF_VALUE).valueSupplier(value);
    }

    public static <M extends ValuePredicateMetadata<M>> M valueMetadata(Object value) {
        return new ValuePredicateMetadata<M>(LEAF_VALUE).valueObject(value);
    }

    public static <M extends ValuePredicateMetadata<M>> M readableMetadata(Readable readable) {
        return new ValuePredicateMetadata<M>(LEAF_VALUE).valueReadable(readable);
    }

    public static <M extends ValuePredicateMetadata<M>> M stringMetadata(String value) {
        return new ValuePredicateMetadata<M>(LEAF_VALUE).valueString(value);
    }

    public static <M extends ValuePredicateMetadata<M>> M unknownMetadata(String value) {
        return new ValuePredicateMetadata<M>(LEAF_VALUE).valueUnknown(value);
    }

    // list values

    public static <M extends ValuePredicateMetadata<M>> M valueListMetadata(Collection<?> values) {
        return new ValuePredicateMetadata<M>(LEAF_VALUE).valueListObject(values);
    }

    // any match

    public static <M extends ValuePredicateMetadata<M>> M anyMatchMetadata(Collection<?> values) {
        return new ValuePredicateMetadata<M>(FIELD_PREDICATE_MATCH_ANY).valueListObject(values);
    }

    public static <M extends ValuePredicateMetadata<M>> M anyMatchMetadata(Metadata metadata) {
        return new ValuePredicateMetadata<M>(FIELD_PREDICATE_MATCH_ANY).valueReadable(lambda);
    }

}
