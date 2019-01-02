package com.domainevent.sample.domaineventsample;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StatusChangedEvent {

	private String aggregateId;
	private String oldStatus;
	private String newStatus;
	
	
}
