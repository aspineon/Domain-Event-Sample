package com.domainevent.sample;

import com.domainevent.sample.DomaineventsampleApplication;
import com.domainevent.sample.domain.AggregateExample;
import com.domainevent.sample.domain.repositories.AggregateExampleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

// This is an integration test per https://stackoverflow.com/questions/38398323/configuring-maven-failsafe-plugin-to-correctly-find-integration-tests
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DomaineventsampleApplication.class)
@ActiveProfiles("postgres")
public class PostgresIT {

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
        assertEquals(1, agg.obtainDomainEventsSize());
        repo.save(agg);

        //Then domain events have been published
        assertEquals(0, agg.obtainDomainEventsSize());
        // TODO assert that events are stored in db

    }
}
