Goto Cassandra in one hour
==========================

http://lesfurets.github.io/model-map/

Session description:
--------------------

This session will explain during a live coding session the journey taken by the french insurance comparison website LesFurets.com to migrate from MySQL to Cassandra.
The team has open sourced their framework that provides: domain model to key value mapping, Cassandra schema generator and CQL query builder on top of the Java driver.
Everything is relying on Java 8 streams and immutable data patterns, live since 6 months with 20 M of comparisons history.

The session will start by decorating a Java beans data model with annotations to obtain a complete key value mapping.
We will then store the key value model in Cassandra with a strongly typed schema using the tooling of the framework.
Finally we will query the produced data with Spark in plain old SQL at the end of the session.

update 18/06/01
---------------

This session will explain during a live coding session the journey taken by the french insurance comparison website LesFurets.com to migrate from MySQL to Cassandra.
The team use and maintain the dOOv OSS framework (http://doov.io) that provides: domain model to key value mapping, Cassandra schema generator and CQL query builder on top of the Java driver.
Everything is relying on Java 8 streams and immutable data patterns, live since 3 years with 30 M of comparisons history.

The session will start by decorating a Java beans data model with annotations to obtain a complete key value mapping.
We will then store the key value model in Cassandra with a strongly typed schema using the tooling of the framework.

fr_FR
-----

Pour ce meetup, nous allons vous présenter comment l'équipe des Furets.com a effectué la migration de l'essentiel de ses données depuis MySQL vers Cassandra.
La session commencera par une rapide présentation des différents datasets produits par les utilisateurs. Puis nous présenterons en live code l'utilsation du framework OSS dOOv (http://doov.io) qui permet d'effecter le mapping du modèle métier du site vers un modèle clé/valeur ainsi que le générateur de schema CQL et le query builder construit au dessus du Java driver de Cassandra.
L'essentiel des composants reposent sur l'utilisation de données immattables et des streams de Java 8. Ils fonctionnent depuis près de 3 ans en production et assurent le stockage de prêt de 30 M de comparaisons effectuéees sur le site.

Le live code commencera par la décoration d'un modèle de Java beans avec les annotations de dOOv pour obtenir le mapping clé/valeur complet. Ensuite, nous écrirons le stockage du modèle dans Cassandra avec un schema fortement typé, basé sur une série temporelle.

Session speakers:
-----------------

### Gilles Di Guglielmo
Gilles is a 15+ software developer working for various software vendors (ILOG, PrimaSolution, Courtanet) : graphic 2D librairy, rules engine, J2EE service platform, domain model code generation.
He is currently software architect for the insurance comparison web site LesFurets.com. He loves to feel the fresh air of San Francisco.

### Julien Baudry
Julien is a 7+ software developer working for software vendors (PrimaSolution, LesFurets.com). He is currently software architect for the insurance comparison web site LesFurets.com.
