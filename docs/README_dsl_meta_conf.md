# DSL.using(java).toGoBeyond(BeanValidation).at(MetaConf.eq(UK));

Fluent, stream-like API are great for writing type checked code, taking
advantage of Java 8 functions and lambdas. But what about creating your own
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
Slides: http://doov.io/dsl_to_go_beyond_bean_validation_english.html

We developed dOOv to migrate our 500 business validation rules to a format that
would be easy to write and to export in a human readable form. We open-sourced
our project and present it in conferences because domain object validation is a
recurrent and important problem in our work, so it might help others. We like
to get feedback on dOOv, discuss about usage and future improvements.

## Status

SUBMITTED

