package org.example.model;


import java.util.ArrayList;
import java.util.List;

public class Event {
    private String userId;
    private String name;
    private List<String> questions = new ArrayList<>();

    public Event(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public Event(String userId, String name, List<String> questions) {
        this.userId = userId;
        this.name = name;
        this.questions = questions;
    }

    public String getUserId() { return userId; }

    public List<String> getQuestions() {
        return questions;
    }

    public void addQuestion(String question) {
        this.questions.add(question);
    }

    public String getName() {
        return name;
    }
}

