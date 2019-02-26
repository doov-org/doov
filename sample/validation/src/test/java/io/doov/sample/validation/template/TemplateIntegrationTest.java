/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.template;

import static io.doov.core.dsl.template.TemplateParam.$String;
import static io.doov.sample.field.dsl.DslSampleModel.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import io.doov.assertions.Assertions;
import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.template.TemplateMapping;
import io.doov.core.dsl.template.TemplateRule;
import io.doov.core.dsl.template.TemplateRule.Rule1;
import io.doov.sample.model.SampleModels;

public class TemplateIntegrationTest {
    @Test
    void templateTest() {
        Rule1<StringFieldInfo> siteContainsGoogle =
                DOOV.template($String).rule(site -> site.contains("google"));

        FieldModel model = SampleModels.wrapper();

        Assertions.assertThat(siteContainsGoogle.bind(favoriteSiteUrl1)).validates(model);
        Assertions.assertThat(siteContainsGoogle.bind(favoriteSiteUrl2)).doesNotValidate(model);
        Assertions.assertThat(siteContainsGoogle.bind(favoriteSiteUrl3)).doesNotValidate(model);

    }

    @Test
    void twoParamsTest() {
        TemplateRule.Rule2<StringFieldInfo, StringFieldInfo> validateSite =
                DOOV.template($String,$String).rule(
                        (url,name) -> url.contains(name.mapToString(String::toLowerCase))
                );

        FieldModel model = SampleModels.wrapper();

        List<StringFieldInfo> urls = favoriteSiteUrl().collect(Collectors.toList());
        List<StringFieldInfo> names = favoriteSiteName().collect(Collectors.toList());

        for(int i = 0; i < 3; i++) {
            Assertions.assertThat(validateSite.bind(urls.get(i),names.get(i))).validates(model);
        }

    }

    @Test
    void mappingTemplateTest() {
        TemplateMapping.Map2<StringFieldInfo,StringFieldInfo> emplace = DOOV.template($String,$String).mapping(
                (from, dest) -> DOOV.map(from).to(dest)
        );

        FieldModel model = SampleModels.wrapper();

        emplace.bind(favoriteSiteName1,favoriteSiteName2).executeOn(model,model);

        org.junit.jupiter.api.Assertions.assertEquals(model.get(favoriteSiteName1),model.get(favoriteSiteName2));
    }
}
