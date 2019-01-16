package com.domainevent.sample.domain.repositories;

import com.domainevent.sample.domain.AggregateExample;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AggregateExampleRepository extends JpaRepository<AggregateExample, Long> {

}
