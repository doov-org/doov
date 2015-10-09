package org.modelmap.sample2.model;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.modelmap.sample2.field.Sample2Tag.EMAIL;
import static org.modelmap.sample2.field.Sample2Tag.ID;
import static org.modelmap.sample2.field.Sample2Tag.LOGIN;
import static org.modelmap.sample2.field.Sample2Tag.PASSWORD;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class Sample2Models {
    public static Sample2Model sample() {
        final Sample2ModelWrapper wrapper = new Sample2ModelWrapper();
        final AtomicLong i = new AtomicLong(0);
        Arrays.stream(wrapper.getFieldInfos()).map(info -> info.id()).filter(id -> asList(id.tags()).contains(EMAIL))
                        .collect(toList())
                        .forEach(id -> wrapper.set(id, "account" + i.incrementAndGet() + "@some.name"));
        i.set(0);
        Arrays.stream(wrapper.getFieldInfos()).map(info -> info.id()).filter(id -> asList(id.tags()).contains(ID))
                        .collect(toList()).forEach(id -> wrapper.set(id, i.incrementAndGet()));
        i.set(0);
        Arrays.stream(wrapper.getFieldInfos()).map(info -> info.id())
                        .filter(id -> asList(id.tags()).contains(PASSWORD)).collect(toList())
                        .forEach(id -> wrapper.set(id, "password_" + i.incrementAndGet()));
        i.set(0);
        Arrays.stream(wrapper.getFieldInfos()).map(info -> info.id()).filter(id -> asList(id.tags()).contains(LOGIN))
                        .collect(toList()).forEach(id -> wrapper.set(id, "login_" + i.incrementAndGet()));
        return wrapper.getModel();
    }
}
