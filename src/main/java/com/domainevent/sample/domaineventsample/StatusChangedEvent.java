package com.domainevent.sample.domaineventsample;

import javax.persistence.Entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StatusChangedEvent {

	private Long aggregateId;
	private String oldStatus;
	private String newStatus;
		
}
