# dOOv (Domain Object Oriented Validation)

[![Build Status](https://travis-ci.org/lesfurets/dOOv.svg?branch=master)](https://travis-ci.org/lesfurets/dOOv)

dOOv is a fluent API for typesafe domain model validation. It uses annotations, code generation and a type safe DSL to make domain model validation fast and easy.

![dOOv logo](docs/png/doov_io_logo_dark_small.png)

## Documentation

- **Wiki** [getting started](https://github.com/lesfurets/dOOv/wiki/Getting-Started)
- **Presentation** [latest presentation at Snowcamp 2018](http://doov.io/dsl_to_go_beyond_bean_validation_english.html)
- **Example** [the sample project in dOOv](sample)

## Usage

### Overview

Annotate your model with @Path annotations on field, qualifying them with field ids (see wiki section [Domain Model Annotation](https://github.com/lesfurets/dOOv/wiki/Domain-Model-Annotation))

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

Use the dOOv code genrator to generate a DSL with elements `userFirstName`, `userLastName` and `userBirthDate` (see wiki section [DSL Code Generation](https://github.com/lesfurets/dOOv/wiki/DSL-Code-Generation)). Then write your rules with entry point `DOOV#when` and terminal operation `ValidationRule#validate` (see wiki section [Validation Rules](https://github.com/lesfurets/dOOv/wiki/Validation-Rules)).

```java
ValidationRule rule = DOOV.when(userBirthdate().ageAt(today()).greaterOrEquals(18))
                          .validate();
```

You can create more complex rules by chaining `and` and `or` or by using matching methods from the `DOOV` class like `matchAny`, etc.

```java
DOOV.when(userBirthdate().ageAt(today()).greaterOrEquals(18)
     .and(userFullName().isNotNull()))
    .validate()
```

You can then execute the rule on an instantiated model (see wiki section [Validation Engine](https://github.com/lesfurets/dOOv/wiki/Validation-Engine)).

```java
// Execute the DSL on the model
DslModel model = new SampleModelWrapper(sampleModel);
Result result = rule.executeOn(model);
if (result.isFalse()) {
  ...
}
```

The result will return true or false depending on the result of the predicate, for example `Result#isTrue` means the predicate validated.

### Syntax tree

The rules provides an AST that can be printed as a human readable format with the `Readable#readable` method that is available on any DSL object. By default the output is from `AstLineVisitor` that outputs the string in plain text (see wiki section [Validation Engine](https://github.com/lesfurets/dOOv/wiki/Validation-Engine)).

```java
DOOV.when(userBirthdate().ageAt(today()).greaterOrEquals(18)).validate().readable()
> When user age at 'today' greater or equals '18', validate with empty message
```

### Testing

Assertions are available in the `doov-assertions` jar. It depends on AssertJ, so you can use the `assertThat` syntax (see wiki section [Testing Rules](https://github.com/lesfurets/dOOv/wiki/Testing-Rules)).

```java
ValidationRule rule = DOOV.when(userFirstName().isNotNull().or(userLastName().isNull())).validate();
assertThat(rule).validates(model).hasFailedNodeEmpty();
```

## Build

To build core, assertions, generator core, maven generator plugin and gradle generator plugin modules:

```bash
# Core
./gradlew build

# Sample modules with examples
./gradlew -p sample build
```

To deploy you need to configure the command line options for your repository:

```bash
./gradlew \
  -Psigning.secretKeyRingFile=secret-file.gpg \
  -Psigning.keyId=key-id \
  -Psigning.password=password \
  -PsnapshotRepository=http://www.acme.com/repository/snapshots \
  -Prepository=http://www.acme.com/repository/releases \
  -PossrhUsername=userName \
  -PossrhPassword=password \
  deploy
```

You can either specify `snapshotRepository` or `repository` depending on the version type.

To generate documentation with gradle:

```bash
# Generate documentation in docs/site/apidocs/subproject
./gradlew javadoc
```

## Release

To release the code, it will create 2 commits with proper tags and versions and push them:

```bash
./gradlew \
  -Psigning.secretKeyRingFile=secret-file.gpg \
  -Psigning.keyId=key-id \
  -Psigning.password=password \
  -PsnapshotRepository=http://www.acme.com/repository/snapshots \
  -Prepository=http://www.acme.com/repository/releases \
  -PossrhUsername=userName \
  -PossrhPassword=password \
  -Pversions.newVersion=RELEASE_VERSION \
  release
```

## Licence

[Apache-2.0](LICENSE)

