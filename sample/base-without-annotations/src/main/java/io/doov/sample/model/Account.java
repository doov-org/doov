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
import java.util.*;

public class Account extends Identity {

    private String login;

    private String password;

    private Timezone timezone;

    private Language language;

    private Country country;

    private String phoneNumber;

    private String email;

    private boolean acceptEmail;

    private LocalDate creationDate;

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

    public boolean isAcceptEmail() {
        return acceptEmail;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
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
