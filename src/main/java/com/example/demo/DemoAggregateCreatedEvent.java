package com.example.demo;

public class DemoAggregateCreatedEvent {

    private final String newId;

    String title;

    public DemoAggregateCreatedEvent(String newId, String title) {
        this.newId = newId;
        this.title = title;
    }

    public String getNewId() {
        return newId;
    }

    public String getTitle() {
        return title;
    }
}