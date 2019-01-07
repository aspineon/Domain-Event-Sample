package com.domainevent.sample.domain;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is rewrite of https://github.com/spring-projects/spring-data-commons/blob/2.1.x/src/main/java/org/springframework/data/domain/AbstractAggregateRoot.java
 * <p>
 * The following changes have been made:
 * 1) make retrieving and clearing domain events public such that unit tests can be run light and fast (e.g. without spinning up a spring context).
 * 2) expose domain events as a list to allow clients to easily know the order in which events occurred
 *
 * To further understand the domain event functionality in Spring, see https://zoltanaltfatter.com/2017/06/09/publishing-domain-events-from-aggregate-roots/
 */
public class AbstractAggregateRoot<A extends AbstractAggregateRoot<A>> {

    private transient final @Transient
    List<Object> domainEvents = new ArrayList<>();

    /**
     * Registers the given event object for publication on a call to a Spring Data repository's save methods.
     *
     * @param event must not be {@literal null}.
     * @return the event that has been added.
     * @see #andEvent(Object)
     */
    protected <T> T registerEvent(T event) {

        Assert.notNull(event, "Domain event must not be null!");

        this.domainEvents.add(event);
        return event;
    }

    /**
     * Clears all domain events currently held. Usually invoked by the infrastructure in place in Spring Data
     * repositories.
     */
    @AfterDomainEventPublication
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    /**
     * All domain events currently captured by the aggregate.
     */
    @DomainEvents
    public List<Object> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Adds all events contained in the given aggregate to the current one.
     *
     * @param aggregate must not be {@literal null}.
     * @return the aggregate
     */
    @SuppressWarnings("unchecked")
    protected final A andEventsFrom(A aggregate) {

        Assert.notNull(aggregate, "Aggregate must not be null!");

        this.domainEvents.addAll(aggregate.domainEvents());

        return (A) this;
    }

    /**
     * Adds the given event to the aggregate for later publication when calling a Spring Data repository's save-method.
     * Does the same as {@link #registerEvent(Object)} but returns the aggregate instead of the event.
     *
     * @param event must not be {@literal null}.
     * @return the aggregate
     * @see #registerEvent(Object)
     */
    @SuppressWarnings("unchecked")
    protected final A andEvent(Object event) {

        registerEvent(event);

        return (A) this;
    }
}
