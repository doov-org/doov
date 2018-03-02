package io.doov.core.dsl.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ListPathMethod<T, R> implements PathMethod<T, R> {

    private final Supplier<R> supplier;
    private final Function<T, List<R>> readMethod;
    private final BiConsumer<T, List<R>> writeMethod;
    private final int position;

    public ListPathMethod(Supplier<R> supplier, Function<T, List<R>> readMethod,
                          BiConsumer<T, List<R>> writeMethod, int position) {
        this.supplier = supplier;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.position = position;
    }

    @Override
    public R get(T link) {
        return Optional.ofNullable(readMethod.apply(link))
                        .filter(l -> l.size() >= position)
                        .map(l -> l.get(position - 1))
                        .orElse(null);
    }

    @Override
    public void set(T link, R value) {
        readMethod.apply(link).add(position - 1, value);
    }

    @Override
    public R create(T link) {
        List<R> list = readMethod.apply(link);
        if (list == null) {
            list = new ArrayList<>();
            writeMethod.accept(link, list);
        }
        for (int i = 0; i <= position - 1; i++) {
            if (list.size() <= i) {
                list.add(i, null);
            }
        }
        R r = supplier.get();
        list.set(position - 1, r);
        return r;
    }

}
