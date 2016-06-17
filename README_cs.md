Goto Cassandra in one hour
==========================

http://lesfurets.github.io/model-map/

Session description:
--------------------

This session will explain the path used by the french inssurance comparaison website LesFurets.com to migrate from MySQL to Cassandra.
The session will use a sample application and replicate the patterns during a rocking live code session the whole path to go to Cassandra.
The team as fully open source their own framework that provide domain model to key value mapping, Cassandra schema generator and CQL query builder on top of the Java driver.
Everything is relying on Java 8 streams and unmutable data patterns.

The session will start by decorating a java beans data model with annotations to obtain a complete key value mapping.
Storing the key value model in cassandra with a strongly typed schema using the tooling of the framework.
Finally we will query the produced data with Spark in plain old SQL at the end of the session, fully live coded.    

Session speakers:
-----------------

### Gilles Di Guglielmo
Gilles is a 15+ software developer working for various software vendors (ILOG, PrimaSolution, Courtanet) : graphic 2D librairy, rules engine, J2EE service platform, domain model code generation.
He is currently software architect for the insurance comparison web site LesFurets.com. He loves to feel the fresh air of San Francisco.

### Julien Baudry
Julien is a 7+ software developer working for software vendors (PrimaSolution, LesFurets.com). He is currently software architect for the insurance comparison web site LesFurets.com.
