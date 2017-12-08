# DSL.using(java).toGoBeyond(BeanValidation).at(MIX.eq(IT));

Fluent, stream-like API's are great for writing type checked code, taking
advantage of Java 8 functions and lambdas. But what about creating your own DSL
to manipulate and validate your model? We created an open-source framework
called dOOv (http://doov.io) that generates a validation DSL from a domain
model. This presentation will demonstrate the efficiency and expressiveness of
dOOv to define validation constraints and to generate a human readable and
comprehensive rules catalog. The validation rules are represented as an
abstract syntax tree and built-in visitors make it easy to print the rules in a
tree-like fashion. We will compare our solution to industry standards like Bean
Validation. During the session, we will live code legacy business rule
migration to dOOv.

## Recent conferences

Quelques conférences récentes :

- Softshake 2017 
  - "DSL.using(java).toGoBeyond(BeanValidation).at(SoftShake.eq(ch));" : http://doov.io/dsl_to_go_beyond_bean_validation_english.html
  - "Apache Spark: Deep dive into the Java API for developers" : https://lesfurets.github.io/lesfurets-conferences/html/apache-spark-deep-dive-into-the-java-api-for-developers-softshake.html
- Devoxx Poland 2017 ("Apache Spark : Deep dive into the Java API for developers" : https://www.youtube.com/watch?v=cr3wCNwKQWY)
- BreizhCamp 2017 ("Apache Spark : Deep dive dans l'API Java pour développeur" : https://www.youtube.com/watch?v=oxRxMl4OSkM)
- Snowcamp 2017 ("Apache Spark : Deep dive dans l'API Java pour développeur")

## Status

SENT

