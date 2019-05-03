package com.example.demo;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class DemoAggregate {

    @AggregateIdentifier
    private String id;

    private String title;

    public DemoAggregate() {

    }


    @CommandHandler
    public DemoAggregate(CreateDemoAggregateCommand command) {
        AggregateLifecycle.apply(new DemoAggregateCreatedEvent(command.getId(), command.getTitle()));
    }

    @EventSourcingHandler
    public void in(DemoAggregateCreatedEvent event) {
        this.id = event.getNewId();
        this.title = event.getTitle();
    }

}
