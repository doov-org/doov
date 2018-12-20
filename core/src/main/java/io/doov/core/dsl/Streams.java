/*
 * Copyright 2018 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.core.dsl;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Static utility methods related to {@code Stream} instances.
 *
 */
public class Streams {

    /**
     * Returns a stream consisting of the results of applying the given function to the elements of
     * {@code stream} and their indices in the stream.
     *
     * Copied from Guava 27.0.1-jre com.google.common.collect.Streams
     *
     * @param stream input stream
     * @param function mapping function with index as long
     * @param <T> input type
     * @param <R> output type
     * @return output stream
     */
    public static <T, R> Stream<R> mapWithIndex(
            Stream<T> stream, BiFunction<? super T, Long, ? extends R> function) {
        boolean isParallel = stream.isParallel();
        Spliterator<T> fromSpliterator = stream.spliterator();

        if (!fromSpliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
            Iterator<T> fromIterator = Spliterators.iterator(fromSpliterator);
            return StreamSupport.stream(
                    new Spliterators.AbstractSpliterator<R>(
                            fromSpliterator.estimateSize(),
                            fromSpliterator.characteristics() & (Spliterator.ORDERED | Spliterator.SIZED)) {
                        long index = 0;

                        @Override
                        public boolean tryAdvance(Consumer<? super R> action) {
                            if (fromIterator.hasNext()) {
                                action.accept(function.apply(fromIterator.next(), index++));
                                return true;
                            }
                            return false;
                        }
                    },
                    isParallel)
                    .onClose(stream::close);
        }
        class Splitr extends MapWithIndexSpliterator<Spliterator<T>, R, Splitr> implements Consumer<T> {

            T holder;

            Splitr(Spliterator<T> splitr, long index) {
                super(splitr, index);
            }

            @Override
            public void accept(T t) {
                this.holder = t;
            }

            @Override
            public boolean tryAdvance(Consumer<? super R> action) {
                if (fromSpliterator.tryAdvance(this)) {
                    try {
                        action.accept(function.apply(holder, index++));
                        return true;
                    } finally {
                        holder = null;
                    }
                }
                return false;
            }

            @Override
            Splitr createSplit(Spliterator<T> from, long i) {
                return new Splitr(from, i);
            }
        }
        return StreamSupport.stream(new Splitr(fromSpliterator, 0), isParallel).onClose(stream::close);
    }

    private abstract static class MapWithIndexSpliterator<
            F extends Spliterator<?>, R, S extends MapWithIndexSpliterator<F, R, S>>
            implements Spliterator<R> {

        final F fromSpliterator;
        long index;

        MapWithIndexSpliterator(F fromSpliterator, long index) {
            this.fromSpliterator = fromSpliterator;
            this.index = index;
        }

        abstract S createSplit(F from, long i);

        @Override
        public S trySplit() {
            @SuppressWarnings("unchecked")
            F split = (F) fromSpliterator.trySplit();
            if (split == null) {
                return null;
            }
            S result = createSplit(split, index);
            this.index += split.getExactSizeIfKnown();
            return result;
        }

        @Override
        public long estimateSize() {
            return fromSpliterator.estimateSize();
        }

        @Override
        public int characteristics() {
            return fromSpliterator.characteristics()
                    & (Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED);
        }
    }
}
