package com.domainevent.sample.domain.repositories;

import com.domainevent.sample.domain.AggregateExample;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AggregateExampleRepository extends CrudRepository<AggregateExample, Long> {

}
