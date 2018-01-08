/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.i18n;

import static io.doov.core.dsl.DOOV.count;
import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.core.dsl.time.TemporalAdjuster.firstDayOfMonth;
import static io.doov.sample.field.SampleFieldIdInfo.accountCreationDate;
import static io.doov.sample.field.SampleFieldIdInfo.accountEmail;
import static io.doov.sample.field.SampleFieldIdInfo.userBirthdate;
import static io.doov.sample.validation.SampleRules.*;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.*;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.ast.AstLineVisitor;

public class SampleRuleI18nTest {
    private static void print(SyntaxTree tree) {
        final StringBuilder sb = new StringBuilder();
        tree.accept(new AstLineVisitor(sb, BUNDLE, Locale.FRANCE), 0);
        System.out.println(sb.toString());
    }

    @Test
    public void test_RULE_EMAIL() {
        final ValidationRule rule = RULE_EMAIL;
        assertThat(rule.getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).type()).isEqualTo(FIELD_PREDICATE);

        print(rule);
    }

    @Test
    public void test_RULE_ACCOUNT() {
        final ValidationRule rule = RULE_ACCOUNT;
        assertThat(rule.getRootMetadata()).isInstanceOf(NaryMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(NARY_PREDICATE);
        assertThat(rule.getRootMetadata().children()).hasSize(3);
        assertThat(rule.getRootMetadata().children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(2)).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().children().get(2).type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(2).children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(2).children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(2).children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(2).children().get(1).type()).isEqualTo(FIELD_PREDICATE);

        print(rule);
    }

    @Test
    public void test_RULE_ACCOUNT_2() {
        final ValidationRule rule = RULE_ACCOUNT_2;

        assertThat(rule.getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0).type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0).children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0).children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0).children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0).children().get(1).type()).isEqualTo(FIELD_PREDICATE);
        print(rule);
    }

    @Test
    public void test_RULE_USER() {
        final ValidationRule rule = RULE_USER;
        assertThat(rule.getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0)).isInstanceOf(NaryMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).type()).isEqualTo(NARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children()).hasSize(2);
        assertThat(rule.getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).type()).isEqualTo(LEAF_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1)).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1).type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1).children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1).children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1).children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1).children().get(1).type()).isEqualTo(FIELD_PREDICATE);
        print(rule);
    }

    @Test
    public void test_RULE_USER_2() {
        final ValidationRule rule = RULE_USER_2;
        assertThat(rule.getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1)).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1).children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1).children().get(1)).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).children().get(1).type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1).children().get(1).children().get(0)).isInstanceOf(NaryMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).children().get(1).children().get(0).type()).isEqualTo(NARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1).children().get(1).children()).hasSize(2);
        assertThat(rule.getRootMetadata().children().get(1).children().get(1).children().get(0).children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).children().get(1).children().get(0).children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1).children().get(1).children().get(0).children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).children().get(1).children().get(0).children().get(1).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1).children().get(1).children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).children().get(1).children().get(1).type()).isEqualTo(LEAF_PREDICATE);
        print(rule);
    }

    @Test
    public void test_RULE_USER_ADULT() {
        final ValidationRule rule = RULE_USER_ADULT;
        assertThat(rule.getRootMetadata()).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) rule.getRootMetadata()).stream().collect(toList());
        assertThat(elts).hasSize(5);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0).isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(2).isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(3).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(3).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(4).isEqualTo(ElementType.VALUE);
        print(rule);
    }

    @Test
    public void test_RULE_USER_ADULT_FIRSTDAY() {
        final ValidationRule rule = RULE_USER_ADULT_FIRSTDAY;
        assertThat(rule.getRootMetadata()).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) rule.getRootMetadata()).stream().collect(toList());
        assertThat(elts).hasSize(7);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0).isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(2).isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(3).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(4).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(5).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(3).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(4).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(5).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(6).isEqualTo(ElementType.VALUE);
        print(rule);
    }

    @Test
    public void test_RULE_FIRST_NAME() {
        final ValidationRule rule = RULE_FIRST_NAME;
        assertThat(rule.getRootMetadata()).isInstanceOf(NaryMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(NARY_PREDICATE);
        assertThat(rule.getRootMetadata().children()).hasSize(1);
        assertThat(rule.getRootMetadata().children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) rule.getRootMetadata().children().get(0)).stream().collect(toList());
        assertThat(elts).hasSize(4);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0).isEqualTo(StringFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(2).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(3).isEqualTo(ElementType.VALUE);
        print(rule);
    }

    @Test
    public void test_RULE_ID() {
        final ValidationRule rule = RULE_ID;
        assertThat(rule.getRootMetadata()).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) rule.getRootMetadata()).stream().collect(toList());
        assertThat(elts).hasSize(2);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0).isEqualTo(LongFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        print(rule);
    }

    @Test
    public void test_RULE_AGE_2() {
        final ValidationRule rule = RULE_AGE_2;
        assertThat(rule.getRootMetadata()).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) rule.getRootMetadata()).stream().collect(toList());
        assertThat(elts).hasSize(6);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0).isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(2).isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(3).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(3).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(4).isEqualTo(ElementType.VALUE);
        assertThat(elts).extracting(Element::getType).element(5).isEqualTo(ElementType.STRING_VALUE);
        print(rule);
    }

    @Test
    public void test_RULE_SUM() {
        final ValidationRule rule = RULE_SUM;
        assertThat(rule.getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0)).isInstanceOf(NaryMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).type()).isEqualTo(NARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).type()).isEqualTo(LEAF_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children()).hasSize(2);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1).type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts0 = ((LeafMetadata) rule.getRootMetadata().children().get(0).children().get(0)).stream().collect(toList());
        assertThat(elts0).hasSize(3);
        final List<Element> elts1 = ((LeafMetadata) rule.getRootMetadata().children().get(0).children().get(1)).stream().collect(toList());
        assertThat(elts1).hasSize(3);
        print(rule);
    }

    @Test
    public void test_RULE_MIN() {
        final ValidationRule rule = RULE_MIN;
        assertThat(rule.getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0)).isInstanceOf(NaryMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).type()).isEqualTo(NARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).type()).isEqualTo(LEAF_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children()).hasSize(2);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1).type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts0 = ((LeafMetadata) rule.getRootMetadata().children().get(0).children().get(0)).stream().collect(toList());
        assertThat(elts0).hasSize(1);
        final List<Element> elts1 = ((LeafMetadata) rule.getRootMetadata().children().get(0).children().get(1)).stream().collect(toList());
        assertThat(elts1).hasSize(1);
        print(rule);
    }

    @Test
    public void test_RULE_DOUBLE_TEMPORAL() {
        final ValidationRule rule = DOOV
                        .when(userBirthdate().with(firstDayOfMonth())
                                        .ageAt(accountCreationDate().with(firstDayOfMonth()))
                                        .lesserThan(18))
                        .validate();
        assertThat(rule.getRootMetadata()).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) rule.getRootMetadata()).stream().collect(toList());
        assertThat(elts).hasSize(9);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0).isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(2).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(3).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(4).isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(5).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(6).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(7).isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(3).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(4).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(5).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(6).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(7).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(8).isEqualTo(ElementType.VALUE);
        print(rule);
    }

    @Test
    public void test_match_any() {
        final ValidationRule rule = DOOV.when(matchAny(accountEmail().startsWith("a"),
                        accountEmail().startsWith("b"))).validate();
        assertThat(rule.getRootMetadata()).isInstanceOf(NaryMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(NARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts0 = ((LeafMetadata) rule.getRootMetadata().children().get(0)).stream().collect(toList());
        assertThat(elts0).hasSize(3);
        final List<Element> elts1 = ((LeafMetadata) rule.getRootMetadata().children().get(1)).stream().collect(toList());
        assertThat(elts1).hasSize(3);
        print(rule);
    }

    @Test
    public void test_count_greater_than_2() {
        ValidationRule rule = DOOV.when(count(accountEmail().startsWith("a"),
                        accountEmail().startsWith("b")).greaterThan(2)).validate();
        assertThat(rule.getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(rule.getRootMetadata().type()).isEqualTo(BINARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0)).isInstanceOf(NaryMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).type()).isEqualTo(NARY_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(1).type()).isEqualTo(LEAF_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children()).hasSize(2);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(0).type()).isEqualTo(FIELD_PREDICATE);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(rule.getRootMetadata().children().get(0).children().get(1).type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts0 = ((LeafMetadata) rule.getRootMetadata().children().get(0).children().get(0)).stream().collect(toList());
        assertThat(elts0).hasSize(3);
        final List<Element> elts1 = ((LeafMetadata) rule.getRootMetadata().children().get(0).children().get(1)).stream().collect(toList());
        assertThat(elts1).hasSize(3);
        print(rule);
    }
}
