/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.sample;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

public class UserAccount implements Serializable {

    private String login;
    private String password;
    private String passwordConfirmation;

    private Timezone timezone = Timezone.ETC_GMT;
    private Language language = Language.EN;

    private String phoneNumber;
    private String email;
    private Boolean acceptEmail;
    private Collection<EmailType> emailTypes = new HashSet<>();
    private int maxEmailPerWeek;

    public UserAccount() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public Timezone getTimezone() {
        return timezone;
    }

    public void setTimezone(Timezone timezone) {
        this.timezone = timezone;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Boolean isAcceptEmail() {
        return acceptEmail;
    }

    public void setAcceptEmail(Boolean acceptEmail) {
        this.acceptEmail = acceptEmail;
    }

    public Collection<EmailType> getEmailTypes() {
        return emailTypes;
    }

    public void setEmailTypes(Collection<EmailType> emailTypes) {
        this.emailTypes = emailTypes;
    }

    public int getMaxEmailPerWeek() {
        return maxEmailPerWeek;
    }

    public void setMaxEmailPerWeek(int maxEmailPerWeek) {
        this.maxEmailPerWeek = maxEmailPerWeek;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
