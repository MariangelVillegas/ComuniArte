package org.example.service;

import org.example.model.Donation;
import org.example.model.Event;
import org.example.model.Usuario;
import org.example.repositorio.AuthRepository;
import org.example.redis.RedisQueueManager;
import org.example.repositorio.EventRepository;

import java.util.List;

import static org.example.redis.RedisQueueManager.EVENTS_CHANNEL;
import static org.example.redis.RedisQueueManager.QUESTIONS_CHANNEL;

public class EventsService {

    private RedisQueueManager redisQueue;
    private EventRepository eventRepository;
    private AuthRepository authRepository;

    public EventsService() {
        redisQueue = new RedisQueueManager();
        eventRepository = new EventRepository();
        authRepository = new AuthRepository();
    }

    public Event startLive(String userId, String nombre) {
        redisQueue.publish(EVENTS_CHANNEL, "El usuario "+ nombre +" comenzó una transmisión en vivo");
        redisQueue.subscribe(QUESTIONS_CHANNEL);
        return eventRepository.save(userId, nombre);
    }

    public void addNewQuestion(Event event, String question) {
        eventRepository.addQuestion(event, question);
        redisQueue.publish(QUESTIONS_CHANNEL, "Un usuario realizó una pregunta");
    }

    public void subscribeEvents() {
        redisQueue.subscribe(EVENTS_CHANNEL);
    }

    public List<Event> getLives() {
        return eventRepository.getAll();
    }

    public List<String> getQuestions(Event event) {
        return eventRepository.findById(event.getUserId()).getQuestions();
    }

    public Usuario getLoggedUser() {
        return authRepository.getLoggedUser();
    }

    public void logOut() {
        authRepository.logoutUser();
    }

    public void addDonation(Event event, Double opt, String userDonorId) {
        eventRepository.addDonation(event, userDonorId, opt);
        redisQueue.publish(QUESTIONS_CHANNEL, "Recibiste una donación de " + userDonorId);
    }

    public List<Donation> getDonations(Event event) {
        return eventRepository.findById(event.getUserId()).getDonations();
    }

    public Event getLiveEvent(Usuario user) {
        return eventRepository.findById(user.get_id());
    }
}
