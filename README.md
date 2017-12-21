# dOOv (Domain Object Oriented Validation)

[![Build Status](https://travis-ci.org/lesfurets/dOOv.svg?branch=master)](https://travis-ci.org/lesfurets/dOOv)

dOOv is a fluent API for typesafe domain model validation. It uses annotations, code generation and a type safe DSL to make domain model validation fast and easy.

## Installation

See [latest version in maven central](https://repo1.maven.org/maven2/io/doov).

```xml
<!-- Annotations, field info, step conditions, DSL, etc. -->
<dependency>
  <groupId>io.doov</groupId>
  <artifactId>doov-core</artifactId>
  <version>LATEST</version>
</dependency>

<!-- Code generator -->
<dependency>
  <groupId>io.doov</groupId>
  <artifactId>doov-generator</artifactId>
  <version>LATEST</version>
</dependency>

<!-- Assertj assertions -->
<dependency>
  <groupId>io.doov</groupId>
  <artifactId>doov-assertions</artifactId>
  <version>LATEST</version>
  <scope>test</scope>
</dependency>
```

## Documentation

See [lastest javadoc](http://doov.io/site/apidocs).

## Usage

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

Use `mvn install` to generate code, it will generate a companion class named `SampleFieldIdInfo` with all your keys.

```java
@Generated(value = "io.doov.gen.ModelMapGenMojo", 
           comments = "generated from io.doov.sample.field.SampleFieldId")
public final class SampleFieldIdInfo extends FieldInfoProvider {

    public static final LocalDateFieldInfo BIRTHDATE = localDateField()
                    .fieldId(SampleFieldId.BIRTHDATE)
                    .readable("user birthdate")
                    .type(LocalDate.class)
                    .build(ALL);

    public static LocalDateFieldInfo userBirthdate() {
        return BIRTHDATE;
    }

```

There is a field for each annotated element, and a method named from the `readable` attribute in the model annotation, on which you can use a static import.

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
User user = new User();
user.setFirstName("first name");

// SampleModel is a generated class
SampleModel sampleModel = new SampleModel();
sampleModel.setUser(user);

// Execute the DSL on the model
DslModel model = new SampleModelWrapper(sampleModel);
Result result = rule.executeOn(model);
if (result.isFalse()) {
  ...
}
```

The result will return true or false depending on the result of the predicate, for example `Result#isTrue` means the predicate validated.

## Registry

You can use a registry to keep your rules organized without keeping actual references to them.

```java
DOOV.when(userBirthdate().ageAt(today()).greaterOrEquals(18))
    .validate()
    .registerOn(REGISTRY_DEFAULT);
```

Validate them by streaming the rules of a registry and calling `ValidationRule#executeOn` with your model instance as an argument.

```java
REGISTRY_DEFAULT.stream()
    .map(rule -> rule.executeOn(model))
    .filter(Result::isInvalid)
    .map(Result::message)
    .collect(toList());
```

## Syntax tree

The rules provides an AST that can be printed as a human readable format with the `Readable#readable` method that is available on any DSL object. By default the output is from `AstLineVisitor` that outputs the string in plain text.

```java
DOOV.when(userBirthdate().ageAt(today()).greaterOrEquals(18)).validate().readable()
> When user age at 'today' greater or equals '18', validate with empty message
```

You can print the syntax tree by calling (or creating your own) `MetadataVisitor`, such as `AstHtmlVisitor`. The method to call is `ValidationRule#accept(MetadataVisitor)`. For example, the HTML renderer will output a tree of tokenized conditions.

```html
<div class='dsl-validation-rule'>
<ul>
<li><span class='dsl-token-rule'>Rule</span>
  <ul>
  <li><span class='dsl-token-when'>When</span></li>
    <ul>
    <li><span class='dsl-token-field'>user birthdate</span> <span class='dsl-token-operator'>age at</span> <span class='dsl-token-value'>today </span> <span class='dsl-token-operator'>greater or equals</span> <span class='dsl-token-value'>18</span></li>
  </ul>
<li>validate with message <span class='dsl-validation-message'>empty</span></li></ul>
</ul>
</div>
```

## Performance and evaluation

Predicate evaluation in dOOv is very similar to Java's lambda predicate evaluation. It differs from bare metal `or` / `and` since there is no operator precedence in dOOv, only tree evaluation order precedence. In general, you can consider:

- predicates evaluates dept first, left to right (that means multiple chained and / or predicates might not evaluate the same way as bare metal Java)
- predicates short-circuits the same way as Java logical operators and streams do (but that can be deactivated)

If you want to deactivate short-circuiting, you can do it with `ValidationRule#withShortCircuit` and use `false` (it is `true` by default). It will impact performance, but it is useful if you want complete runtime metrics ("what nodes trigger on my production server", etc.).

## Testing

Assertions are available in the `doov-assertions` jar. It depends on AssertJ, so you can use the `assertThat` syntax.

```java
ValidationRule rule = DOOV.when(userFirstName().isNotNull().or(userLastName().isNull())).validate();
assertThat(rule).validates(model).hasFailedNodeEmpty();
```

## Releasing

We use a custom release script that will

- change the version to VERSION (in pom.xml, core/pom.xml, assertions/pom.xml, generator/pom.xml)
- release the parent pom (signed with GPG)
- release core, assertions, generator (signed with GPG)
- commit the changes in git and tag the commit to VERSION
- revert changes to VERSION
- push commit and tags

```bash
bin/release.sh VERSION REPOSITORY_ID REPOSITORY_URL GPG_KEYNAME
```

## Generating documentation (maven site)

It is automatically generated and commited by the release script (so the available documentation is the latest released version).

It can also be generated for a snapshot version with the following command, the output is in target/apidocs.

```bash
mvn -pl core/core clean site
```

## Licence

[Apache-2.0](LICENSE)

