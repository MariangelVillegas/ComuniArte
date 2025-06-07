package org.example.bd;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class Conexion {
    private static final String URI = "mongodb+srv://mariangelUser:3c9N31C3Gu4lrYU2@cluster0.uuyd6ig.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";


    private static MongoClient client;

    public static MongoClient getClient() {
        if (client == null) {
            client = MongoClients.create(URI);
        }
        return client;
    }
}
