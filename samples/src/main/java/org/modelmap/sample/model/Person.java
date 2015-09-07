/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.sample.model;

import org.modelmap.sample.field.SampleFieldId;
import org.modelmap.sample.field.SamplePath;

import java.io.Serializable;
import java.util.Date;

public class Person implements Serializable {

    @SamplePath(field = SampleFieldId.FIRST_NAME)
    private String firstName;
    @SamplePath(field = SampleFieldId.LAST_NAME)
    private String lastName;
    @SamplePath(field = SampleFieldId.BIRTHDATE)
    private Date birthDate;

    public Person() {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
