package com.domainevent.sample;

import com.domainevent.sample.domain.AggregateExample;
import com.domainevent.sample.domain.events.AggregateCreatedEvent;
import com.domainevent.sample.domain.events.StatusChangedEvent;
import com.domainevent.sample.domain.repositories.AggregateExampleRepository;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryTest {

    private AggregateExampleRepository repo =  MockRepositoryFactory.build();
    
    @Test
    public void shouldCreateADomainEventForCreatingAnAggregate() {
    	//Given a status and ID
    	Long id = 1L;
    	String status = "new aggregate";
    	
    	//When the aggregate is created
    	AggregateExample newAgg = AggregateExample.createNewAggregate(id, status);
    	
    	//Then a created domain event has been registered
    	assertEquals(1, newAgg.domainEvents().size());
    	AggregateCreatedEvent event = (AggregateCreatedEvent) newAgg.domainEvents().get(0);
    	assertEquals("new aggregate", event.getStatus());
    	assertEquals(Long.valueOf(1L),event.getAggregateId());
    }

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
    public void shouldClearDomainEventsOnSaveForNewlyCreatedAggregate() {
        //Given an aggregate that is being created
    	AggregateExample agg = AggregateExample.createNewAggregate(1L, "new agg");
    	assertEquals(1, agg.domainEvents().size());
        

        //When the agg is saved
        repo.save(agg);

        //Then domain events have been published
        assertEquals(0, agg.domainEvents().size());
    }

    @Test
    public void shouldClearDomainEventsOnSaveForAlreadyCreatedAggregate() {
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

