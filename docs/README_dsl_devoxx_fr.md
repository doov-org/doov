# DSL.using(java).toGoBeyond(BeanValidation).at(Devoxx.eq(FR));

Les API "fluent", comme les stream de Java 8, sont très pratiques parce qu'ils
sont clairs, concis, fortement typés et prennent maintenant avantage des
lambda. Et si on créait notre propre API fluent pour manipuler et valider notre
domaine métier ? Nous avons créé un framework open-source appelé dOOv, pour
"Domain Object Oriented Validation" (http://doov.io), qui génère un DSL de
validation à partir d'un domaine métier. Cette présentation va démontrer
l'efficacité et l'expressivité de dOOv pour définir des contraintes de
validation. Les règles de validation sont représentées par un arbre de syntaxe
abstraite, ce qui permet de parcourir l'arbre et de l'afficher en texte, en
markdown, ou en HTML. Nous allons comparer notre solution avec les standards de
l'industrie comme Bean Validation. Pendant la session, nous allons faire en
live code une migration de règle métier vers dOOv.

## Private message for committee

Website : http://doov.io
Github : https://github.com/lesfurets/dOOv
Slides : http://doov.io/dsl_to_go_beyond_bean_validation_softshake.html

Nous avons développé dOOv afin de migrer nos 500 règles de validation métier à
un format facile à écrire et à exporter en format lisible. La validation
d'objet métier est un problème récurrent et important dans notre travail, d'où
cette présentation et le projet open-source, qui pourrait en aider d'autres.
Nous cherchons récupérer du feedback sur dOOv, discuter de l'usage et des
améliorations futures.

## Status

SUBMITTED

