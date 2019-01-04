package com.domainevent.sample.domain.events;

import java.time.Instant;

public interface DomainEvent {

    Instant occurredOn();

}
