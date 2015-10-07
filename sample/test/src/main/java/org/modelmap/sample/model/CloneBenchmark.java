package org.modelmap.sample.model;

import static java.util.Arrays.stream;
import static org.modelmap.sample.model.FavoriteWebsite.webSite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.modelmap.core.FieldInfo;
import org.modelmap.core.FieldModel;
import org.modelmap.core.FieldModels;
import org.modelmap.sample.field.SampleFieldIdInfo;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2)
@Measurement(iterations = 3)
@Fork(2)
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
    public FieldModel clone_field_model() {
        FieldModel wrapper = new SampleModelWrapper(source);
        FieldModel clone = new SampleModelWrapper();
        Arrays.stream(wrapper.getFieldInfos()).forEach(e -> clone.set(e.id(), wrapper.get(e.id())));
        return clone;
    }

    @Benchmark
    public FieldModel clone_by_fieldid() {
        FieldModel wrapper = new SampleModelWrapper(source);
        FieldModel clone = new SampleModelWrapper();
        for (FieldInfo fieldInfo : wrapper.getFieldInfos())
            clone.set(fieldInfo.id(), wrapper.get(fieldInfo.id()));
        return clone;
    }

    @Benchmark
    public FieldModel clone_stream_sequential() {
        FieldModel wrapper = new SampleModelWrapper(source);
        return FieldModels.stream(wrapper, false).filter(e -> e.getValue() != null)
                        .collect(FieldModels.toFieldModel(SampleModelWrapper::new));
    }

    @Benchmark
    public FieldModel clone_stream_parallel() {
        FieldModel wrapper = new SampleModelWrapper(source);
        return FieldModels.stream(wrapper, true).parallel().filter(e -> e.getValue() != null)
                        .collect(FieldModels.toFieldModel(SampleModelWrapper::new));
    }
}
