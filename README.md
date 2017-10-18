# dOOv (Domain Object Oriented Validation)

dOOv is a fluent API for typesafe domain model validation. It uses annotations, code generation and a type safe DSL to make domain model validation fast and easy.

## Installation

```xml
<dependency>
  <groupId>io.doov</groupId>
  <artifactId>doov-core</artifactId>
  <version>LATEST</version>
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

Use `mvn install` to generate code, then write your rules with entry point `DSL#when` and terminal operation `ValidationRule#validate`

```java
DSL.when(userBirthdate().ageAt(today()).greaterOrEquals(18L))
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

