package io.doov.benchmark.model;

import static io.doov.benchmark.model.BenchmarkFieldId.AGE;
import static io.doov.benchmark.model.BenchmarkFieldId.DRIVING_LICENSE;
import static io.doov.benchmark.model.BenchmarkFieldId.NAME;

import java.util.ArrayList;
import java.util.List;

public class Driver {

    @BenchmarkPath(field = NAME)
    private String name;

    @BenchmarkPath(field = AGE)
    private int age;

    @BenchmarkPath(field = DRIVING_LICENSE)
    private boolean hasDrivingLicense;

    private List<Friend> friends = new ArrayList<>();

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

    public Driver addFriend(Friend driver) {
        this.friends.add(driver);
        return this;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
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