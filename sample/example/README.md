# dOOv samples

## Cassandra sample

To run `io.doov.sample.LiveCodeCassandraMeetup`, you should first
* install a Cassdandra database [download here](https://cassandra.apache.org/download/)
* start the server using `bin/cassandra`
* launch a CQL shell using `bin/cqlsh`
* create a keyspace using the CQL shell and executing `CREATE KEYSPACE meetup WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};`
