package com.example.demo;

public class CreateDemoAggregateCommand {

    private final String newId;

    String title;

    public CreateDemoAggregateCommand(String newId, String title) {
        this.newId = newId;
        this.title = title;
    }

    public String getId() {
        return newId;
    }

    public String getTitle() {
        return title;
    }

}
