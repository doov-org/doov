package io.doov.sample.model;

import java.time.LocalDate;

import io.doov.sample.field.SampleFieldId;
import io.doov.sample.field.SamplePath;

public class User extends Identity {

    @SamplePath(field = SampleFieldId.FIRST_NAME, readable = "user first name")
    private String firstName;

    @SamplePath(field = SampleFieldId.LAST_NAME, readable = "user last name")
    private String lastName;

    @SamplePath(field = SampleFieldId.BIRTHDATE, readable = "user birthdate")
    private LocalDate birthDate;

    @SamplePath(field = SampleFieldId.FULLNAME, readable = "user full name")
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
}
