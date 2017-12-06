# dOOv (Domain Object Oriented Validation)

dOOv is a fluent API for typesafe domain model validation. It uses annotations, code generation and a type safe DSL to make domain model validation fast and easy.

## Installation

```xml
<dependency>
  <groupId>io.doov</groupId>
  <artifactId>doov-core</artifactId>
  <version>LATEST</version>
</dependency>
<dependency>
  <groupId>io.doov</groupId>
  <artifactId>doov-assertions</artifactId>
  <version>LATEST</version>
  <scope>test</scope>
</dependency>
```

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

Use `mvn install` to generate code, then write your rules with entry point `DOOV#when` and terminal operation `ValidationRule#validate`

```java
DOOV.when(userBirthdate().ageAt(today()).greaterOrEquals(18))
    .validate();
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

The rules provides an AST that can be printed as a human readable format with

```java
DOOV.when(userBirthdate().ageAt(today()).greaterOrEquals(18)).validate().readable()
> When user age at 'today' greater or equals '18', validate with empty message
```

## Syntax tree

You can print the syntax tree by calling (or creating your own) `MetadataVisitor`, such as `AstHtmlVisitor`. The method to call is `ValidationRule#accept(MetadataVisitor)`. The HTML renderer will output a tree of tokenized conditions.

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

## Testing

Assertions are available in the `doov-assertions` jar. It depends on AssertJ, so you can use the `assertThat` syntax.

```java
ValidationRule rule = DOOV.when(userFirstName().isNotNull().or(userLastName().isNull())).validate();
assertThat(rule).validates(model).hasFailedNodeEmpty();
```

## Licence

[Apache-2.0](LICENSE)

