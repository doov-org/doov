package org.modelmap.sample.validation;

import static org.modelmap.core.dsl.DSL.matchAny;
import static org.modelmap.sample.field.SampleFieldIdInfo.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.modelmap.core.dsl.DSL;
import org.modelmap.core.dsl.lang.RuleRegistry;
import org.modelmap.core.dsl.lang.ValidationRule;
import org.modelmap.sample.model.*;

public enum Registry implements RuleRegistry {

    ACCOUNT;

    public static final ValidationRule ACCOUNT_VALID_EMAIL;
    public static final ValidationRule ACCOUNT_VALID_COUNTRY;
    public static final ValidationRule ACCOUNT_VALID_COUNTRY_1;
    public static final ValidationRule ACCOUNT_VALID_COUNTRY_2;
    public static final ValidationRule ACCOUNT_VALID_COUNTRY_3;

    static {
        ACCOUNT_VALID_EMAIL = DSL.when(accountEmail().matches("\\w+[@]\\w+\\.com")
                        .or(accountEmail().matches("\\w+[@]\\w+\\.fr")))
                        .validate()
                        .withMessage("email finishes with .com or .fr")
                        .registerOn(ACCOUNT);

        ACCOUNT_VALID_COUNTRY = DSL.when(matchAny(accountCountry().eq(Country.FR)
                                        .and(accountLanguage().eq(Language.FR))
                                        .and(accountPhoneNumber().startsWith("+33")),
                        accountCountry().eq(Country.UK)
                                        .and(accountLanguage().eq(Language.EN))
                                        .and(accountPhoneNumber().startsWith("+45"))))
                        .validate()
                        .registerOn(ACCOUNT);

        ACCOUNT_VALID_COUNTRY_1 = DSL.when(
                        accountCountry().eq(Country.UK)
                                        .and(accountLanguage().eq(Language.EN))
                                        .and(accountPhoneNumber().startsWith("+45")))
                        .validate()
                        .registerOn(ACCOUNT);

        ACCOUNT_VALID_COUNTRY_2 = DSL.when(
                        accountCountry().eq(Country.UK)
                                        .and(accountLanguage().eq(Language.EN))
                                        .and(accountPhoneNumber().startsWith("+45"))
                                        .and(accountTimezone().eq(Timezone.ETC_GMT))
                                        .and(accountEmailAccepted().isTrue()))
                        .validate()
                        .registerOn(ACCOUNT);

        ACCOUNT_VALID_COUNTRY_3 = DSL.when(
                        accountCountry().eq(Country.UK)
                                        .and(accountLanguage().eq(Language.EN))
                                        .and(accountPhoneNumber().startsWith("+45"))
                                        .and(accountTimezone().eq(Timezone.ETC_GMT))
                                        .and(accountEmailAccepted().isTrue()
                                                        .and(userFirstName().eq("Foo"))
                                                        .and(userLastName().eq("Bar"))
                                                        .and(userBirthdate().eq(LocalDate.of(1980, 8, 1)))))
                        .validate()
                        .registerOn(ACCOUNT);
    }

    private List<ValidationRule> rules = new ArrayList<>();

    @Override
    public void register(ValidationRule rule) {
        rules.add(rule);
    }

    @Override
    public Stream<ValidationRule> stream() {
        return rules.stream();
    }

}
