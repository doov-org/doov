package org.modelmap.sample.model;

import static java.util.Arrays.asList;
import static org.modelmap.sample.model.FavoriteWebsite.webSite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.modelmap.core.*;
import org.openjdk.jmh.annotations.*;

@Threads(Threads.MAX)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 2)
@Measurement(iterations = 5, time = 2)
@Fork(1)
public class CloneBenchmark {

    private SampleModel source;
    private SampleModelWrapper wrapper;

    @Setup
    public void init() {
        source = SampleModels.sample();
        wrapper = new SampleModelWrapper(source);
    }

    @Benchmark
    public SampleModel clone_java_bean() {
        SampleModel clone = new SampleModel();

        User user = new User();
        user.setId(source.getUser().getId());
        user.setFirstName(source.getUser().getFirstName());
        user.setLastName(source.getUser().getLastName());
        user.setBirthDate(source.getUser().getBirthDate());
        clone.setUser(user);

        Account account = new Account();
        account.setId(source.getAccount().getId());
        account.setAcceptEmail(source.getAccount().getAcceptEmail());
        account.setEmail(source.getAccount().getEmail());
        account.setEmailTypes(source.getAccount().getEmailTypes());
        account.setLanguage(source.getAccount().getLanguage());
        account.setLogin(source.getAccount().getLogin());
        account.setPassword(source.getAccount().getPassword());
        account.setPhoneNumber(source.getAccount().getPhoneNumber());
        account.setTimezone(source.getAccount().getTimezone());

        account.setTop3WebSite(new ArrayList<>());
        source.getAccount().getTop3WebSite()
                        .forEach(site -> account.getTop3WebSite().add(webSite(site.getName(), site.getUrl())));
        clone.setAccount(account);
        return clone;
    }

    @Benchmark
    public FieldModel clone_by_field_info_stream_sequential() {
        FieldModel clone = new SampleModelWrapper();
        Arrays.stream(wrapper.getFieldInfos()).forEach(e -> clone.set(e.id(), wrapper.get(e.id())));
        return clone;
    }

    @Benchmark
    public FieldModel clone_by_field_info_for() {
        FieldModel clone = new SampleModelWrapper();
        for (FieldInfo fieldInfo : wrapper.getFieldInfos())
            clone.set(fieldInfo.id(), wrapper.get(fieldInfo.id()));
        return clone;
    }

    @Benchmark
    public FieldModel clone_by_field_info_for_each() {
        FieldModel clone = new SampleModelWrapper();
        asList(wrapper.getFieldInfos()).forEach(fieldInfo -> clone.set(fieldInfo.id(), wrapper.get(fieldInfo.id())));
        return clone;
    }

    @Benchmark
    public FieldModel clone_stream_sequential() {
        return wrapper.stream().collect(FieldModels.toFieldModel(new SampleModelWrapper()));
    }

    @Benchmark
    public FieldModel clone_stream_parallel() {
        return wrapper.parallelStream().collect(FieldModels.toMapThenFieldModel(SampleModelWrapper::new));
    }
}
