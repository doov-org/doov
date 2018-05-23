# dOOv core

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.doov/doov-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.doov/doov-core)

## Installation

See [latest version in maven central](https://repo1.maven.org/maven2/io/doov).

```xml
<dependency>
  <groupId>io.doov</groupId>
  <artifactId>doov-core</artifactId>
  <version>LATEST</version>
</dependency>
```

## Usage

### Model keying

Create your path constraint, path annotation and field id with one key ```EMAIL```.

```java
package com.example.domain.field;

public enum ModelConstraint implements PathConstraint { NONE; }
```

```java
package com.example.domain.field;

public enum ModelFieldId implements FieldId { EMAIL }
```

```java
package com.example.domain.field;

@Path
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelPath {

    ModelFieldId field();

    ModelConstraint constraint() default NONE;

    String readable() default "";

}
```

Then annotate your model with the field id ```EMAIL```.

```java
package com.example.domain.model;

public class Model {
    
    private Acccount account;
    
    public Acccount getAcccount() {
        return account;
    }

    public void setAcccount(Acccount account) {
        this.account = account;
    }
    
}
```

```java
package com.example.domain.model;

public class Account {

    @ModelPath(field = ModelFieldId.EMAIL, readable = "account email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
```

### Code generation

See [doov-generator](../generator) to generate ```ModelFieldIdInfo``` (from ```ModelFieldId```) and ```ModelWrapper``` (from ```Model```).

### Write validation rules

The generated class ```ModelFieldIdInfo``` contains the field to use. You can add your rule to the ```DefaultRuleRegistry```.

```java
package com.example.domain.validation;

public class Validation {
    
    ValidationRule email = DOOV.when(accountEmail().matches("...")).validate().registerOn(REGISTRY_DEFAULT);
    
}
```

### Execute rules

You need to wrap you model, then execute the rules on it. You can use the ```DefaultRuleRegistry```.

```java
package com.example.domain.validation;

public class Execute {
    
    public static void main(String... args) {
        Account account = new Account();
        account.setEmail("account@email.com");
        
        Model model = new Model();
        model.setAccount(account);
        
        ModelWrapper wrapper = new ModelWrapper(account);
        
        List<Result> failed = REGISTRY_DEFAULT.stream()
                        .map(rule -> rule.executeOn(wrapper))
                        .filter(result -> result.isFalse())
                        .collect(Collectors.toList());
    }

}
```

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
