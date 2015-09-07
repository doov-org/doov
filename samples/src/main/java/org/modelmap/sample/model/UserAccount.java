/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.sample.model;

import org.modelmap.sample.field.SampleFieldId;
import org.modelmap.sample.field.SamplePath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class UserAccount implements Serializable {

    @SamplePath(field = SampleFieldId.LOGIN)
    private String login;
    @SamplePath(field = SampleFieldId.PASSWORD)
    private String password;

    @SamplePath(field = SampleFieldId.TIMEZONE)
    private Timezone timezone = Timezone.ETC_GMT;
    @SamplePath(field = SampleFieldId.LANGUAGE)
    private Language language = Language.EN;

    @SamplePath(field = SampleFieldId.PHONE_NUMBER)
    private String phoneNumber;

    @SamplePath(field = SampleFieldId.EMAIL)
    private String email;
    @SamplePath(field = SampleFieldId.EMAIL_ACCEPTED)
    private Boolean acceptEmail;
    @SamplePath(field = SampleFieldId.EMAILS_PREFERENCES)
    private Collection<EmailType> emailTypes = new HashSet<>();

    //FIXME add field for web site property (3 * 2 = 6 fields)
    private List<FavoriteWebsite> top3WebSite = new ArrayList<>();

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
