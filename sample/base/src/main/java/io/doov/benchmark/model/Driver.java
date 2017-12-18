/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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