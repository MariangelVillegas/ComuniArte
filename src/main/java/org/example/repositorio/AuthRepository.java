package org.example.repositorio;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Usuario;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class AuthRepository {
    private static final String REDIS_HOST = "localhost";
    private static final int PORT = 6379;
    private static final String KEY = "user:logged";

    public Usuario loginUser(String userId, String name) {
        try (Jedis jedis = new Jedis(REDIS_HOST, PORT)) {
            jedis.hset(KEY, "userId", userId);
            jedis.hset(KEY, "name", name);

            //TODO se puede agregar expiracion
            return new Usuario(userId, name, "ape", "email", 1);
        }
    }

    public void logoutUser() {
        try (Jedis jedis = new Jedis(REDIS_HOST, PORT)) {
            jedis.del(KEY);
        }
    }

    public Usuario getLoggedUser() {
        try (Jedis jedis = new Jedis(REDIS_HOST, PORT)) {
            Map<String, String> data = jedis.hgetAll(KEY);

            if (data == null || data.isEmpty()) {
                return null;
            }

            String userId = data.get("userId");
            String name = data.get("name");

            return new Usuario(userId, name, "apellido", "email", 0);
        }
    }

    public void saveLoggedUser(Usuario usuario) {
        try (Jedis jedis = new Jedis(REDIS_HOST, PORT)) {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(usuario);
            jedis.set(KEY, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
