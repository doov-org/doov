package io.doov.sample.field;

import static io.doov.core.dsl.runtime.FieldChainBuilder.from;
import static io.doov.core.dsl.runtime.FieldChainBuilder.generify;

import java.time.LocalDate;
import java.util.*;

import io.doov.core.dsl.runtime.RuntimeField;
import io.doov.core.dsl.runtime.RuntimeFieldRegistry;
import io.doov.sample.model.*;

public class RuntimePath extends RuntimeFieldRegistry<SampleModel> {

    private static final List<RuntimeField<SampleModel, Object>> ALL = new ArrayList<>();

    public static final RuntimeField<SampleModel, Long> USER_ID = from(SampleModel.class, SampleFieldId.USER_ID)
                    .readable("user id")
                    .get(SampleModel::getUser, SampleModel::setUser, User::new)
                    .field(User::getId, User::setId, Long.TYPE)
                    .register(ALL);

    public static final RuntimeField<SampleModel, Long> ACCOUNT_ID = from(SampleModel.class, SampleFieldId.ACCOUNT_ID)
                    .readable("account id")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .field(Account::getId, Account::setId, Long.TYPE)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> FIRST_NAME = from(SampleModel.class, SampleFieldId.FIRST_NAME)
                    .readable("user first name")
                    .get(SampleModel::getUser, SampleModel::setUser, User::new)
                    .field(User::getFirstName, User::setFirstName, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> LAST_NAME = from(SampleModel.class, SampleFieldId.LAST_NAME)
                    .readable("user last name")
                    .get(SampleModel::getUser, SampleModel::setUser, User::new)
                    .field(User::getLastName, User::setLastName, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> FULLNAME = from(SampleModel.class, SampleFieldId.FULLNAME)
                    .readable("user full name")
                    .get(SampleModel::getUser, SampleModel::setUser, User::new)
                    .field(User::getFullName, User::setFullName, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, LocalDate> BIRTHDATE = from(SampleModel.class, SampleFieldId.BIRTHDATE)
                    .readable("user birth date")
                    .get(SampleModel::getUser, SampleModel::setUser, User::new)
                    .field(User::getBirthDate, User::setBirthDate, LocalDate.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> EMAIL = from(SampleModel.class, SampleFieldId.EMAIL)
                    .readable("account email")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .field(Account::getEmail, Account::setEmail, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, Language> LANGUAGE = from(SampleModel.class, SampleFieldId.LANGUAGE)
                    .readable("account language")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .field(Account::getLanguage, Account::setLanguage, Language.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, Timezone> TIMEZONE = from(SampleModel.class, SampleFieldId.TIMEZONE)
                    .readable("account timezone")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .field(Account::getTimezone, Account::setTimezone, Timezone.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, Country> COUNTRY = from(SampleModel.class, SampleFieldId.COUNTRY)
                    .readable("account country")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .field(Account::getCountry, Account::setCountry, Country.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> PHONE_NUMBER = from(SampleModel.class, SampleFieldId.PHONE_NUMBER)
                    .readable("account phone number")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .field(Account::getPhoneNumber, Account::setPhoneNumber, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, Boolean> EMAIL_ACCEPTED = from(SampleModel.class, SampleFieldId.EMAIL_ACCEPTED)
                    .readable("account email accepted")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .field(Account::getAcceptEmail, Account::setAcceptEmail, Boolean.TYPE)
                    .register(ALL);

    public static final RuntimeField<SampleModel, LocalDate> CREATION_DATE = from(SampleModel.class, SampleFieldId.CREATION_DATE)
                    .readable("account creation date")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .field(Account::getCreationDate, Account::setCreationDate, LocalDate.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, Collection<EmailType>> EMAILS_PREFERENCES = from(SampleModel.class, SampleFieldId.EMAILS_PREFERENCES)
                    .readable("account email preferences")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .field(Account::getEmailTypes, Account::setEmailTypes, generify(Collection.class), EmailType.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> FAVORITE_SITE_NAME_1 = from(SampleModel.class, SampleFieldId.FAVORITE_SITE_NAME_1)
                    .readable("account favorite web site name 1")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .list(Account::getTop3WebSite, Account::setTop3WebSite, FavoriteWebsite::new)
                    .field(FavoriteWebsite::getName, FavoriteWebsite::setName, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> FAVORITE_SITE_NAME_2 = from(SampleModel.class, SampleFieldId.FAVORITE_SITE_NAME_2)
                    .readable("account favorite web site name 2")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .list(Account::getTop3WebSite, Account::setTop3WebSite, FavoriteWebsite::new)
                    .field(FavoriteWebsite::getName, FavoriteWebsite::setName, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> FAVORITE_SITE_NAME_3 = from(SampleModel.class, SampleFieldId.FAVORITE_SITE_NAME_3)
                    .readable("account favorite web site name 3")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .list(Account::getTop3WebSite, Account::setTop3WebSite, FavoriteWebsite::new)
                    .field(FavoriteWebsite::getName, FavoriteWebsite::setName, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> FAVORITE_SITE_URL_1 = from(SampleModel.class, SampleFieldId.FAVORITE_SITE_URL_1)
                    .readable("account favorite web site url 1")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .list(Account::getTop3WebSite, Account::setTop3WebSite, FavoriteWebsite::new)
                    .field(FavoriteWebsite::getUrl, FavoriteWebsite::setUrl, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> FAVORITE_SITE_URL_2 = from(SampleModel.class, SampleFieldId.FAVORITE_SITE_URL_2)
                    .readable("account favorite web site url 2")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .list(Account::getTop3WebSite, Account::setTop3WebSite, FavoriteWebsite::new)
                    .field(FavoriteWebsite::getUrl, FavoriteWebsite::setUrl, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> FAVORITE_SITE_URL_3 = from(SampleModel.class, SampleFieldId.FAVORITE_SITE_URL_3)
                    .readable("account favorite web site url 3")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .list(Account::getTop3WebSite, Account::setTop3WebSite, FavoriteWebsite::new)
                    .field(FavoriteWebsite::getUrl, FavoriteWebsite::setUrl, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, Integer> CONFIGURATION_EMAIL_MAX_SIZE = from(SampleModel.class, SampleFieldId.CONFIGURATION_EMAIL_MAX_SIZE)
                    .readable("configuration email maximum size")
                    .get(SampleModel::getConfiguration, SampleModel::setConfiguration, Configuration::new)
                    .field(Configuration::getMaxEmailSize, Configuration::setMaxEmailSize, Integer.TYPE)
                    .register(ALL);

    public static final RuntimeField<SampleModel, Integer> CONFIGURATION_MIN_AGE = from(SampleModel.class, SampleFieldId.CONFIGURATION_MIN_AGE)
                    .readable("configuration minimum age")
                    .get(SampleModel::getConfiguration, SampleModel::setConfiguration, Configuration::new)
                    .field(Configuration::getMinAge, Configuration::setMinAge, Integer.TYPE)
                    .register(ALL);

    public static final RuntimeField<SampleModel, Boolean> CONFIGURATION_MAILING_CAMPAIGN = from(SampleModel.class, SampleFieldId.CONFIGURATION_MAILING_CAMPAIGN)
                    .readable("configuration mailing campaign")
                    .get(SampleModel::getConfiguration, SampleModel::setConfiguration, Configuration::new)
                    .field(Configuration::isMailingCampaign, Configuration::setMailingCampaign, Boolean.TYPE)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> LOGIN = from(SampleModel.class, SampleFieldId.LOGIN)
                    .readable("account login")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .field(Account::getLogin, Account::setLogin, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> PASSWD = from(SampleModel.class, SampleFieldId.PASSWD)
                    .readable("account password")
                    .get(SampleModel::getAccount, SampleModel::setAccount, Account::new)
                    .field(Account::getPassword, Account::setPassword, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> ADDRESS = from(SampleModel.class, CoreFieldId.ADDRESS)
                    .readable("user address")
                    .get(SampleModel::getUser, SampleModel::setUser, User::new)
                    .field(User::getAddress, User::setAddress, String.class)
                    .register(ALL);

    public static final RuntimeField<SampleModel, String> TEL = from(SampleModel.class, CoreFieldId.TEL)
                    .readable("user tel")
                    .get(SampleModel::getUser, SampleModel::setUser, User::new)
                    .field(User::getTel, User::setTel, String.class)
                    .register(ALL);

    public static RuntimePath INSTANCE = new RuntimePath();

    private RuntimePath() {
        super(ALL);
    }

}
