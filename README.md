# dOOv (Domain Object Oriented Validation)

[![Build Status](https://travis-ci.org/lesfurets/dOOv.svg?branch=master)](https://travis-ci.org/lesfurets/dOOv)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.doov/doov-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.doov/doov-core)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.doov/doov-generator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.doov/doov-generator)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.doov/doov-assertions/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.doov/doov-assertions)

dOOv is a fluent API for typesafe domain model validation. It uses annotations, code generation and a type safe DSL to make domain model validation fast and easy.

![dOOv logo](docs/png/doov_io_logo_dark_small.png)

## Documentation

### Modules

- **[doov-core](core)**: Contains the DSL, the AST and the annotations ([javadoc](http://doov.io/site/core/apidocs))
- **[doov-generator](generator)**: Contains the code generator for model-map and field infos ([javadoc](http://doov.io/site/generator/apidocs))
- **[doov-assertions](assertions)**: Contains AssertJ assertions ([javadoc](http://doov.io/site/assertions/apidocs))

### Presentations

See [latest presentation at Snowcamp 2018](http://doov.io/dsl_to_go_beyond_bean_validation_english.html)

### Example

See [the sample project in dOOv](sample)

## Usage overview

**For a more in-depth usage documentation, see the [documentation](#documentation)**

Annotate your model with @Path annotations on field, qualifying them with field ids.

```java
public class User {

    @SamplePath(field = SampleFieldId.FIRST_NAME, readable = "user first name")
    private String firstName;

    @SamplePath(field = SampleFieldId.LAST_NAME, readable = "user last name")
    private String lastName;

    @SamplePath(field = SampleFieldId.BIRTHDATE, readable = "user birthdate")
    private LocalDate birthDate;

}
```

Use `mvn install` to generate code, it will generate a companion class named `SampleFieldIdInfo` with all your keys. There is a field for each annotated element, and a method named from the `readable` attribute in the model annotation, on which you can use a static import.

Then write your rules with entry point `DOOV#when` and terminal operation `ValidationRule#validate`. You can see the `userBirthdate` method that is imported from the `SampleFieldIdInfo` class, and `today` that is imported from `LocalDateSuppliers`.

```java
ValidationRule rule = DOOV.when(userBirthdate().ageAt(today()).greaterOrEquals(18)).validate();
```

You can create more complex rules by chaining `and` and `or` or by using matching methods from the `DOOV` class like `matchAny`, etc.

```java
DOOV.when(userBirthdate().ageAt(today()).greaterOrEquals(18)
     .and(userFullName().isNotNull()))
    .validate()
```

You can then execute the rule on an instantiated model

```java
// Execute the DSL on the model
DslModel model = new SampleModelWrapper(sampleModel);
Result result = rule.executeOn(model);
if (result.isFalse()) {
  ...
}
```

The result will return true or false depending on the result of the predicate, for example `Result#isTrue` means the predicate validated.

## Syntax tree

The rules provides an AST that can be printed as a human readable format with the `Readable#readable` method that is available on any DSL object. By default the output is from `AstLineVisitor` that outputs the string in plain text.

```java
DOOV.when(userBirthdate().ageAt(today()).greaterOrEquals(18)).validate().readable()
> When user age at 'today' greater or equals '18', validate with empty message
```

## Testing

Assertions are available in the `doov-assertions` jar. It depends on AssertJ, so you can use the `assertThat` syntax.

```java
ValidationRule rule = DOOV.when(userFirstName().isNotNull().or(userLastName().isNull())).validate();
assertThat(rule).validates(model).hasFailedNodeEmpty();
```

## Releasing

We use a custom release script that will

- change the version to VERSION (in pom.xml, core/pom.xml, assertions/pom.xml, generator/pom.xml)
- release the parent pom (optional: signed with GPG)
- release core, assertions, generator (optional: signed with GPG)
- commit the changes in git and tag the commit to VERSION
- revert changes to VERSION
- push commit and tags

```bash
bin/release.sh VERSION REPOSITORY_ID REPOSITORY_URL [GPG_KEYNAME]
```

## Generating documentation

It is automatically generated and commited by the release script (so the available documentation is the latest released version).

It can also be generated for a snapshot version with the following command, the output is in `target/site`.

```bash
mvn -pl core clean site
mvn -pl assertions clean site
mvn -pl generator clean site
```

## Licence

[Apache-2.0](LICENSE)
