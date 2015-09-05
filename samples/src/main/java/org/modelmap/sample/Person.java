/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.sample;

import java.io.Serializable;
import java.util.Date;

public class Person implements Serializable {

    private String firstName;
    private String lastName;
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
