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

Session speakers:
-----------------

### Gilles Di Guglielmo
Gilles is a 15+ software developer working for various software vendors (ILOG, PrimaSolution, Courtanet) : graphic 2D librairy, rules engine, J2EE service platform, domain model code generation.
He is currently software architect for the insurance comparison web site LesFurets.com. He loves to feel the fresh air of San Francisco.

### Julien Baudry
Julien is a 7+ software developer working for software vendors (PrimaSolution, LesFurets.com). He is currently software architect for the insurance comparison web site LesFurets.com.
