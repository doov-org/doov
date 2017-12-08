# DSL.using(java).toGoBeyond(BeanValidation).at(Devoxx.eq(UK));

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

## Private message for committee

Website : http://doov.io
Github : https://github.com/lesfurets/dOOv
Slides : http://doov.io/dsl_to_go_beyond_bean_validation_english.html

We started developing dOOv as an internal library to migrate our 500 business
validation rules to a format that would be easy to write and to export in a
human readable form. We realized that we could go beyond existing libraries by
using a very different approach to validation, which is the usage of a DSL.  We
think the end result really fits with the direction Java code is now written
(fluent API, lambda, etc.), so we'd like to share dOOv and get feedback on the
syntax, the usage, and future improvement.

## Status

TO SEND

