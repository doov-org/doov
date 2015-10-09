package org.modelmap.sample2.model;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static org.modelmap.sample2.field.Sample2Tag.EMAIL;
import static org.modelmap.sample2.field.Sample2Tag.ID;
import static org.modelmap.sample2.field.Sample2Tag.LOGIN;
import static org.modelmap.sample2.field.Sample2Tag.PASSWORD;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import org.modelmap.core.FieldInfo;

public class Sample2Models {
    public static Sample2Model sample() {
        final Sample2ModelWrapper wrapper = new Sample2ModelWrapper();
        final AtomicLong i = new AtomicLong(0);
        stream(wrapper.getFieldInfos()).map(FieldInfo::id).filter(id -> asList(id.tags()).contains(EMAIL))
                        .forEach(id -> wrapper.set(id, "account" + i.incrementAndGet() + "@some.name"));

        i.set(0);
        stream(wrapper.getFieldInfos()).map(FieldInfo::id).filter(id -> asList(id.tags()).contains(ID))
                        .forEach(id -> wrapper.set(id, i.incrementAndGet()));

        i.set(0);
        stream(wrapper.getFieldInfos()).map(FieldInfo::id).filter(id -> asList(id.tags()).contains(PASSWORD))
                        .forEach(id -> wrapper.set(id, "password_" + i.incrementAndGet()));

        i.set(0);
        stream(wrapper.getFieldInfos()).map(FieldInfo::id).filter(id -> asList(id.tags()).contains(LOGIN))
                        .forEach(id -> wrapper.set(id, "login_" + i.incrementAndGet()));

        return wrapper.getModel();
    }
}
