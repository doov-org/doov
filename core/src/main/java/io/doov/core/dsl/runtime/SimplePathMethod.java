package io.doov.core.dsl.runtime;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimplePathMethod<T, R> implements PathMethod<T, R> {

    private final Supplier<R> supplier;
    private final Function<T, R> readMethod;
    private final BiConsumer<T, R> writeMethod;

    public SimplePathMethod(Supplier<R> supplier, Function<T, R> readMethod, BiConsumer<T, R> writeMethod) {
        this.supplier = supplier;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
    }

    @Override
    public R get(T link) {
        return readMethod.apply(link);
    }

    @Override
    public void set(T link, R value) {
        writeMethod.accept(link, value);
    }

    @Override
    public R create(T link) {
        R r = supplier.get();
        writeMethod.accept(link, r);
        return r;
    }

}
