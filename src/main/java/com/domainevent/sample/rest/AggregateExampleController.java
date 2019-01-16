package com.domainevent.sample.rest;

import com.domainevent.sample.domain.AggregateExample;
import com.domainevent.sample.domain.repositories.AggregateExampleRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AggregateExampleController {

	@Autowired
	private AggregateExampleRepository repo;
	
	@GetMapping("/aggregates")
	List<AggregateExample> all() {
		return repo.findAll();
	}
	
	@PostMapping("/aggregates")
	AggregateExample newAggregateExample(@RequestBody AggregateExample aggregate) {
		return repo.save(AggregateExample.createNewAggregate(aggregate.getId(), aggregate.getStatus()));
	}
	
	@PutMapping("/aggregates/{id}") 
	AggregateExample updateAggregate(@RequestBody AggregateExample aggregate, @PathVariable Long id){
		AggregateExample agg = repo.findById(id).get();
		agg.changeStatus(aggregate.getStatus());
		return repo.save(agg);
	}
	
	
}
