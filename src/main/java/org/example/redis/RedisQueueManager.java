package org.example.redis;

import org.example.model.Usuario;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.CountDownLatch;

public class RedisQueueManager {

    public static String EVENTS_CHANNEL = "live-notifications";
    public static String QUESTIONS_CHANNEL = "questions-notifications";
    public static String CHAT_CHANNEL = "chat-notifications";
    private final CountDownLatch latch = new CountDownLatch(1);
    private static final String LOCALHOST = "localhost";
    private static final int PORT = 6379;

    public void publish(String channel, String message) {
        try {
            latch.await();
            try (Jedis jedis = new Jedis(LOCALHOST, PORT)) {
                jedis.publish(channel, message);
                System.out.println("Message published!");
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribe(String channel) {
        new Thread(() -> {
            try (Jedis jedis = new Jedis(LOCALHOST, PORT)) {
                JedisPubSub pubSub = new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        try (Jedis historyRedis = new Jedis(LOCALHOST, PORT)) {
                            historyRedis.lpush(channel, message);
                        }
                        System.out.println("Mensaje recibido del canal " + channel + ": " + message);
                    }

                    @Override
                    public void onSubscribe(String channel, int subscribedChannels) {
                        latch.countDown();
                    }
                };
                jedis.subscribe(pubSub, channel);
            } catch (Exception e) {
                System.err.println("Error en la suscripción: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}
