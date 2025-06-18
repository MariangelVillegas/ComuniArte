package org.example.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.CountDownLatch;

public class RedisQueueManager {

    public static String LIVE_CHANNEL = "live-notifications";
    public static String QUESTIONS_CHANNEL = "questions-notifications";
    public static String CHAT_CHANNEL = "chat-notifications";
    private CountDownLatch latch = new CountDownLatch(1);

    public void publish(String channel, String message) {
        try {
            latch.await();
            try (Jedis jedis = new Jedis("localhost", 6379)) {
                jedis.publish(channel, message);
                System.out.println("Message published!");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribe(String channel) {
        new Thread(() -> {
            try (Jedis jedis = new Jedis("localhost", 6379)) {
                JedisPubSub pubSub = new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
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
