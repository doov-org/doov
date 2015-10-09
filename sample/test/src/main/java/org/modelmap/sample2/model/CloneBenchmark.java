package org.modelmap.sample2.model;

import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import org.modelmap.core.FieldInfo;
import org.modelmap.core.FieldModel;
import org.modelmap.core.FieldModels;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@Threads(Threads.MAX)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 2)
@Measurement(iterations = 3, time = 2)
@Fork(2)
public class CloneBenchmark {

    private Sample2Model source;
    private Sample2ModelWrapper wrapper;

    @Setup
    public void init() {
        source = Sample2Models.sample();
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
    public FieldModel clone_stream_sequential() {
        return StreamSupport.stream(wrapper.spliterator(), false).collect(
                        FieldModels.toFieldModel(new Sample2ModelWrapper()));
    }

    @Benchmark
    public FieldModel clone_stream_parallel() {
        return StreamSupport.stream(wrapper.spliterator(), false).parallel()
                        .collect(FieldModels.toMapThenFieldModel(Sample2ModelWrapper::new));
    }
}
