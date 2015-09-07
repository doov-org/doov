/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.sample.model;

public class SampleContext {

    private Person person = new Person();
    private UserAccount userAccount = new UserAccount();

    public Person getPerson() {
        return person;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }
}
