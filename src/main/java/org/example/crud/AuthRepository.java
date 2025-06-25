package org.example.repositorio;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Usuario;
import redis.clients.jedis.Jedis;

public class AuthRepository {
    private static final String REDIS_HOST = "localhost";
    private static final int PORT = 6379;
    private static final String KEY = "user:logged";

    public void logoutUser() {
        try (Jedis jedis = new Jedis(REDIS_HOST, PORT)) {
            jedis.del(KEY);
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

    public Usuario getLoggedUser() {
        try (Jedis jedis = new Jedis(REDIS_HOST, PORT)) {
            String json = jedis.get(KEY);

            if (json != null) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(json, Usuario.class);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
