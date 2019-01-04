package com.domainevent.sample;

import com.domainevent.sample.domain.repositories.AggregateExampleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InMemoryTestApplication.class)
public class InMemoryTest {


    @Autowired
    private AggregateExampleRepository repo;


    @Test
    public void contextLoads() {

    }


}

