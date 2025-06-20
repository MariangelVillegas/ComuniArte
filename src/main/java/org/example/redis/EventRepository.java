package org.example.redis;

import org.example.model.Event;
import org.example.model.Usuario;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventRepository {

    private static final String LOCALHOST = "localhost";
    private static final int PORT = 6379;

    public Usuario loginUser(String userId, String name) {
        try (Jedis jedis = new Jedis(LOCALHOST, PORT)) {
            String key = "user:logged:" + userId;

            jedis.hset(key, "userId", userId);
            jedis.hset(key, "name", name);

            //TODO se puede agregar expiracion
            return new Usuario(userId, name, "ape", "email", 1);
        }
    }

    public Event save(String userId, String name) {
        try (Jedis jedis = new Jedis("LOCALHOST", PORT)) {

            String key = "event:" + userId;

            jedis.hset(key, "userId", userId);
            jedis.hset(key, "userName", name);
            jedis.lpush("event:ids", userId);

            return new Event(name, userId);
        }
    }

    public List<Event> getAll() {
        List<Event> events = new ArrayList<>();

        try (Jedis jedis = new Jedis(LOCALHOST, PORT)) {
            List<String> ids = jedis.lrange("event:ids", 0, -1);

            for (String id : ids) {
                String key = "event:" + id;
                Map<String, String> data = jedis.hgetAll(key);

                if (!data.isEmpty()) {
                    String userId = data.get("userId");
                    String userName = data.get("userName");
                    List<String> questions = jedis.lrange("event:" + id + ":questions", 0, -1);

                    events.add(new Event(userName, userId, questions));
                }
            }
        }

        return events;
    }

    public void addQuestion(String eventId, String question) {
        try (Jedis jedis = new Jedis(LOCALHOST, PORT)) {
            String questionsKey = "event:" + eventId + ":questions";

            jedis.rpush(questionsKey, question);
        }
    }
}
