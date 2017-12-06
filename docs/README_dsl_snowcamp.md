# DSL.using(java).toGoBeyond(BeanValidation).at(Snowcamp);

Fluent, stream-like API's are great for writing type checked code, taking
advantage of Java 8 functions and lambdas. Perhaps the best example of such
project is jOOQ, which creates a fluent Java DSL for SQL. But what about
creating your own DSL to manipulate and validate your model? We created an
open-source framework called dOOv (http://doov.io) for generating a validation
DSL from a domain model. This presentation will demonstrate the efficiency and
expressiveness of this framework to define validation constraints and to
generate a human readable and comprehensive rules catalog. We will also
refactor complex legacy business rules during a live code session.

## Private message for committee

We recently started using massively jOOQ in our codebase (see our Devoxx France
presentation on the subject https://www.youtube.com/watch?v=Gev7iQ5_VCA) and we
where impressed by the ease of use and expressiveness of the resulting code.
Our questioning was "Can we also create a DSL to simplify our business rules,
using Java 8 lambdas?"

## Status

ACCEPTED

