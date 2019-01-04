package com.domainevent.sample.domain.events;

import lombok.*;

import java.time.Instant;

@Value
@Builder
public class StatusChangedEvent implements DomainEvent {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Instant occurredOn = Instant.now();

    private Long aggregateId;
    private String oldStatus;
    private String newStatus;


    @Override
    public Instant occurredOn() {
        return occurredOn;
    }
}
