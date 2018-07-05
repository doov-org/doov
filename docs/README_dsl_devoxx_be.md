# Conference Session :

# DSL.using(java).toGoBeyond(BeanValidation).at(Devoxx.eq(BE));

Fluent, stream-like API are great for writing type checked code, leveraging 
Java 8 functions and lambdas. But what about creating your own
fluent API to manipulate and validate your model? We created an open-source
framework called dOOv, for "Domain Object Oriented Validation"
(http://doov.io), that generates a validation DSL from a domain model. This
presentation will demonstrate the efficiency and expressiveness of dOOv to
define validation constraints. The validation rules are represented as an
abstract syntax tree, which makes it possible to visit the tree and show the
rule in text format, markdown, or HTML. We will compare our solution to
industry standards like Bean Validation. During the session, we will live code
legacy business rule migration to dOOv.

## Private message for committee

Website: http://doov.io
Github: https://github.com/lesfurets/dOOv
Slides: http://doov.io/dsl_to_go_beyond_bean_validation_ocode.html

We developed dOOv to migrate our 500 business validation rules to a format that
would be easy to write and to export in a human readable form. We open-sourced
our project and present it in conferences because domain object validation is a
recurrent and important problem in our work, so it might help others. We like
to get feedback on dOOv, discuss about usage and future improvements.

## Status

Deadline Friday, July 6, 2018

# Hands-On-Labs

# Getting Started with custom DSLs using the dOOv framework

Have you heard a lot about the advantages of DSLs but don't know where to 
start? In this session, we will be modernizing an e-Commerce application,
which has validation rules with BeanValidation and implements 
business logic and object-to-object mapping code with plain old Java. 
Using dOOv, attendees will generate a Java-based DSL and migrate incrementally 
the logic and mapping code of the application. First, attendees will use 
dOOv to rewrite BeanValidation rules and discover the benefits of a 
strongly-typed fluent API to express constraints of a model.
Then we will migrate the application logic and the mapping code to ensure 
the compliance and governance of the partner exchanges. Finally, we will 
evaluate the performance and flexibility gains.

## Private message for committee

Website: http://doov.io
Github: https://github.com/lesfurets/dOOv
Slides: http://doov.io/dsl_to_go_beyond_bean_validation_ocode.html

We developed dOOv to migrate our 500 business validation rules to a format that
would be easy to write and to export in a human readable form. We open-sourced
our project and present it in conferences because domain object validation is a
recurrent and important problem in our work, so it might help others. We like
to get feedback on dOOv, discuss about usage and future improvements.

## Status

Deadline Friday, July 6, 2018