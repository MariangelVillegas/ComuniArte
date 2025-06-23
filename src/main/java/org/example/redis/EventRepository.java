package org.example.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Donation;
import org.example.model.Event;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EventRepository {

    private static final String REDIS_HOST = "localhost";
    private static final int PORT = 6379;

    public Event save(String userId, String name) {
        try (Jedis jedis = new Jedis("LOCALHOST", PORT)) {

            String key = "event:" + userId;

            jedis.hset(key, "userId", userId);
            jedis.hset(key, "userName", name);
            jedis.lpush("event:ids", userId);

            return new Event(userId, name);
        }
    }

    public List<Event> getAll() {
        List<Event> events = new ArrayList<>();

        try (Jedis jedis = new Jedis(REDIS_HOST, PORT)) {
            List<String> ids = jedis.lrange("event:ids", 0, -1);

            for (String id : ids) {
                String key = "event:" + id;
                Map<String, String> data = jedis.hgetAll(key);

                if (!data.isEmpty()) {
                    String userId = data.get("userId");
                    String userName = data.get("userName");
                    List<String> questions = jedis.lrange("event:" + id + ":questions", 0, -1);
                    List<Donation> donations = getDonations(jedis.lrange("event:" + id + ":donations", 0, -1));
                    events.add(new Event(userId, userName, questions, donations));
                }
            }
        }

        return events;
    }

    public void addQuestion(Event event, String question) {
        try (Jedis jedis = new Jedis(REDIS_HOST, PORT)) {
            String questionsKey = "event:" + event.getUserId() + ":questions";

            jedis.rpush(questionsKey, question);
        }
    }

    public Event findById(String id) {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            String key = "event:" + id;
            Map<String, String> data = jedis.hgetAll(key);

            if (!data.isEmpty()) {
                String userId = data.get("userId");
                String userName = data.get("userName");

                List<String> questions = jedis.lrange("event:" + id + ":questions", 0, -1);
                List<Donation> donations = getDonations(jedis.lrange("event:" + id + ":donations", 0, -1));

                return new Event(userId, userName, questions, donations);
            } else {
                return null;
            }
        }
    }

    public void addDonation(Event event, String userDonorId, double amount) {
        try (Jedis jedis = new Jedis(REDIS_HOST, PORT)) {
            String key = "event:" + event.getUserId() + ":donations";

            Donation donation = new Donation(userDonorId, amount);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(donation);

            jedis.rpush(key, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Donation> getDonations(List<String> donationsString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Donation> donations = new ArrayList<>();
            for (String donation : donationsString) {
                donations.add(mapper.readValue(donation, Donation.class));
            }
            return donations;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
