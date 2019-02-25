/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.template;

import static io.doov.core.dsl.template.ParameterNamespace.$String;
import static io.doov.sample.field.dsl.DslSampleModel.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;

import io.doov.assertions.Assertions;
import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.field.types.TextFieldInfo;
import io.doov.core.dsl.template.TemplateRule;
import io.doov.core.dsl.template.TemplateRule.Rule1;
import io.doov.sample.model.SampleModels;

public class TemplateIntegrationTest {
    /*
    @Test
    void templateTest() {
        Rule1<String, StringFieldInfo> siteContainsGoogle =
                DOOV.template($String).with(site -> site.contains("google"));

        FieldModel model = SampleModels.wrapper();

        Assertions.assertThat(siteContainsGoogle.bind(favoriteSiteUrl1)).validates(model);
        Assertions.assertThat(siteContainsGoogle.bind(favoriteSiteUrl2)).doesNotValidate(model);
        Assertions.assertThat(siteContainsGoogle.bind(favoriteSiteUrl3)).doesNotValidate(model);

    }

    @Test
    void twoParamsTest() {
        TemplateRule.Rule2<String, StringFieldInfo, String, StringFieldInfo> validateSite =
                DOOV.template($String,$String).with(
                        (url,name) -> url.contains(name.mapToString(String::toLowerCase))
                );

        FieldModel model = SampleModels.wrapper();

        List<StringFieldInfo> urls = favoriteSiteUrl().collect(Collectors.toList());
        List<StringFieldInfo> names = favoriteSiteName().collect(Collectors.toList());

        for(int i = 0; i < 3; i++) {
            Assertions.assertThat(validateSite.bind(urls.get(i),names.get(i))).validates(model);
        }

    }
    */

}
