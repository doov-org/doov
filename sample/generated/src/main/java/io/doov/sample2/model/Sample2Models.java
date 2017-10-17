package io.doov.sample2.model;

import static io.doov.sample2.field.Sample2Tag.EMAIL;
import static io.doov.sample2.field.Sample2Tag.ID;
import static io.doov.sample2.field.Sample2Tag.LOGIN;
import static io.doov.sample2.field.Sample2Tag.PASSWORD;

import java.util.concurrent.atomic.AtomicLong;

import io.doov.core.FieldInfo;
import io.doov.core.FieldModel;
import io.doov.sample2.model.Sample2Model;

public class Sample2Models {

    public static Sample2Model sample() {
        final Sample2ModelWrapper wrapper = new Sample2ModelWrapper();
        final AtomicLong i = new AtomicLong(0);
        wrapper.getFieldInfos().stream().map(FieldInfo::id)
                        .filter(id -> id.hasTag(EMAIL))
                        .forEach(id -> wrapper.set(id, "account" + i.incrementAndGet() + "@some.name"));

        i.set(0);
        wrapper.getFieldInfos().stream().map(FieldInfo::id)
                        .filter(id -> id.hasTag(ID))
                        .forEach(id -> wrapper.set(id, i.incrementAndGet()));

        i.set(0);
        wrapper.getFieldInfos().stream().map(FieldInfo::id)
                        .filter(id -> id.hasTag(PASSWORD))
                        .forEach(id -> wrapper.set(id, "password_" + i.incrementAndGet()));

        i.set(0);
        wrapper.getFieldInfos().stream().map(FieldInfo::id)
                        .filter(id -> id.hasTag(LOGIN))
                        .forEach(id -> wrapper.set(id, "login_" + i.incrementAndGet()));

        return wrapper.getModel();
    }

    public static FieldModel wrapper() {
        return new Sample2ModelWrapper(sample());
    }
}
