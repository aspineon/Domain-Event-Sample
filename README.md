# Domain-Event-Sample

## Running in Minishift/Local Openshift Cluster (Need Admin Rights for certain commands):

#### 1. Get a running instance of postgres that includes required plugin for debezium:

```
oc new-app debezium/postgres -e POSTGRES_PASSWORD=mysecretpassword -e POSTGRES_USER=postgres
```

Related refences:
* https://hub.docker.com/r/debezium/postgres
* https://debezium.io/docs/connectors/postgresql/#output-plugin
* https://debezium.io/docs/connectors/postgresql/#amazon-rds

#### 2. Get the spring boot domain event sample app running in openshift:

```
mvn clean install -DskipITs
```
If you have docker running locally an instance of the postgres DB above, feel free to run the integration tests.

Navigate to *route*/swagger-ui.html to test things are working at this point. First, post an aggregate with a given id and status. Then, update it by using the put command with a new status. This fires off a domain event that you can view in the postgres DB (connect to the postgres container and view the records under the events table in the postgres database)

#### 3. Get the strimzi operator installed and running (must be completed as admin on cluster):

```
wget https://github.com/strimzi/strimzi/releases/download/0.9.0/strimzi-0.9.0.tar.gz
tar xzvf strimzi-0.9.0.tar.gz
cd strimzi-0.9.0
sed -i 's/namespace: .*/namespace: <openshift project name>/' install/cluster-operator/*RoleBinding*.yaml
oc apply -f install/cluster-operator -n <openshift project name>
```
Be sure that you are using the latest version of strimzi if you are running on minishift (https://github.com/strimzi/strimzi-kafka-operator/issues/1009).
Verify that the strimzi-cluster-operator is running using `oc get pods`.

#### 4. Deploy a basic kafka cluster (must be done as admin):

```
oc apply -f examples/templates/cluster-operator/persistent-template.yaml -n <openshift project name>
oc process strimzi-persistent \
    -p ZOOKEEPER_NODE_COUNT=1 \
    -p KAFKA_NODE_COUNT=1 \
    -p KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
    -p KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
    | oc apply -f - && \
    oc patch kafka my-cluster --type merge -p '{ "spec" : { "zookeeper" : { "resources" : { "limits" : { "memory" : "512Mi" }, "requests" : { "memory" : "512Mi" } } },  "kafka" : { "resources" : { "limits" : { "memory" : "1Gi" }, "requests" : { "memory" : "1Gi" } } } } }'
```
Verify that one kafka broker, one zookeeper node, and the entity operator are running using `oc get pods`.

#### 5. Setup Kafka Connect with Debezium Connect with Debezium Connectors to postgres

```
oc apply -f examples/templates/cluster-operator/connect-s2i-template.yaml -n <openshift project name>
```
Next step must be completed as admin:
```
oc process strimzi-connect-s2i \
    -p CLUSTER_NAME=debezium \
    -p KAFKA_CONNECT_CONFIG_STORAGE_REPLICATION_FACTOR=1 \
    -p KAFKA_CONNECT_OFFSET_STORAGE_REPLICATION_FACTOR=1 \
    -p KAFKA_CONNECT_STATUS_STORAGE_REPLICATION_FACTOR=1 \
    | oc apply -f -
```
```
export DEBEZIUM_VERSION=0.8.3.Final
mkdir -p plugins && cd plugins
curl http://central.maven.org/maven2/io/debezium/debezium-connector-postgres/$DEBEZIUM_VERSION/debezium-connector-postgres-$DEBEZIUM_VERSION-plugin.tar.gz | tar xz;
oc start-build debezium-connect --from-dir=. --follow
cd ..
```
Verify that the new debezium-connect pod has been deployed.

Now register the Debezium postgres connector (connect to a container running in order to use the kafka connect rest api).

```
oc exec -c kafka -i my-cluster-kafka-0 -- curl -s  -w "\n" -X POST \
    -H "Accept:application/json" \
    -H "Content-Type:application/json" \
    http://debezium-connect-api:8083/connectors -d @- <<'EOF'

{
    "name": "domainevent-connector",
    "config": {
        "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
        "database.hostname": "postgres",
        "database.port": "5432",
        "database.user": "postgres",
        "database.password": "mysecretpassword",
        "database.dbname": "postgres",
        "database.server.name": "domaineventdb",
        "table.whitelist": "public.events"
    }
}
EOF
```

#### 6. Consume the events published to Kafka using basic shell script.

This is a basic consumer that ships with Kafka:

```
oc exec -c zookeeper -it my-cluster-zookeeper-0 -- /opt/kafka/bin/kafka-console-consumer.sh \
   --bootstrap-server my-cluster-kafka-bootstrap:9092 \
   --from-beginning \
   --property print.key=true \
   --topic domaineventdb.public.events
```

You should now be able to see a stream of changes that have occurred to the events table. Continue to make updates to an aggregate and new changes will appear.

#### 7. Consume the events using Camel (https://github.com/haithamshahin333/Camel-Kafka-Consumer).

Clone the repo and run `mvn clean install -DskipTests` to get the app deployed to openshift

## References

1. Basic walkthrough of using @DomainEvents: https://www.baeldung.com/spring-data-ddd
	- Note that you do need to use spring data repositories when persisting the aggregate in order to have the domain event published. The article goes into the details. Associated code linked here: https://github.com/eugenp/tutorials/blob/master/persistence-modules/spring-data-jpa/src/main/java/com/baeldung/ddd/event/Aggregate3.java

2. Link on how to create a hibernate user type to support postgres jsonb data type: https://thoughts-on-java.org/persist-postgresqls-jsonb-data-type-hibernate/
	- In the code example I using a raw expression to insert the data in a database I created with two columns, an ID and a JSON val datatype.

3. Blog post/example using the @DomainEvents annotation for publishing: https://github.com/altfatterz/publishing-domain-events

4. Link on setting up a DB schema to support the JSON domain events: https://kalele.io/blog-posts/the-ideal-domain-driven-design-aggregate-store/
	- Came from Justin's article (https://medium.com/@justinmichaelholmes/creating-optionality-with-domain-event-logging-7eb8f1f422e7).

5. AbstractAggregateRoot docs: https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/AbstractAggregateRoot.html
	- Used this in the code example to make event publishing simpler.

6. Debezium Lab: https://github.com/debezium/microservices-lab/tree/master/voxxed-microservices-2018
