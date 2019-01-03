package com.domainevent.sample.domaineventsample;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
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
		AggregateExample agg = AggregateExample.builder().id(1L)
												.status("Newly Createdd")
												.build();
		repo.save(agg);
		//When the status is changed
		agg.changeStatus("Updated");
		assertEquals(1,agg.obtainDomainEventsSize());
		repo.save(agg);
		
		//Then domain events have been published
		assertEquals(0, agg.obtainDomainEventsSize());
		
	}
	
}

