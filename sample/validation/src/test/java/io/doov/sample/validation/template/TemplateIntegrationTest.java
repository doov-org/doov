/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.template;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.map;
import static io.doov.core.dsl.DOOV.template;
import static io.doov.core.dsl.template.ParameterTypes.$String;
import static io.doov.sample.field.dsl.DslSampleModel.favoriteSiteName;
import static io.doov.sample.field.dsl.DslSampleModel.favoriteSiteName1;
import static io.doov.sample.field.dsl.DslSampleModel.favoriteSiteName2;
import static io.doov.sample.field.dsl.DslSampleModel.favoriteSiteUrl;
import static io.doov.sample.field.dsl.DslSampleModel.favoriteSiteUrl1;
import static io.doov.sample.field.dsl.DslSampleModel.favoriteSiteUrl2;
import static io.doov.sample.field.dsl.DslSampleModel.favoriteSiteUrl3;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.template.TemplateMapping.Map2;
import io.doov.core.dsl.template.TemplateRule.Rule1;
import io.doov.core.dsl.template.TemplateRule.Rule2;
import io.doov.sample.model.SampleModels;

public class TemplateIntegrationTest {
    FieldModel model = SampleModels.wrapper();

    @Test
    void templateTest() {
        Rule1<StringFieldInfo> siteContainsGoogle = template($String).rule(site -> site.contains("google"));

        assertThat(siteContainsGoogle.bind(favoriteSiteUrl1)).validates(model);
        assertThat(siteContainsGoogle.bind(favoriteSiteUrl2)).doesNotValidate(model);
        assertThat(siteContainsGoogle.bind(favoriteSiteUrl3)).doesNotValidate(model);

    }

    @Test
    void twoParamsTest() {
        Rule2<StringFieldInfo, StringFieldInfo> validateSite = template($String, $String)
                .rule((url, name) -> url.contains(name.mapToString(String::toLowerCase)));
        List<StringFieldInfo> urls = favoriteSiteUrl().collect(toList());
        List<StringFieldInfo> names = favoriteSiteName().collect(toList());

        for (int i = 0; i < 3; i++) {
            assertThat(validateSite.bind(urls.get(i), names.get(i))).validates(model);
        }

    }

    @Test
    void mappingTemplateTest() {
        Map2<StringFieldInfo, StringFieldInfo> emplace = template($String, $String)
                .mapping((from, dest) -> map(from).to(dest));
        emplace.bind(favoriteSiteName1, favoriteSiteName2).executeOn(model, model);

        assertThat(model.get(favoriteSiteName1)).isEqualTo(model.get(favoriteSiteName2));
    }
}
