package com.domainevent.sample;

import com.domainevent.sample.domain.AbstractAggregateRoot;
import com.domainevent.sample.domain.repositories.AggregateExampleRepository;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MockRepositoryFactory {

    // TODO can this be generized to any type of repository
    public static AggregateExampleRepository build() {
        AggregateExampleRepository repo = Mockito.mock(AggregateExampleRepository.class);

        // TODO look into a class matcher here to make sure argument extends
        //  the custom base class, not just the spring data base class
        //  this may lead to runtime errors, but that's ok in tests
        when(repo.save(any())).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                AbstractAggregateRoot aggregate = (AbstractAggregateRoot) invocation.getArguments()[0];
                aggregate.clearDomainEvents();
                return aggregate;
            }
        });
        return repo;
    }
}
