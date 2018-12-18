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
import io.doov.core.dsl.mapping.*;
import io.doov.core.dsl.meta.MappingMetadata;
import io.doov.core.dsl.meta.MappingOperator;
import io.doov.core.dsl.meta.ast.AstMarkdownVisitor;
import io.doov.core.serial.TypeAdapters;
import io.doov.sample.field.dsl.DslSampleModel;
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

    private static final FunctionInput<Boolean> functionInput = new FunctionInput<>(
            MappingMetadata.inputMetadata("is mine"), (model, context) -> ((MyContext) context).isMine());

    private static final ConsumerOutput<String> consumerOutput = new ConsumerOutput<>(
            MappingMetadata.outputMetadata("sysout"),(model, context, s) -> System.out.println(s));

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
                        map(favoriteSiteUrl(i)).to(favoriteSiteUrl(i))),

                mapWithIndex(favoriteSiteName(), (field, index) ->
                        map(field).to(favoriteSiteName(index + 1))),

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

                map(functionInput).to(configurationMailingCampaign),

                map(userFirstName).to(consumerOutput),

                map(Timezone.ETC_GMT).to(accountTimezone),
                mapNull(accountTimezone),
                map(accountTimezone).using(TypeConverters.asString(TypeAdapters.INSTANCE)).to(DslSampleModel.userTel)
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
        assertThat(copy.getModel().getUser().getTel()).isEqualTo("ETC_GMT");
        assertThat(copy.getModel().getAccount().getTop3WebSite())
                .extracting(FavoriteWebsite::getName)
                .containsExactly("Google", "Bing", "Yahoo");
        assertThat(copy.getModel().getAccount().getTop3WebSite()).hasSize(3);
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
            visitor.browse(m.metadata(), 0);
            sb.append("\n");
        });
        System.out.println(sb.toString());
    }
}
