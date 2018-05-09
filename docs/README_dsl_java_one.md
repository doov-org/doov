
# Conference Session :

# DSL.using(java).toGoBeyond(BeanValidation).at(Oracle.Code.One);

Fluent, stream-like API are great for writing type checked code, taking
advantage of Java 8 functions and lambdas. What about creating your own
fluent API to manipulate and validate your model? We created an framework
called dOOv, for "Domain Object Oriented Validation",
that generates a validation DSL from a domain model. This
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

SUBMITED

# BOF

# How to create a fluent API DSL with lambda builders? 

With the dOOv framework, we are trying to solve a common problem with software: 
performance vs. readability vs. type-safety. Since Java 8, lambdas offer the 
ability to compose functions at runtime with high performance. We built a 
fluent API using lambdas to write validation and mapping logic with a DSL. It 
enables the written application rules to be introspected and profiled by 
visiting the DSL abstract syntax tree at runtime. During this BOF we will 
discuss how the framework is designed, the issues that we encountered 
generating the natural language output, failure cause analysis, and AST 
rewriting.

## Status 

SUBMITED

# Hands-On-Labs

# Getting Started with custom DSLs using the dOOv framework

Have you heard a lot about the advantages of DSLs but don't know where to 
start? In this session, we will be modernizing an e-Commerce application,
which has validation rules with BeanValidation and implements 
business logic and mapping code with plain old Java. Using dOOv, 
attendees will generate a Java-based DSL and migrate incrementally the 
logic and mapping code of the application. First, attendees will use 
dOOv to rewrite BeanValidation rules and discover the benefits of a 
strongly-typed fluent API to express constraints of a model.
Then we will migrate the application logic and the mapping code to ensure 
the compliance and governance of the partner exchanges. Finally, we will 
evaluate the performance and flexibility gains.

## Status

SUBMITED
