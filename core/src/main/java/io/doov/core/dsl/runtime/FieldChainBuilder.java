package io.doov.core.dsl.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

import io.doov.core.FieldId;

/**
 * Builder for {@link RuntimeField}
 * @param <B> model entry type
 * @param <T> type of the current chain link
 * @param <R> return type of the last chain link
 */
public class FieldChainBuilder<B, T, R> {

    private final Class<B> rootType;
    private final List<PathMethod<Object, Object>> chain;
    private final FieldId id;
    private String readable;
    private FieldId[] siblings;
    private boolean isTransient;

    private FieldChainBuilder(Class<B> rootType,
                              List<PathMethod<Object, Object>> chain,
                              FieldId id,
                              String readable,
                              FieldId[] siblings,
                              boolean isTransient) {
        this.rootType = rootType;
        this.chain = chain;
        this.id = id;
        this.readable = readable;
        this.siblings = siblings == null ? new FieldId[0] : siblings;
        this.isTransient = isTransient;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> generify(Class<?> cls) {
        return (Class<T>) cls;
    }

    public static <B> FieldChainBuilder<B, B, B> from(Class<B> root, FieldId id) {
        return new FieldChainBuilder<>(root, new ArrayList<>(), id, null, null, false);
    }

    public FieldChainBuilder<B, T, R> readable(String readable) {
        this.readable = readable;
        return this;
    }

    public FieldChainBuilder<B, T, R> siblings(FieldId... siblings) {
        this.siblings = siblings;
        return this;
    }

    public FieldChainBuilder<B, T, R> _transient(boolean isTransient) {
        this.isTransient = isTransient;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <O> FieldChainBuilder<B, R, O> get(Function<R, O> readMethod,
                                              BiConsumer<R, O> writeMethod,
                                              Supplier<O> supplier) {
        PathMethod<R, O> method = new SimplePathMethod<>(supplier, readMethod, writeMethod);
        chain.add((PathMethod) method);
        return new FieldChainBuilder<>(this.rootType, this.chain, id, readable, siblings, isTransient);
    }

    @SuppressWarnings("unchecked")
    public <O> FieldChainBuilder<B, R, O> list(Function<R, List<O>> readMethod,
                                               BiConsumer<R, List<O>> writeMethod,
                                               Supplier<O> supplier) {
        PathMethod<R, O> method = new ListPathMethod<>(supplier, readMethod, writeMethod, id.position());
        chain.add((PathMethod) method);
        return new FieldChainBuilder<>(this.rootType, this.chain, id, readable, siblings, isTransient);
    }

    /**
     * Terminal method that builds the RuntimeField
     *
     * @param readMethod get method
     * @param writeMethod set method
     * @param type field type
     * @param genericTypes generic types
     * @param <O> field type
     * @return the runtime field
     */
    @SuppressWarnings("unchecked")
    public <O> RuntimeField<B, O> field(Function<R, O> readMethod,
                                        BiConsumer<R, O> writeMethod,
                                        Class<O> type, Class<?>... genericTypes) {
        PathMethod<R, O> method = new SimplePathMethod<>(null, readMethod, writeMethod);
        PathMethod<Object, O> m = (PathMethod<Object, O>) method;
        return new RuntimeField<>(this.chain, m, id, readable, siblings, type, genericTypes, isTransient);
    }

}
