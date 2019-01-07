package com.domainevent.sample.domain;


import com.domainevent.sample.domain.events.StatusChangedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregateExample extends AbstractAggregateRoot {

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

}
