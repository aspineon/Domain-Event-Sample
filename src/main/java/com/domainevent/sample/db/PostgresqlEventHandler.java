package com.domainevent.sample.db;

import com.domainevent.sample.domain.events.DomainEvent;
import com.domainevent.sample.domain.events.StatusChangedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;


@Profile("postgres")
@Service
public class PostgresqlEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresqlEventHandler.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate template;

    @TransactionalEventListener
    public void handleEvent(DomainEvent e) {
        try {
            LOG.info(objectMapper.writeValueAsString(e));
            template.update("INSERT INTO events(data) VALUES (to_json(?::json))", objectMapper.writeValueAsString(e));

        } catch (JsonProcessingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

}
