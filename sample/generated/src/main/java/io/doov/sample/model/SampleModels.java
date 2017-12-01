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
import java.util.Arrays;
import java.util.EnumSet;

import io.doov.core.FieldModel;

public class SampleModels {

    public static SampleModel sample() {
        User user = new User();
        user.setId(1);
        user.setFirstName("Foo");
        user.setLastName("BAR");
        user.setBirthDate(LocalDate.of(1980, 8, 1));

        Account account = new Account();
        account.setId(9);
        account.setCreationDate(LocalDate.of(2017, 1, 1));
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
        configuration.setMinAge(18);

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
