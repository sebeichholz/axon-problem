package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.DomainEventStream;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EventHelper {

    @Autowired
    private EventStore eventStore;

    public Map<String, Map<String, Object>> getEventInformation(String aggregateId) {

        Map<String, Map<String, Object>> result = new TreeMap<>(Collections.reverseOrder());

        ObjectMapper mapper = new ObjectMapper();

        DomainEventStream domainEventStream = eventStore.readEvents(aggregateId);
        Iterator<? extends DomainEventMessage<?>> iterator = domainEventStream.asStream().iterator();
        while (iterator.hasNext()) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            DomainEventMessage<?> event = iterator.next();
            map.put("Identifier", event.getIdentifier());
            map.put("SequenceNumber", event.getSequenceNumber());

            Date myDate = Date.from(event.getTimestamp());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
            String formattedDate = formatter.format(myDate);

            try {
                map.put("Payload", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event.getPayload()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            try {
                map.put("MetaData", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event.getMetaData()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            result.put(formattedDate, map);
        }

        return result;

    }

    public boolean check(Map<String, Map<String, Object>> events, String eventId) {
        for(Map<String, Object> e: events.values()) {
            if (e.get("Identifier").equals(eventId)) return true;
        }
        return false;
    }
}
