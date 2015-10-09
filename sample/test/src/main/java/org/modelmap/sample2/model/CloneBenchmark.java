package org.modelmap.sample2.model;

import static java.util.Arrays.asList;
import static org.modelmap.core.FieldModels.toFieldModel;
import static org.modelmap.core.FieldModels.toMapThenFieldModel;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import org.modelmap.core.FieldInfo;
import org.modelmap.core.FieldModel;
import org.modelmap.sample2.model.Sample2ModelWrapper.Sample2ModelProperty;
import org.openjdk.jmh.annotations.*;

@Threads(Threads.MAX)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 2)
@Measurement(iterations = 3, time = 2)
@Fork(2)
public class CloneBenchmark {

    private Sample2ModelWrapper wrapper;


    @Setup
    public void init() {
        Sample2Model source = Sample2Models.sample();
        wrapper = new Sample2ModelWrapper(source);
    }

    @Benchmark
    public FieldModel clone_by_field_info_stream_sequential() {
        FieldModel clone = new Sample2ModelWrapper();
        Arrays.stream(wrapper.getFieldInfos()).forEach(e -> clone.set(e.id(), wrapper.get(e.id())));
        return clone;
    }

    @Benchmark
    public FieldModel clone_by_field_info_for() {
        FieldModel clone = new Sample2ModelWrapper();
        for (FieldInfo fieldInfo : wrapper.getFieldInfos())
            clone.set(fieldInfo.id(), wrapper.get(fieldInfo.id()));
        return clone;
    }

    @Benchmark
    public FieldModel clone_by_field_info_for_each() {
        FieldModel clone = new Sample2ModelWrapper();
        asList(wrapper.getFieldInfos()).forEach(fieldInfo -> clone.set(fieldInfo.id(), wrapper.get(fieldInfo.id())));
        return clone;
    }

    @Benchmark
    public FieldModel clone_stream_sequential_and_collect() {
        return StreamSupport.stream(wrapper.spliterator(), false).collect(toFieldModel(new Sample2ModelWrapper()));
    }

    @Benchmark
    public FieldModel clone_stream_parallel_and_collect() {
        return StreamSupport.stream(wrapper.spliterator(), false).parallel()
                        .collect(toMapThenFieldModel(Sample2ModelWrapper::new));
    }

    @Benchmark
    public FieldModel clone_stream_sequential_for_each() {
        FieldModel clone = new Sample2ModelWrapper();
        StreamSupport.stream(wrapper.spliterator(), false).forEach(e -> {
            final Object value = e.getValue();
            if (value != null) {
                clone.set(e.getKey(), value);
            }
        });
        return clone;
    }

    @Benchmark
    public FieldModel clone_stream_parallel_for_each() {
        FieldModel clone = new Sample2ModelWrapper();
        StreamSupport.stream(wrapper.spliterator(), true).forEach(e -> {
            final Object value = e.getValue();
            if (value != null) {
                clone.set(e.getKey(), value);
            }
        });
        return clone;
    }

    @Benchmark
    public void clone_stream_sequential_property() {
        Sample2ModelWrapper clone = new Sample2ModelWrapper();

        Arrays.stream(Sample2ModelProperty.values()).forEach(p -> {
            p.consumer().accept(clone.getModel(), p.supplier().apply(wrapper.getModel()));
        });

    }

}
