package com.domainevent.sample.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class BaseDomainEvent implements DomainEvent {

    // This will need to change if you choose rehydrate events from the database.
    // occurredOn will need to be set via a constructor
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Instant occurredOn = Instant.now();

    @Getter(onMethod = @__(@JsonIgnore))
    @Setter
    private Long id;

    @Override
    @JsonProperty("occurredOn")
    public Instant occurredOn() {
        return occurredOn;
    }
}
