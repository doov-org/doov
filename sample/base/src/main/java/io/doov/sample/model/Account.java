package io.doov.sample.model;

import java.util.*;

import io.doov.sample.field.SampleFieldId;
import io.doov.sample.field.SamplePath;

public class Account extends Identity {

    @SamplePath(field = SampleFieldId.LOGIN, readable = "account login")
    private String login;

    @SamplePath(field = SampleFieldId.PASSWD, readable = "account password")
    private String password;

    @SamplePath(field = SampleFieldId.TIMEZONE, readable = "account timezone")
    private Timezone timezone;

    @SamplePath(field = SampleFieldId.LANGUAGE, readable = "account language")
    private Language language;

    @SamplePath(field = SampleFieldId.COUNTRY, readable = "account country")
    private Country country;

    @SamplePath(field = SampleFieldId.PHONE_NUMBER, readable = "account phone number")
    private String phoneNumber;

    @SamplePath(field = SampleFieldId.EMAIL, readable = "account email")
    private String email;

    @SamplePath(field = SampleFieldId.EMAIL_ACCEPTED, readable = "account email accepted")
    private boolean acceptEmail;

    @SamplePath(field = SampleFieldId.EMAILS_PREFERENCES, readable = "account préférences mail")
    private Collection<EmailType> emailTypes = new HashSet<>();

    private List<FavoriteWebsite> top3WebSite = new ArrayList<>();

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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public boolean getAcceptEmail() {
        return acceptEmail;
    }

    public void setAcceptEmail(boolean acceptEmail) {
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

    public List<FavoriteWebsite> getTop3WebSite() {
        return top3WebSite;
    }

    public void setTop3WebSite(List<FavoriteWebsite> top3WebSite) {
        this.top3WebSite = top3WebSite;
    }
}
