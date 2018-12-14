/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.i18n;

import static io.doov.core.dsl.DOOV.count;
import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.meta.MetadataType.*;
import static io.doov.core.dsl.time.TemporalAdjuster.firstDayOfMonth;
import static io.doov.sample.field.dsl.DslSampleModel.*;
import static io.doov.sample.validation.SampleRules.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.predicate.*;
import io.doov.core.dsl.time.LocalDateSuppliers;
import io.doov.sample.field.dsl.DslSampleModel;

public class SampleRuleI18nTest {

    private static Metadata predicate(ValidationRule rule) {
        return rule.metadata().children().findFirst().get().children().findFirst().get();
    }

    @Test
    public void test_RULE_EMAIL() {
        final Metadata predicate = predicate(RULE_EMAIL);
        assertThat(predicate).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(predicate.type()).isEqualTo(BINARY_PREDICATE);
        assertThat(predicate.children()).element(0).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(0).isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children()).element(1).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(1).isEqualTo(FIELD_PREDICATE);

        System.out.println(RULE_EMAIL);
    }

    @Test
    public void test_RULE_ACCOUNT() {
        final Metadata predicate = predicate(RULE_ACCOUNT);
        assertThat(predicate).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(predicate.type()).isEqualTo(NARY_PREDICATE);
        assertThat(predicate.children()).hasSize(3);
        assertThat(predicate.children()).element(0).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(0).isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children()).element(1).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(1).isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children()).element(2).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(2).isEqualTo(BINARY_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(2).children()).element(0)
                        .isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(2).children()).extracting(Metadata::type).element(0)
                        .isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(2).children()).element(1)
                        .isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(2).children()).extracting(Metadata::type).element(1)
                        .isEqualTo(FIELD_PREDICATE);

        System.out.println(RULE_ACCOUNT);
    }

    @Test
    public void test_RULE_ACCOUNT_2() {
        final Metadata predicate = predicate(RULE_ACCOUNT_2);

        assertThat(predicate).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(predicate.type()).isEqualTo(BINARY_PREDICATE);
        assertThat(predicate.children()).element(0).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(0).isEqualTo(BINARY_PREDICATE);
        assertThat(predicate.children()).element(1).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(1).isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children()).element(0)
                        .isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(predicate.children().collect(toList()).get(0).children()).extracting(Metadata::type).element(0)
                        .isEqualTo(BINARY_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children()).element(1)
                        .isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(0).children()).extracting(Metadata::type).element(1)
                        .isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children().collect(toList()).get(0).children())
                        .element(0).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(0).children().collect(toList()).get(0).children())
                        .extracting(Metadata::type).element(0).isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children().collect(toList()).get(0).children())
                        .element(1).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(0).children().collect(toList()).get(0).children())
                        .extracting(Metadata::type).element(1).isEqualTo(FIELD_PREDICATE);
        System.out.println(BINARY_PREDICATE);
    }

    @Test
    public void test_RULE_USER() {
        final Metadata predicate = predicate(RULE_USER);
        assertThat(predicate).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(predicate.type()).isEqualTo(BINARY_PREDICATE);
        assertThat(predicate.children()).element(0).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(0).isEqualTo(NARY_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children()).hasSize(2);
        assertThat(predicate.children()).element(1).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(1).isEqualTo(LEAF_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children()).element(0)
                        .isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(0).children()).extracting(Metadata::type).element(0)
                        .isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children()).element(1)
                        .isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(predicate.children().collect(toList()).get(0).children()).extracting(Metadata::type).element(1)
                        .isEqualTo(BINARY_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children().collect(toList()).get(1).children())
                        .element(0).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(0).children().collect(toList()).get(1).children())
                        .extracting(Metadata::type).element(0).isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children().collect(toList()).get(1).children())
                        .element(1).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(0).children().collect(toList()).get(1).children())
                        .extracting(Metadata::type).element(1).isEqualTo(FIELD_PREDICATE);
        System.out.println(RULE_USER);
    }

    @Test
    public void test_RULE_USER_2() {
        final Metadata predicate = predicate(RULE_USER_2);
        assertThat(predicate).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(predicate.type()).isEqualTo(BINARY_PREDICATE);
        assertThat(predicate.children()).element(0).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(0).isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children()).element(1).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(1).isEqualTo(BINARY_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(1).children()).element(0)
                        .isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(1).children()).extracting(Metadata::type).element(0)
                        .isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(1).children()).element(1)
                        .isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(predicate.children().collect(toList()).get(1).children()).extracting(Metadata::type).element(1)
                        .isEqualTo(BINARY_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(1).children().collect(toList()).get(1).children())
                        .element(0).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(predicate.children().collect(toList()).get(1).children().collect(toList()).get(1).children())
                        .extracting(Metadata::type).element(0).isEqualTo(NARY_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(1).children().collect(toList()).get(1).children())
                        .hasSize(2);
        assertThat(predicate.children().collect(toList()).get(1).children().collect(toList()).get(1).children()
                        .collect(toList()).get(0).children()).element(0).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(1).children().collect(toList()).get(1).children()
                        .collect(toList()).get(0).children()).extracting(Metadata::type).element(0)
                                        .isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(1).children().collect(toList()).get(1).children()
                        .collect(toList()).get(0).children()).element(1).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(1).children().collect(toList()).get(1).children()
                        .collect(toList()).get(0).children()).extracting(Metadata::type).element(1)
                                        .isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(1).children().collect(toList()).get(1).children())
                        .element(1).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(1).children().collect(toList()).get(1).children())
                        .extracting(Metadata::type).element(1).isEqualTo(LEAF_PREDICATE);
        System.out.println(RULE_USER_2);
    }

    @Test
    public void test_RULE_USER_ADULT() {
        final Metadata predicate = predicate(RULE_USER_ADULT);
        assertThat(predicate).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) predicate).elementsAsList();
        assertThat(elts).hasSize(5);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0)
                        .isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(2)
                        .isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(3)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(3).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(4).isEqualTo(ElementType.VALUE);
        System.out.println(RULE_USER_ADULT);
    }

    @Test
    public void test_RULE_USER_ADULT_FIRSTDAY() {
        final Metadata predicate = predicate(RULE_USER_ADULT_FIRSTDAY);
        assertThat(predicate).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) predicate).elementsAsList();
        assertThat(elts).hasSize(7);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0)
                        .isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(2)
                        .isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(3)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(4)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(5)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(3).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(4).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(5).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(6).isEqualTo(ElementType.VALUE);
        System.out.println(RULE_USER_ADULT_FIRSTDAY);
    }

    @Test
    public void test_RULE_FIRST_NAME() {
        final Metadata predicate = predicate(RULE_FIRST_NAME);
        assertThat(predicate).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(predicate.type()).isEqualTo(NARY_PREDICATE);
        assertThat(predicate.children()).hasSize(1);
        assertThat(predicate.children()).element(0).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(0).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) predicate.children().collect(toList()).get(0))
                        .elementsAsList();
        assertThat(elts).hasSize(5);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0)
                        .isEqualTo(StringFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1)
                        .isEqualTo(DefaultOperator.class);
        // assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(2).isEqualTo(LeafMetadata.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(3)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.UNKNOWN);
        assertThat(elts).extracting(Element::getType).element(3).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(4).isEqualTo(ElementType.VALUE);
        System.out.println(RULE_FIRST_NAME);
    }

    @Test
    public void test_RULE_ID() {
        final Metadata predicate = predicate(RULE_ID);
        assertThat(predicate).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) predicate).elementsAsList();
        assertThat(elts).hasSize(2);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0)
                        .isEqualTo(LongFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        System.out.println(RULE_ID);
    }

    @Test
    public void test_RULE_AGE_2() {
        final Metadata predicate = predicate(RULE_AGE_2);
        assertThat(predicate).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) predicate).elementsAsList();
        assertThat(elts).hasSize(6);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0)
                        .isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(2)
                        .isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(3)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(3).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(4).isEqualTo(ElementType.VALUE);
        assertThat(elts).extracting(Element::getType).element(5).isEqualTo(ElementType.TEMPORAL_UNIT);
        System.out.println(RULE_AGE_2);
    }

    @Test
    public void test_RULE_SUM() {
        final Metadata predicate = predicate(RULE_SUM);
        assertThat(predicate).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(predicate.type()).isEqualTo(BINARY_PREDICATE);
        assertThat(predicate.children()).element(0).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(0).isEqualTo(NARY_PREDICATE);
        assertThat(predicate.children()).element(1).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(1).isEqualTo(LEAF_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children()).hasSize(2);
        assertThat(predicate.children().collect(toList()).get(0).children().collect(toList()).get(0))
                        .isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(0).children()).extracting(Metadata::type).element(0)
                        .isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children()).element(1)
                        .isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(0).children()).extracting(Metadata::type).element(1)
                        .isEqualTo(FIELD_PREDICATE);
        final List<Element> elts0 = ((LeafMetadata) predicate.children().collect(toList()).get(0).children()
                        .collect(toList()).get(0)).elementsAsList();
        assertThat(elts0).hasSize(3);
        final List<Element> elts1 = ((LeafMetadata) predicate.children().collect(toList()).get(0).children()
                        .collect(toList()).get(1)).elementsAsList();
        assertThat(elts1).hasSize(3);
        System.out.println(RULE_SUM);
    }

    @Test
    public void test_RULE_MIN() {
        final Metadata predicate = predicate(RULE_MIN);
        assertThat(predicate).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(predicate.type()).isEqualTo(BINARY_PREDICATE);
        assertThat(predicate.children()).element(0).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(0).isEqualTo(NARY_PREDICATE);
        assertThat(predicate.children()).element(1).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(1).isEqualTo(LEAF_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children()).hasSize(2);
        assertThat(predicate.children().collect(toList()).get(0).children().collect(toList()).get(0))
                        .isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(0).children().collect(toList()).get(0).type())
                        .isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children().collect(toList()).get(1))
                        .isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(0).children().collect(toList()).get(1).type())
                        .isEqualTo(FIELD_PREDICATE);
        final List<Element> elts0 = ((LeafMetadata) predicate.children().collect(toList()).get(0).children()
                        .collect(toList()).get(0)).elementsAsList();
        assertThat(elts0).hasSize(1);
        final List<Element> elts1 = ((LeafMetadata) predicate.children().collect(toList()).get(0).children()
                        .collect(toList()).get(1)).elementsAsList();
        assertThat(elts1).hasSize(1);
        System.out.println(RULE_MIN);
    }

    @Test
    public void test_RULE_DOUBLE_TEMPORAL() {
        final ValidationRule rule = DOOV.when(userBirthdate.with(firstDayOfMonth())
                        .ageAt(accountCreationDate.with(firstDayOfMonth())).lesserThan(18)).validate();
        final Metadata predicate = predicate(rule);
        assertThat(predicate).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) predicate).elementsAsList();
        assertThat(elts).hasSize(9);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0)
                        .isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(2)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(3)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(4)
                        .isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(5)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(6)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(7)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(3).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(4).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(5).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(6).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(7).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(8).isEqualTo(ElementType.VALUE);
        System.out.println(rule);
    }

    @Test
    public void test_match_any() {
        final ValidationRule rule = DOOV.when(matchAny(accountEmail.startsWith("a"), accountEmail.startsWith("b")))
                        .validate();
        final Metadata predicate = predicate(rule);
        assertThat(predicate).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(predicate.type()).isEqualTo(NARY_PREDICATE);
        assertThat(predicate.children()).element(0).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(0).isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children()).element(1).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(1).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts0 = ((LeafMetadata) predicate.children().collect(toList()).get(0))
                        .elementsAsList();
        assertThat(elts0).hasSize(3);
        final List<Element> elts1 = ((LeafMetadata) predicate.children().collect(toList()).get(1))
                        .elementsAsList();
        assertThat(elts1).hasSize(3);
        System.out.println(rule);
    }

    @Test
    public void test_count_greater_than_2() {
        final ValidationRule rule = DOOV
                        .when(count(accountEmail.startsWith("a"), accountEmail.startsWith("b")).greaterThan(2))
                        .validate();
        final Metadata predicate = predicate(rule);
        assertThat(predicate).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(predicate.type()).isEqualTo(BINARY_PREDICATE);
        assertThat(predicate.children()).element(0).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(0).isEqualTo(NARY_PREDICATE);
        assertThat(predicate.children()).element(1).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children()).extracting(Metadata::type).element(1).isEqualTo(LEAF_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children()).hasSize(2);
        assertThat(predicate.children().collect(toList()).get(0).children()).element(0)
                        .isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(0).children()).extracting(Metadata::type).element(0)
                        .isEqualTo(FIELD_PREDICATE);
        assertThat(predicate.children().collect(toList()).get(0).children()).element(1)
                        .isInstanceOf(LeafMetadata.class);
        assertThat(predicate.children().collect(toList()).get(0).children().collect(toList()).get(1).type())
                        .isEqualTo(FIELD_PREDICATE);
        final List<Element> elts0 = ((LeafMetadata) predicate.children().collect(toList()).get(0).children()
                        .collect(toList()).get(0)).elementsAsList();
        assertThat(elts0).hasSize(3);
        final List<Element> elts1 = ((LeafMetadata) predicate.children().collect(toList()).get(0).children()
                        .collect(toList()).get(1)).elementsAsList();
        assertThat(elts1).hasSize(3);
        System.out.println(rule);
    }

    @Test
    public void test_combined_when() {
        final ValidationRule rule = DOOV
                        .when(configurationMinAge.when(configurationMaxEmailSize.anyMatch(11, 12, 13)).eq(1))
                        .validate();
        final Metadata predicate = predicate(rule);
        assertThat(predicate).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) predicate).elementsAsList();
        assertThat(elts).hasSize(9);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0)
                        .isEqualTo(IntegerFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(3)
                        .isEqualTo(IntegerFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(4)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(7)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.PARENTHESIS_LEFT);
        assertThat(elts).extracting(Element::getType).element(3).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(4).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(5).isEqualTo(ElementType.VALUE);
        assertThat(elts).extracting(Element::getType).element(6).isEqualTo(ElementType.PARENTHESIS_RIGHT);
        assertThat(elts).extracting(Element::getType).element(7).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(8).isEqualTo(ElementType.VALUE);
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(0)
                        .isEqualTo("configuration.min.age");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(1).isEqualTo("when");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(2).isEqualTo("(");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(3)
                        .isEqualTo("configuration.max.email.size");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(4)
                        .isEqualTo("match any");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(5)
                        .isEqualTo(" : 11, 12, 13");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(6).isEqualTo(")");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(7).isEqualTo("=");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(8).isEqualTo("1");
        System.out.println(rule);
    }

    @Test
    public void test_temporal_unit() {
        final ValidationRule rule = DOOV
                        .when(DslSampleModel.userBirthdate.minus(1, YEARS).before(LocalDateSuppliers.today()))
                        .validate();
        final Metadata predicate = predicate(rule);
        assertThat(predicate).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) predicate).elementsAsList();
        assertThat(elts).hasSize(6);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0)
                        .isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(4)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(5)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.VALUE);
        assertThat(elts).extracting(Element::getType).element(3).isEqualTo(ElementType.TEMPORAL_UNIT);
        assertThat(elts).extracting(Element::getType).element(4).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(5).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(0)
                        .isEqualTo("user.birthdate");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(1).isEqualTo("minus");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(2).isEqualTo("1");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(3).isEqualTo("years");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(4).isEqualTo("before");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(5).isEqualTo("today");
        System.out.println(rule);
    }

    @Test
    public void test_eq_today() {
        final ValidationRule rule = DOOV.when(DslSampleModel.userBirthdate.eq(LocalDateSuppliers.today())).validate();
        final Metadata predicate = predicate(rule);
        assertThat(predicate).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) predicate).elementsAsList();
        assertThat(elts).hasSize(3);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0)
                        .isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(0)
                        .isEqualTo("user.birthdate");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(1).isEqualTo("=");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(2).isEqualTo("today");
        System.out.println(rule);
    }

    @Test
    public void test_eq_today_plus_1_day() {
        final ValidationRule rule = DOOV.when(DslSampleModel.userBirthdate.eq(LocalDateSuppliers.today().plus(1, DAYS)))
                        .validate();
        final Metadata predicate = predicate(rule);
        assertThat(predicate).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.type()).isEqualTo(FIELD_PREDICATE);
        final List<Element> elts = ((LeafMetadata) predicate).elementsAsList();
        assertThat(elts).hasSize(6);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0)
                        .isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(3).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(4).isEqualTo(ElementType.VALUE);
        assertThat(elts).extracting(Element::getType).element(5).isEqualTo(ElementType.TEMPORAL_UNIT);
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(0)
                        .isEqualTo("user.birthdate");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(1).isEqualTo("=");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(2).isEqualTo("today");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(3).isEqualTo("plus");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(4).isEqualTo("1");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(5).isEqualTo("days");
        System.out.println(rule);
    }

    @Test
    void test_age_at_today() {
        final Metadata predicate = predicate(RULE_AGE);
        assertThat(predicate).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.type()).isEqualTo(FIELD_PREDICATE);
        List<Element> elts = ((LeafMetadata) predicate).elementsAsList();
        assertThat(elts).hasSize(5);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0)
                        .isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(3).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(4).isEqualTo(ElementType.VALUE);
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(0)
                        .isEqualTo("user.birthdate");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(1).isEqualTo("age at");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(2).isEqualTo("today");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(3).isEqualTo(">=");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(4).isEqualTo("18");
        System.out.println(RULE_AGE);
    }

    @Test
    void test_double_lambda() {
        final Metadata predicate = predicate(RULE_DOUBLE_LAMBDA);
        assertThat(predicate).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.type()).isEqualTo(FIELD_PREDICATE_MATCH_ANY);
        List<Element> elts = ((LeafMetadata) predicate).elementsAsList();
        assertThat(elts).hasSize(3);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0)
                        .isEqualTo(StringFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.VALUE);
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(0)
                        .isEqualTo("favorite.site.name.1");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(1)
                        .isEqualTo("match any");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(2)
                        .isEqualTo("-function-");
        System.out.println(RULE_DOUBLE_LAMBDA);
    }

    @Test
    void test_map_to_int() {
        final Metadata predicate = predicate(RULE_BORN_1980);
        assertThat(predicate).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.type()).isEqualTo(FIELD_PREDICATE);
        List<Element> elts = ((LeafMetadata) predicate).elementsAsList();
        assertThat(elts).hasSize(5);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0)
                        .isEqualTo(LocalDateFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.UNKNOWN);
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(0)
                        .isEqualTo("user.birthdate");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(1)
                        .isEqualTo("as a number");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(2)
                        .isEqualTo("-function- ");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(3).isEqualTo("=");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(4).isEqualTo("1980");
        System.out.println(RULE_BORN_1980);
    }

    @Test
    void test_map_to_string() {
        final Metadata predicate = predicate(RULE_ACCOUNT_TIME_CONTAINS);
        assertThat(predicate).isInstanceOf(LeafMetadata.class);
        assertThat(predicate.type()).isEqualTo(FIELD_PREDICATE);
        List<Element> elts = ((LeafMetadata) predicate).elementsAsList();
        assertThat(elts).hasSize(5);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(0)
                        .isEqualTo(EnumFieldInfo.class);
        assertThat(elts).extracting(Element::getReadable).extracting(Object::getClass).element(1)
                        .isEqualTo(DefaultOperator.class);
        assertThat(elts).extracting(Element::getType).element(0).isEqualTo(ElementType.FIELD);
        assertThat(elts).extracting(Element::getType).element(1).isEqualTo(ElementType.OPERATOR);
        assertThat(elts).extracting(Element::getType).element(2).isEqualTo(ElementType.UNKNOWN);
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(0)
                        .isEqualTo("account.timezone");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(1)
                        .isEqualTo("as a string");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(2)
                        .isEqualTo("-function- ");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(3)
                        .isEqualTo("contains");
        assertThat(elts).extracting(Element::getReadable).extracting(Readable::readable).element(4).isEqualTo("00:00");
        System.out.println(RULE_ACCOUNT_TIME_CONTAINS);
    }
}
