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
package io.doov.sample.model;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import io.doov.sample.field.*;

public class User extends Identity {

    @SamplePath(field = SampleFieldId.FIRST_NAME, readable = "user.first.name")
    private String firstName;

    @SamplePath(field = SampleFieldId.LAST_NAME, readable = "user.last.name")
    private String lastName;

    @Past
    @NotNull
    @SamplePath(field = SampleFieldId.BIRTHDATE, readable = "user.birthdate")
    private LocalDate birthDate;

    @CorePath(field = CoreFieldId.ADDRESS, readable = "user.address")
    private String address;

    @CorePath(field = CoreFieldId.TEL, readable = "user.tel")
    private String tel;

    @SamplePath(field = SampleFieldId.FULLNAME, readable = "user.full.name")
    public String getFullName() {
        return firstName != null && lastName != null ? firstName + " " + lastName : null;
    }

    public void setFullName(String fullname) {
        // not supported
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
