package com.domainevent.sample.domain.events;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AggregateCreatedEvent extends BaseDomainEvent{
	
    private Long aggregateId;
    private String status;
}
