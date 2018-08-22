package io.doov.core.dsl;

import static io.doov.core.dsl.DOOV.*;
import static io.doov.core.dsl.mapping.TypeConverters.biConverter;
import static io.doov.core.dsl.mapping.TypeConverters.converter;
import static io.doov.core.dsl.mapping.TypeConverters.counter;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.sample.field.dsl.DslSampleModel.*;
import static io.doov.sample.field.dsl.DslSampleModel.when;
import static io.doov.sample.model.SampleModels.sample;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.impl.DefaultContext;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.mapping.MappingRegistry;
import io.doov.core.dsl.meta.MappingMetadata;
import io.doov.core.dsl.meta.MappingOperator;
import io.doov.core.dsl.meta.ast.AstMarkdownVisitor;
import io.doov.sample.model.*;
import io.doov.sample.wrapper.SampleModelWrapper;

public class DOOVMappingTest {

    private static final TypeConverter<String, String> STRIPPING_COUNTRY_CODE =
            converter(in -> in.startsWith("+33") ? "0" + in.substring(3) : in, "", "stripping country code");

    private static final TypeConverter<String, Integer> LENGTH_OR_ZERO = converter(String::length, 0, "string length");

    private static final BiTypeConverter<String, String, String> FULL_NAME =
            biConverter((i, j) -> i + " " + j, "", "", "firstName lastName");

    private static final BiTypeConverter<Collection<EmailType>, String, String> CONVERTER =
            biConverter((i, j) -> {
                String[] em = j.split("@");
                return em[0] + "+" + i.size() + "@" + em[1];
            }, "", "WTF");

    private MappingRegistry mappings;

    @BeforeEach
    void setUp() {
        mappings = mappings(
                when(accountLanguage.eq(Language.FR)).then(
                        map(accountPhoneNumber)
                                .using(STRIPPING_COUNTRY_CODE)
                                .to(accountPhoneNumber)),

                map(accountId).to(configurationMaxLong),

                map(userFirstName)
                        .using(LENGTH_OR_ZERO)
                        .to(configurationMinAge),

                map(userId).to(userId),

                map(userFirstName, userLastName)
                        .using(FULL_NAME)
                        .to(userFirstName),

                when(accountAcceptEmail.isTrue()).then(
                        map(accountPreferencesMail, accountEmail)
                                .using(CONVERTER)
                                .to(accountEmail))
                        .otherwise(
                                map(() -> false).to(configurationMailingCampaign)),

                mapRange(1, 4, i ->
                        map(favoriteSiteName(i)).to(favoriteSiteName(i))),

                map(favoriteSiteName())
                        .using(counter("email size"))
                        .to(configurationMaxEmailSize),

                when(matchAny(favoriteSiteName(), DefaultCondition::isNotNull)).then(
                        mapRange(1, 4, i ->
                                map(favoriteSiteName(i)).to(favoriteSiteName(i)))),

                map(() -> Country.FR)
                        .using(converter(this::countryToLanguage, ""))
                        .to(accountLanguage),

                when(accountLogin.isNotNull()).then(
                        map(() -> true).to(accountAcceptEmail)),

                map((model, context) -> ((MyContext) context).isMine()).to(configurationMailingCampaign),

                map(Timezone.ETC_GMT).to(accountTimezone),
                mapNull(accountTimezone)
        );
    }

    private Language countryToLanguage(Country c) {
        switch (c) {
            case US:
            case UK:
                return Language.EN;
            case CAN:
            case FR:
                return Language.FR;
        }
        return null;
    }

    private class MyContext extends DefaultContext {

        private final boolean isMine = true;

        MyContext() {
            super(MappingMetadata.mappings(MappingOperator.mappings));
        }

        boolean isMine() {
            return isMine;
        }
    }

    @Test
    void doov() {
        SampleModelWrapper sample = new SampleModelWrapper(sample());
        SampleModelWrapper copy = new SampleModelWrapper();
        mappings.validateAndExecute(sample, copy, new MyContext());
        assertThat(copy.getModel().getConfiguration().getMaxLong()).isEqualTo(9);
        assertThat(copy.getModel().getConfiguration().getMinAge()).isEqualTo(3);
        assertThat(copy.getModel().getAccount().getPhoneNumber()).isEqualTo("0102030409");
        assertThat(copy.getModel().getUser().getFirstName()).isEqualTo("Foo BAR");
        assertThat(copy.getModel().getAccount().getEmail()).isEqualTo("foo+2@bar.com");
        assertThat(copy.getModel().getConfiguration().getMaxEmailSize()).isEqualTo(3);
        assertThat(copy.getModel().getAccount().getLanguage()).isEqualTo(Language.FR);
        assertThat(copy.getModel().getAccount().getAcceptEmail()).isTrue();
        assertThat(copy.getModel().getConfiguration().isMailingCampaign()).isTrue();
        assertThat(copy.getModel().getAccount().getTimezone()).isNull();
    }

    @Test
    void mapping_otherwise() {
        SampleModelWrapper sample = new SampleModelWrapper(sample());
        SampleModelWrapper copy = new SampleModelWrapper();
        sample.set(accountAcceptEmail, false);
        mappings.validateAndExecute(sample, copy, new MyContext());
        assertThat(copy.getModel().getConfiguration().getMaxLong()).isEqualTo(9);
        assertThat(copy.getModel().getConfiguration().getMinAge()).isEqualTo(3);
        assertThat(copy.getModel().getAccount().getPhoneNumber()).isEqualTo("0102030409");
        assertThat(copy.getModel().getUser().getFirstName()).isEqualTo("Foo BAR");
        assertThat(copy.getModel().getAccount().getEmail()).isNull();
        assertThat(copy.getModel().getConfiguration().getMaxEmailSize()).isEqualTo(3);
        assertThat(copy.getModel().getAccount().getLanguage()).isEqualTo(Language.FR);
        assertThat(copy.getModel().getConfiguration().isMailingCampaign()).isTrue();
    }

    @Test
    void print_ast() {
        System.out.println(mappings.stream().map(Readable::readable).collect(Collectors.joining("\n\n")));
    }

    @Test
    void print_ast_markdown() {
        StringBuilder sb = new StringBuilder();
        AstMarkdownVisitor visitor = new AstMarkdownVisitor(sb, BUNDLE, Locale.getDefault());
        mappings.stream().forEach(m -> {
            m.accept(visitor, 0);
            sb.append("\n");
        });
        System.out.println(sb.toString());
    }
}
