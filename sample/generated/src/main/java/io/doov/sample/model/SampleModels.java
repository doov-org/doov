package io.doov.sample.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;

import io.doov.core.FieldModel;

public class SampleModels {

    public static SampleModel sample() {
        User user = new User();
        user.setId(1);
        user.setFirstName("Foo");
        user.setLastName("Bar");
        user.setBirthDate(LocalDate.of(1980, 8, 1));

        Account account = new Account();
        account.setId(9);
        account.setAcceptEmail(true);
        account.setEmail("foo@bar.com");
        account.setEmailTypes(EnumSet.of(EmailType.ADMINISTRATOR, EmailType.PRIVATE));
        account.setLanguage(Language.FR);
        account.setCountry(Country.FR);
        account.setLogin("foobar");
        account.setPassword("PqssW0rd");
        account.setPhoneNumber("+33102030409");
        account.setTimezone(Timezone.ETC_GMT);
        account.setTop3WebSite(Arrays.asList(
                        FavoriteWebsite.webSite("Google", "www.google.com"),
                        FavoriteWebsite.webSite("Bing", "www.bing.com"),
                        FavoriteWebsite.webSite("Yahoo", "www.yahoo.com")));

        Configuration configuration = new Configuration();
        configuration.setMaxEmailSize(25);

        SampleModel model = new SampleModel();
        model.setUser(user);
        model.setAccount(account);
        model.setConfiguration(configuration);

        return model;
    }

    public static FieldModel wrapper() {
        return new SampleModelWrapper(sample());
    }
}
