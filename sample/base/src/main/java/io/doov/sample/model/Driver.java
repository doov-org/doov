package io.doov.sample.model;

import io.doov.sample.field.SampleFieldId;
import io.doov.sample.field.SamplePath;

public class Driver {

    @SamplePath(field = SampleFieldId.NAME)
    private String name;

    @SamplePath(field = SampleFieldId.AGE)
    private int age;

    @SamplePath(field = SampleFieldId.DRIVING_LICENSE)
    private boolean hasDrivingLicense;

    public Driver() {
    }

    public Driver(String name, int age, boolean hasDrivingLicense) {
        this.name = name;
        this.age = age;
        this.hasDrivingLicense = hasDrivingLicense;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isHasDrivingLicense() {
        return hasDrivingLicense;
    }

    public void setHasDrivingLicense(boolean hasDrivingLicense) {
        this.hasDrivingLicense = hasDrivingLicense;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Driver");
        sb.append("{name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append(", hasDrivingLicense=").append(hasDrivingLicense);
        sb.append('}');
        return sb.toString();
    }

}