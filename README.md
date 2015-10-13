DomainModel.stream()
======================

Session description:
--------------------

The perfect representation for data manipulation is based on collections, lists or maps but most applications are designed based on an object domain model. 

We solved this issue by implementing a KVM (Key Value Mapping) at the core level of our application. Data manipulation became very easy after implementing the bi-directional mapping of our entities to a key/value model. It took significant time before being able to leverage this new ecosystem and it started with a wide refactoring of the entities in order to fix our model key space. 

The main issues we faced were: 
* avoiding instance sharing (pointer equality is no longer available) 
* fixing the maximum size of the unbound relations 
* identifying the path from the root entities holder to every value 

We will explain: 
* the patterns to avoid 
* the mandatory refactorings 
* how we generated a key/value proxy API on top of our domain model (based on annotation processing and metadata). 

We implemented a wide set of new features on multiple tiers of our application and leveraged the power of stream API and RxJava. 

Here are a few examples: 

* mapping idempotence (consistency between Object entities and Key-Value model) 
* persistence in a column-based data store 
* diff between two instances of a domain model 
* incremental storage using CQRS, diff and Copy-On-Write approach of the model 
* optimized serialization using JSON dictionaries 
* easy mocking for testing 
* simplified MVC pattern for JS single page application 
* classifying datasets using Spark and MLLib directly on top of the domain model 


Session speakers:
-----------------

### Gilles Di Guglielmo
Gilles is a 15+ software developer working for various software vendors (ILOG, PrimaSolution, Courtanet) : graphic 2D librairy, rules engine, J2EE service platform, domain model code generation. He is currently software architect for the insurance comparison web site LesFurets.com. He loves to feel the fresh air of San Francisco and Anvers.

### Julien Baudry
Julien is a 7+ software developer working for software vendors (PrimaSolution, LesFurets.com). He is currently software architect for the insurance comparison web site LesFurets.com.
