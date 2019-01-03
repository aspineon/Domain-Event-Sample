package com.domainevent.sample.domaineventsample;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AggregateExampleController {

	@Autowired
	private AggregateExampleRepository repo;
	
	@PostMapping("/aggregates")
	AggregateExample newAggregateExample(@RequestBody AggregateExample aggregate) {
		return repo.save(aggregate);
	}
	
	@PutMapping("/aggregates/{id}") 
	AggregateExample updateAggregate(@RequestBody AggregateExample aggregate, @PathVariable Long id){
		AggregateExample agg = repo.findOne(id);
		agg.changeStatus(aggregate.getStatus());
		return repo.save(agg);
	}
	
	
}
