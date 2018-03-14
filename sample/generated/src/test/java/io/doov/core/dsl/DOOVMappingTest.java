package io.doov.core.dsl;

import static io.doov.core.dsl.DOOV.map;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.mapping.TypeConverters.*;
import static io.doov.core.dsl.mapping.DefaultMappingRegistry.mappings;
import static io.doov.sample.field.dsl.DslSampleModel.*;
import static io.doov.sample.model.SampleModels.sample;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.lang.BiTypeConverter;
import io.doov.core.dsl.lang.NaryTypeConverter;
import io.doov.core.dsl.lang.MappingRegistry;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.TypeConverter;
import io.doov.core.dsl.meta.ast.*;
import io.doov.sample.model.Country;
import io.doov.sample.model.EmailType;
import io.doov.sample.model.Language;
import io.doov.sample.model.SampleModelWrapper;

public class DOOVMappingTest {

    private static final TypeConverter<String, String> STRIPPING_COUNTRY_CODE =
            converter(in -> in.startsWith("+33") ? "0" + in.substring(3, in.length()) : in, "",
                    "stripping country code");

    private static final TypeConverter<String, Integer> LENGTH_OR_ZERO =
            converter(String::length, 0, "string length");

    private static final BiTypeConverter<String, String, String> FULL_NAME =
            biConverter((i, j) -> i + " " + j, "", "", "firstName lastName");

    private static final BiTypeConverter<Collection<EmailType>, String, String> CONVERTER =
            biConverter((i, j) -> {
                String[] em = j.split("@");
                return em[0] + "+" + i.size() + "@" + em[1];
            }, "", "WTF");

    private static final NaryTypeConverter<Integer> EMAIL_SIZE =
            nConverter((model, fieldInfos) ->
                    (int) fieldInfos.stream()
                            .map(f -> model.get(f.id()))
                            .filter(Objects::nonNull).count(), "favorite web site size -> email size");

    private MappingRegistry mappings;

    @BeforeEach
    void setUp() {
        mappings = mappings(
                when(accountLanguage().eq(Language.FR)).then(
                        map(accountPhoneNumber())
                                .using(STRIPPING_COUNTRY_CODE)
                                .to(accountPhoneNumber())),

                map(accountId())
                        .to(configurationMaxLong()),

                map(userFirstName())
                        .using(LENGTH_OR_ZERO)
                        .to(configurationMinAge()),

                map(userId())
                        .to(userId()),

                map(userFirstName(), userLastName())
                        .using(FULL_NAME)
                        .to(userFirstName()),
                when(accountAcceptEmail().isTrue()).then(
                        map(accountPreferencesMail(), accountEmail())
                                .using(CONVERTER)
                                .to(accountEmail()))
                        .otherwise(
                                map(() -> false)
                                        .to(configurationMailingCampaign())),

                map(favoriteSiteName1(), favoriteSiteName2(), favoriteSiteName3())
                        .using(EMAIL_SIZE)
                        .to(configurationMaxEmailSize()),

                map(() -> Country.FR)
                        .using(valueConverter(this::countryToLanguage, ""))
                        .to(accountLanguage()),

                when(accountLogin().isNotNull()).then(
                        map(() -> true)
                                .to(accountAcceptEmail()))
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

    @Test
    void doov() {
        SampleModelWrapper sample = new SampleModelWrapper(sample());
        SampleModelWrapper copy = new SampleModelWrapper();
        mappings.validateAndExecute(sample, copy);
        assertThat(copy.getModel().getConfiguration().getMaxLong()).isEqualTo(9);
        assertThat(copy.getModel().getConfiguration().getMinAge()).isEqualTo(3);
        assertThat(copy.getModel().getAccount().getPhoneNumber()).isEqualTo("0102030409");
        assertThat(copy.getModel().getUser().getFirstName()).isEqualTo("Foo BAR");
        assertThat(copy.getModel().getAccount().getEmail()).isEqualTo("foo+2@bar.com");
        assertThat(copy.getModel().getConfiguration().getMaxEmailSize()).isEqualTo(3);
        assertThat(copy.getModel().getAccount().getLanguage()).isEqualTo(Language.FR);
        assertThat(copy.getModel().getAccount().getAcceptEmail()).isTrue();
    }

    @Test
    void mapping_otherwise() {
        SampleModelWrapper sample = new SampleModelWrapper(sample());
        SampleModelWrapper copy = new SampleModelWrapper();
        sample.set(accountAcceptEmail(), false);
        mappings.validateAndExecute(sample, copy);
        assertThat(copy.getModel().getConfiguration().getMaxLong()).isEqualTo(9);
        assertThat(copy.getModel().getConfiguration().getMinAge()).isEqualTo(3);
        assertThat(copy.getModel().getAccount().getPhoneNumber()).isEqualTo("0102030409");
        assertThat(copy.getModel().getUser().getFirstName()).isEqualTo("Foo BAR");
        assertThat(copy.getModel().getAccount().getEmail()).isNull();
        assertThat(copy.getModel().getConfiguration().getMaxEmailSize()).isEqualTo(3);
        assertThat(copy.getModel().getAccount().getLanguage()).isEqualTo(Language.FR);
        assertThat(copy.getModel().getConfiguration().isMailingCampaign()).isFalse();
    }

    @Test
    void print_ast() {
        System.out.println(mappings.stream().map(Readable::readable).collect(Collectors.joining("\n\n")));
    }

    @Test
    void print_ast_markdown() {
        StringBuilder sb = new StringBuilder();
        AstMarkdownVisitor visitor = new AstMarkdownVisitor(sb, new ReadableResourceProvider(), Locale.getDefault());
        mappings.stream().forEach(m -> {
            m.accept(visitor, 0);
            sb.append("\n");
        });
        System.out.println(sb.toString());
    }
}
