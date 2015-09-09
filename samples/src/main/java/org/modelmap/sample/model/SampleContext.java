/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.sample.model;

public class SampleContext {

    private User user = new User();
    private Account account = new Account();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
