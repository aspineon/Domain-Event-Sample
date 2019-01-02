package com.domainevent.sample.domaineventsample;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DomaineventsampleApplicationTests {
	
	@Autowired
	private AggregateExampleRepository repo;

	@Test
	public void contextLoads() {
		
	}
	
	@Test
	public void testDomainEvents() {
		//Given an aggregate
		AggregateExample agg = AggregateExample.builder().id(UUID.randomUUID().toString())
												.status("Newly Created")
												.build();
		
		//When the status is changed
		agg.changeStatus("Updated");
		assertEquals(1,agg.getDomainEventsSize());
		repo.save(agg);
		
		//Then domain events have been published
		assertEquals(0, agg.getDomainEventsSize());
		
	}

	@Test
	public void testDomainEvents2() {
		//Given an aggregate
		AggregateExample agg = AggregateExample.builder().id(UUID.randomUUID().toString())
												.status("Newly Created")
												.build();
		
		//When the status is changed
		agg.changeStatus("Updated");
		assertEquals(1,agg.getDomainEventsSize());
		repo.save(agg);
		
		//Then domain events have been published
		assertEquals(0, agg.getDomainEventsSize());
		
		//When the status is changed
		agg.changeStatus("Updated Again");
		assertEquals(1,agg.getDomainEventsSize());
		repo.save(agg);
				
		//Then domain events have been published
		assertEquals(0, agg.getDomainEventsSize());
		
	}
	
}

