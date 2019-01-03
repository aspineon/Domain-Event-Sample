package com.domainevent.sample.domaineventsample;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AggregateExampleRepository extends CrudRepository<AggregateExample, Long>{

}
