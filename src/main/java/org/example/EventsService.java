package org.example;

import org.example.model.Event;
import org.example.model.Usuario;
import org.example.redis.RedisQueueManager;
import org.example.redis.EventRepository;

import java.util.List;

import static org.example.redis.RedisQueueManager.EVENTS_CHANNEL;
import static org.example.redis.RedisQueueManager.QUESTIONS_CHANNEL;

public class EventsService {

    private RedisQueueManager redisQueue;
    private EventRepository eventRepository;

    public EventsService() {
        redisQueue = new RedisQueueManager();
        eventRepository = new EventRepository();
    }

    public Event startLive(String userId, String nombre) {
        redisQueue.publish(EVENTS_CHANNEL, "El usuario "+ nombre +" comenzó una transmisión en vivo");
        redisQueue.subscribe(QUESTIONS_CHANNEL);
        return eventRepository.save(userId, nombre);
    }

    public void showQuestions() {
        redisQueue.getQuestions().forEach(System.out::println);
    }

    public void addNewQuestion(String eventId, String question) {
        eventRepository.addQuestion(eventId, question);
    }

    public void subscribeEvents() {
        redisQueue.subscribe(EVENTS_CHANNEL);
    }

    public Usuario saveLoggedUser(String userId, String name) {
        return eventRepository.loginUser(userId, name);
    }

    public List<Event> getLives() {
        return eventRepository.getAll();
    }
}
