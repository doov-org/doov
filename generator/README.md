# dOOv generator

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.doov/doov-generator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.doov/doov-generator)

## Installation

See [latest version in maven central](https://repo1.maven.org/maven2/io/doov).

```xml
<dependency>
  <groupId>io.doov</groupId>
  <artifactId>doov-generator</artifactId>
  <version>LATEST</version>
</dependency>
```

## Usage

See [doov-core](../assertions) to annotate your model and create ```ModelFieldId``` and ```Model```.

```xml
  <plugin>
    <groupId>io.doov</groupId>
    <artifactId>doov-generator</artifactId>
    <executions>
      <execution>
        <id>doov-generate-model</id>
        <goals>
          <goal>generate</goal>
        </goals>
        <configuration>
          <packageFilter>com.example.model</packageFilter>
          <fieldClass>com.example.domain.field.ModelFieldId</fieldClass>
          <sourceClass>com.example.domain.model.Model</sourceClass>
        </configuration>
      </execution>
    </executions>
  </plugin>
  
  <plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
      <generatedSourcesDirectory>src/generated/java</generatedSourcesDirectory>
    </configuration>
  </plugin>

  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-clean-plugin</artifactId>
    <configuration>
      <filesets>
        <fileset>
          <directory>${basedir}/src/generated</directory>
        </fileset>
      </filesets>
    </configuration>
  </plugin>

```

Then execute install on the project.

```bash
mvn clean install
```

The sources will be in `src/generated/java`
