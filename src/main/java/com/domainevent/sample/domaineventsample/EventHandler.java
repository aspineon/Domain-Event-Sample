package com.domainevent.sample.domaineventsample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EventHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(EventHandler.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private JdbcTemplate template;
	
	@TransactionalEventListener
	public void handleEvent(StatusChangedEvent e) {
		LOG.info("Id: " + e.getAggregateId() 
		+ "\nOldStatus: " + e.getOldStatus()
		+ "\nNewStatus: " + e.getNewStatus());
		
		try {
			LOG.info(objectMapper.writeValueAsString(e));
			template.update("INSERT INTO jsonevents(val) VALUES (to_json(?::json))", objectMapper.writeValueAsString(e));
			
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
