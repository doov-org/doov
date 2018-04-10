/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.model;

import static io.doov.core.dsl.runtime.FieldChainBuilder.from;
import static io.doov.sample.field.SampleFieldId.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.doov.core.dsl.runtime.RuntimeField;
import io.doov.core.dsl.runtime.RuntimeFieldRegistry;
import io.doov.sample.field.SampleFieldId;

public class FieldRegistry extends RuntimeFieldRegistry<SampleModel> {

    private static final List<RuntimeField<SampleModel, Object>> ALL = new ArrayList<>();

    public final static RuntimeField<SampleModel, Boolean> account_accept_email = from(SampleModel.class, EMAIL_ACCEPTED)
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .field(Account::getAcceptEmail, Account::setAcceptEmail, Boolean.TYPE)
                    .register(ALL);

    public final static RuntimeField<SampleModel, Country> account_country = from(SampleModel.class, COUNTRY)
                    ._transient(true)
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .field(Account::getCountry, Account::setCountry, Country.class)
                    .register(ALL);

    public final static RuntimeField<SampleModel, String> favorite_site_1_name = from(SampleModel.class, FAVORITE_SITE_NAME_1)
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .list(Account::getTop3WebSite, Account::setTop3WebSite, FavoriteWebsite::new)
                    .field(FavoriteWebsite::getName, FavoriteWebsite::setName, String.class)
                    .register(ALL);

    public final static RuntimeField<SampleModel, String> favorite_site_2_url = from(SampleModel.class, FAVORITE_SITE_URL_2)
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .list(Account::getTop3WebSite, Account::setTop3WebSite, FavoriteWebsite::new)
                    .field(FavoriteWebsite::getUrl, FavoriteWebsite::setUrl, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, Long> user_id = from(SampleModel.class, USER_ID)
                    .get(SampleModel::getUser, SampleModel::setUser, User::new)
                    .field(User::getId, User::setId, Long.TYPE)
                    .register(ALL);

    public static final RuntimeField<SampleModel, LocalDate> user_birth_date = from(SampleModel.class, BIRTHDATE)
                    .get(SampleModel::getUser, SampleModel::setUser, User::new)
                    .field(User::getBirthDate, User::setBirthDate, LocalDate.class)
                    .register(ALL);

    public static final FieldRegistry INSTANCE = new FieldRegistry();

    private FieldRegistry() {
        super(ALL);
    }

}
