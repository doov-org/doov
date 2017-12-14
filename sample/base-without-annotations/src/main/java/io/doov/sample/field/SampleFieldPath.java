/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.field;

import static io.doov.core.dsl.path.PathBuilder.from;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import io.doov.core.dsl.path.*;
import io.doov.sample.model.*;

public class SampleFieldPath implements FieldPathProvider {



    private static final List<FieldPath> ALL = new ArrayList<>();

    private static final FieldPath USER_ID = from(SampleModel.class)
                    .get(SampleModel::getUser)
                    .field(User::getId, User::setId)
                    .fieldId(SampleFieldId.USER_ID)
                    .readable("user id")
                    .build(ALL);

    private static final FieldPath ACCOUNT_ID = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .field(Account::getId, Account::setId)
                    .fieldId(SampleFieldId.ACCOUNT_ID)
                    .readable("account id")
                    .build(ALL);

    private static final FieldPath FIRST_NAME = from(SampleModel.class)
                    .get(SampleModel::getUser)
                    .field(User::getFirstName, User::setFirstName)
                    .readable("user first name")
                    .fieldId(SampleFieldId.FIRST_NAME)
                    .build(ALL);

    private static final FieldPath LAST_NAME = from(SampleModel.class)
                    .get(SampleModel::getUser)
                    .field(User::getLastName, User::setLastName)
                    .readable("user last name")
                    .fieldId(SampleFieldId.LAST_NAME)
                    .build(ALL);

    private static final FieldPath FULLNAME = from(SampleModel.class)
                    .get(SampleModel::getUser)
                    .field(User::getFullName, User::setFullName)
                    .readable("user full name")
                    .fieldId(SampleFieldId.FULLNAME)
                    .build(ALL);

    private static final FieldPath BIRTHDATE = from(SampleModel.class)
                    .get(SampleModel::getUser)
                    .field(User::getBirthDate, User::setBirthDate)
                    .readable("user birth date")
                    .fieldId(SampleFieldId.BIRTHDATE)
                    .build(ALL);

    private static final FieldPath EMAIL = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .field(Account::getEmail, Account::setEmail)
                    .readable("account email")
                    .fieldId(SampleFieldId.EMAIL)
                    .build(ALL);

    private static final FieldPath LANGUAGE = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .field(Account::getLanguage, Account::setLanguage)
                    .readable("account language")
                    .fieldId(SampleFieldId.LANGUAGE)
                    .build(ALL);

    private static final FieldPath TIMEZONE = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .field(Account::getTimezone, Account::setTimezone)
                    .readable("account timezone")
                    .fieldId(SampleFieldId.TIMEZONE)
                    .build(ALL);

    private static final FieldPath COUNTRY = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .field(Account::getCountry, Account::setCountry)
                    .readable("account country")
                    .fieldId(SampleFieldId.COUNTRY)
                    .build(ALL);

    private static final FieldPath PHONE_NUMBER = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .field(Account::getPhoneNumber, Account::setPhoneNumber)
                    .readable("account phone number")
                    .fieldId(SampleFieldId.PHONE_NUMBER)
                    .build(ALL);

    private static final FieldPath EMAIL_ACCEPTED = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .field(Account::getAcceptEmail, Account::setAcceptEmail)
                    .readable("account email accepted")
                    .fieldId(SampleFieldId.EMAIL_ACCEPTED)
                    .build(ALL);

    private static final FieldPath CREATION_DATE = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .field(Account::getCreationDate, Account::setCreationDate)
                    .readable("account creation date")
                    .fieldId(SampleFieldId.CREATION_DATE)
                    .build(ALL);

    private static final FieldPath EMAILS_PREFERENCES = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .field(Account::getEmailTypes, Account::setEmailTypes)
                    .readable("account email preferences")
                    .fieldId(SampleFieldId.EMAILS_PREFERENCES)
                    .build(ALL);

    private static final FieldPath FAVORITE_SITE_NAME_1 = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .iterable(Account::getTop3WebSite)
                    .field(FavoriteWebsite::getName, FavoriteWebsite::setName)
                    .readable("account favorite web site name 1")
                    .fieldId(SampleFieldId.FAVORITE_SITE_NAME_1)
                    .build(ALL);

    private static final FieldPath FAVORITE_SITE_NAME_2 = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .iterable(Account::getTop3WebSite)
                    .field(FavoriteWebsite::getName, FavoriteWebsite::setName)
                    .readable("account favorite web site name 2")
                    .fieldId(SampleFieldId.FAVORITE_SITE_NAME_2)
                    .build(ALL);

    private static final FieldPath FAVORITE_SITE_NAME_3 = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .iterable(Account::getTop3WebSite)
                    .field(FavoriteWebsite::getName, FavoriteWebsite::setName)
                    .readable("account favorite web site name 3")
                    .fieldId(SampleFieldId.FAVORITE_SITE_NAME_3)
                    .build(ALL);

    private static final FieldPath FAVORITE_SITE_URL_1 = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .iterable(Account::getTop3WebSite)
                    .field(FavoriteWebsite::getUrl, FavoriteWebsite::setUrl)
                    .readable("account favorite web site url 1")
                    .fieldId(SampleFieldId.FAVORITE_SITE_URL_1)
                    .build(ALL);

    private static final FieldPath FAVORITE_SITE_URL_2 = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .iterable(Account::getTop3WebSite)
                    .field(FavoriteWebsite::getUrl, FavoriteWebsite::setUrl)
                    .readable("account favorite web site url 2")
                    .fieldId(SampleFieldId.FAVORITE_SITE_URL_2)
                    .build(ALL);

    private static final FieldPath FAVORITE_SITE_URL_3 = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .iterable(Account::getTop3WebSite)
                    .field(FavoriteWebsite::getUrl, FavoriteWebsite::setUrl)
                    .readable("account favorite web site url 3")
                    .fieldId(SampleFieldId.FAVORITE_SITE_URL_3)
                    .build(ALL);

    private static final FieldPath CONFIGURATION_EMAIL_MAX_SIZE = from(SampleModel.class)
                    .get(SampleModel::getConfiguration)
                    .field(Configuration::getMaxEmailSize, Configuration::setMaxEmailSize)
                    .readable("configuration email maximum size")
                    .fieldId(SampleFieldId.CONFIGURATION_EMAIL_MAX_SIZE)
                    .build(ALL);

    private static final FieldPath CONFIGURATION_MIN_AGE = from(SampleModel.class)
                    .get(SampleModel::getConfiguration)
                    .field(Configuration::getMinAge, Configuration::setMinAge)
                    .readable("configuration minimum age")
                    .fieldId(SampleFieldId.CONFIGURATION_MIN_AGE)
                    .build(ALL);

    private static final FieldPath CONFIGURATION_MAILING_CAMPAIGN = from(SampleModel.class)
                    .get(SampleModel::getConfiguration)
                    .field(Configuration::isMailingCampaign, Configuration::setMailingCampaign)
                    .readable("configuration mailing campaign")
                    .fieldId(SampleFieldId.CONFIGURATION_MAILING_CAMPAIGN)
                    .build(ALL);

    private static final FieldPath LOGIN = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .field(Account::getLogin, Account::setLogin)
                    .readable("account login")
                    .fieldId(SampleFieldId.LOGIN)
                    .build(ALL);

    private static final FieldPath PASSWD = from(SampleModel.class)
                    .get(SampleModel::getAccount)
                    .field(Account::getPassword, Account::setPassword)
                    .readable("account password")
                    .fieldId(SampleFieldId.PASSWD)
                    .build(ALL);

    @Override
    public List<FieldPath> values() {
        return ALL;
    }
}
