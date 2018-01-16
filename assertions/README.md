# dOOv assertions

## Installation

See [latest version in maven central](https://repo1.maven.org/maven2/io/doov).

```xml
<dependency>
  <groupId>io.doov</groupId>
  <artifactId>doov-assertions</artifactId>
  <version>LATEST</version>
</dependency>
```

## Usage

Use ```Assertions#assertThat``` with either a ```ValidationRule```, a ```StepCondition``` or a ```Result```.

```java
public class Test {

    private ModelWrapper wrapper;

    @BeforeEach
    public void before() {
        Account account = new Account();
        account.setEmail("account@email.com");
        
        Model model = new Model();
        model.setAccount(account);
        
        wrapper = new ModelWrapper(account);
    }

    @Test
    public void test() {
        // ValidationRule
        ValidationRule rule = DOOV.when(accountEmail().isNotNull()).validate();
        assertThat(rule).validates(model).hasNoInvalidatedMetadata();

        // StepCondiiton
        StepCondiiton condition = accountEmail().isNotNull();
        assertThat(condition).validates(model).hasNoInvalidatedMetadata();        
        
        // Result
        Result result = DOOV.when(accountEmail().isNotNull()).validate().executeOn(model);        
        assertThat(result).isTrue();
    }

}
```
