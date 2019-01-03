package com.domainevent.sample.domaineventsample;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.DomainEvents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregateExample extends AbstractAggregateRoot{
	
	private static final Logger LOG = LoggerFactory.getLogger(AggregateExample.class);
	
	@Id
	private Long id;
	private String status;
	
	public void changeStatus(String status) {
		String oldStatus = this.status;
		this.status = status;
		LOG.info("Status changed from " + oldStatus + " to " + status);
		this.registerEvent(StatusChangedEvent.builder().aggregateId(this.getId())
														.oldStatus(oldStatus)
														.newStatus(status)
														.build());
	}
	
	public int obtainDomainEventsSize() {
		return super.domainEvents().size();
	}
	
	
}
