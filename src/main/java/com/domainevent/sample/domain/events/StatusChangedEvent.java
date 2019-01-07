package com.domainevent.sample.domain.events;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StatusChangedEvent extends BaseDomainEvent {

    private Long aggregateId;
    private String oldStatus;
    private String newStatus;
}
