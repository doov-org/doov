package org.modelmap.sample2.model;

import static java.util.Arrays.asList;
import static org.modelmap.core.FieldModels.toFieldModel;
import static org.modelmap.core.FieldModels.toMapThenFieldModel;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.modelmap.core.FieldInfo;
import org.modelmap.core.FieldModel;
import org.openjdk.jmh.annotations.*;

@Threads(Threads.MAX)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 2)
@Measurement(iterations = 5, time = 2)
@Fork(1)
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
        return wrapper.stream().collect(toFieldModel(new Sample2ModelWrapper()));
    }

    @Benchmark
    public FieldModel clone_stream_parallel_and_collect() {
        return wrapper.parallelStream().collect(toMapThenFieldModel(Sample2ModelWrapper::new));
    }

    @Benchmark
    public FieldModel clone_stream_sequential_for_each() {
        FieldModel clone = new Sample2ModelWrapper();
        wrapper.stream().forEach(e -> clone.set(e.getKey(), e.getValue()));
        return clone;
    }

    @Benchmark
    public FieldModel clone_stream_parallel_for_each() {
        FieldModel clone = new Sample2ModelWrapper();
        wrapper.parallelStream().forEach(e -> clone.set(e.getKey(), e.getValue()));
        return clone;
    }
}
