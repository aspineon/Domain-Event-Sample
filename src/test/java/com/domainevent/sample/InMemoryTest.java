package com.domainevent.sample;

import com.domainevent.sample.domain.AggregateExample;
import com.domainevent.sample.domain.events.StatusChangedEvent;
import com.domainevent.sample.domain.repositories.AggregateExampleRepository;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryTest {

    private AggregateExampleRepository repo =  MockRepositoryFactory.build();

    @Test
    public void shouldCreateADomainEventForChangingStatus() {
        //Given an aggregate
        AggregateExample agg = AggregateExample.builder().id(1L)
                .status("Newly Created")
                .build();


        //When the status is changed
        agg.changeStatus("Updated");

        //Then the aggregate state changes
        assertEquals("Updated", agg.getStatus());

        //And domain events have been registered
        assertEquals(1, agg.domainEvents().size());
        StatusChangedEvent event = (StatusChangedEvent) agg.domainEvents().get(0);
        assertEquals( "Updated", event.getNewStatus());
        assertEquals( "Newly Created", event.getOldStatus());
    }

    @Test
    public void shouldClearDomainEventsOnSave() {
        //Given an aggregate
        AggregateExample agg = AggregateExample.builder().id(1L)
                .status("Newly Created")
                .build();
        repo.save(agg);

        //When the status is changed
        agg.changeStatus("Updated");
        assertEquals(1, agg.domainEvents().size());
        repo.save(agg);

        //Then domain events have been published
        assertEquals(0, agg.domainEvents().size());
    }
}

