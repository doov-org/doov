# DSL.using(java).toGoBeyond(BeanValidation).at(Riviera.eq(DEV));

Les API "fluent", comme les stream de Java 8, sont très puissants parce qu'ils sont clairs, concis, fortement typés et prennent maintenant avantage des lambda. Et si on créait notre propre API fluent pour manipuler et valider notre domaine métier ? Nous avons créé un framework open-source appelé dOOv, pour "Domain Object Oriented Validation" (http://doov.io), qui génère un DSL de validation à partir d'un domaine métier. Cette présentation va démontrer l'efficacité et l'expressivité de dOOv pour définir des contraintes de validation. Les règles de validation sont représentées par un arbre de syntaxe abstraite, ce qui permet de parcourir l'arbre et de l'afficher en texte, en markdown, ou en HTML. Nous allons comparer notre solution avec les standards de l'industrie comme Bean Validation. Pendant la session, nous allons faire en live code une migration de règle métier vers dOOv.

## Private message for committee

Quelques conférences récentes :

- Softshake 2017 
  - "DSL.using(java).toGoBeyond(BeanValidation).at(SoftShake.eq(ch));" : http://doov.io/dsl_to_go_beyond_bean_validation_english.html
  - "Apache Spark: Deep dive into the Java API for developers" : https://lesfurets.github.io/lesfurets-conferences/html/apache-spark-deep-dive-into-the-java-api-for-developers-softshake.html
- Devoxx Poland 2017 ("Apache Spark : Deep dive into the Java API for developers" : https://www.youtube.com/watch?v=cr3wCNwKQWY)
- BreizhCamp 2017 ("Apache Spark : Deep dive dans l'API Java pour développeur" : https://www.youtube.com/watch?v=oxRxMl4OSkM)
- Snowcamp 2017 ("Apache Spark : Deep dive dans l'API Java pour développeur")

## Status

SUBMITTED

