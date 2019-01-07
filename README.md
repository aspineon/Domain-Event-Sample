# Domain-Event-Sample

1. Basic walkthrough of using @DomainEvents: https://www.baeldung.com/spring-data-ddd
	- Note that you do need to use spring data repositories when persisting the aggregate in order to have the domain event published. The article goes into the details. Associated code linked here: https://github.com/eugenp/tutorials/blob/master/persistence-modules/spring-data-jpa/src/main/java/com/baeldung/ddd/event/Aggregate3.java

2. Link on how to create a hibernate user type to support postgres jsonb data type: https://thoughts-on-java.org/persist-postgresqls-jsonb-data-type-hibernate/
	- In the code example I using a raw expression to insert the data in a database I created with two columns, an ID and a JSON val datatype.

3. Blog post/example using the @DomainEvents annotation for publishing: https://github.com/altfatterz/publishing-domain-events

4. Link on setting up a DB schema to support the JSON domain events: https://kalele.io/blog-posts/the-ideal-domain-driven-design-aggregate-store/
	- Came from Justin's article (https://medium.com/@justinmichaelholmes/creating-optionality-with-domain-event-logging-7eb8f1f422e7).

5. AbstractAggregateRoot docs: https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/AbstractAggregateRoot.html
	- Used this in the code example to make event publishing simpler.
