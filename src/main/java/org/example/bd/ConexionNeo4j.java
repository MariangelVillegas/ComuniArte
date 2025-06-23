package org.example.bd;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import java.net.URI;

public class ConexionNeo4j {
    private Driver driver;
    private static final String URI = "neo4j+s://7585eef9.databases.neo4j.io";
    String usuario= "neo4j";
    String clave = "VjDHhJ6QZ-pqstbPzym1pvsQQW8eO-1ZZAKQPyR8Mq4";

   public ConexionNeo4j(){
       driver = GraphDatabase.driver(URI, AuthTokens.basic(usuario, clave));
   }
    public void cerrarConexion() {
        driver.close();
    }

    public void pruebaConexion() {
        try (Session session = driver.session()) {
            String mensaje = session.run("RETURN 'Hola desde Neo4j' AS mensaje")
                    .single().get("mensaje").asString();
            System.out.println(mensaje);
        }
    }

    public Driver getDriver() {
        return driver;
    }
}
