package org.modelmap.sample.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.modelmap.core.dsl.lang.EValidity.INVALID;
import static org.modelmap.core.dsl.lang.EValidity.VALID;
import static org.modelmap.sample.validation.Rules.EMAIL_VALID;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;
import org.modelmap.sample.model.*;

public class RulesTest {

    private SampleModelWrapper wrapper;
    private SampleModel model;
    private Account account;
    private User user;

    @Before
    public void before() {
        model = new SampleModel();

        user = new User();
        user.setId(1);
        user.setFirstName("Foo");
        user.setLastName("Bar");
        user.setBirthDate(LocalDate.of(1980, 8, 1));
        model.setUser(user);

        account = new Account();
        account.setId(9);
        account.setAcceptEmail(true);
        account.setEmail("foo@bar.com");
        account.setEmailTypes(EnumSet.of(EmailType.ADMINISTRATOR, EmailType.PRIVATE));
        account.setLanguage(Language.EN);
        account.setLogin("foobar");
        account.setPassword("PqssW0rd");
        account.setPhoneNumber("0102030409");
        account.setTimezone(Timezone.ETC_GMT);
        account.setTop3WebSite(Arrays.asList(
                        FavoriteWebsite.webSite("Google", "www.google.com"),
                        FavoriteWebsite.webSite("Bing", "www.bing.com"),
                        FavoriteWebsite.webSite("Yahoo", "www.yahoo.com")));
        model.setAccount(account);

        wrapper = new SampleModelWrapper(model);
    }

    @Test
    public void test_valid_email() {
        System.out.println(EMAIL_VALID.readable());
        assertThat(EMAIL_VALID.executeOn(wrapper).validity()).isEqualTo(VALID);

        account.setEmail("test@test.gh");
        assertThat(EMAIL_VALID.executeOn(wrapper).validity()).isEqualTo(INVALID);
    }

}
