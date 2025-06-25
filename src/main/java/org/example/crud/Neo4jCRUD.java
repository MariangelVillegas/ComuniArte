package org.example.crud;

import org.example.bd.ConexionNeo4j;
import org.example.model.Contenido;
import org.example.model.Usuario;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.summary.ResultSummary;
import org.neo4j.driver.types.Node;


public class  Neo4jCRUD {
    private static ConexionNeo4j conexionNeo4j = null;

    public Neo4jCRUD() {
        this.conexionNeo4j = new ConexionNeo4j();
    }

    public static void guardarEnNeo4j(Usuario usuario) {
        try (Session session = conexionNeo4j.getDriver().session()) {
            String query = "CREATE (u:Usuario {id_usuario: $id, nombre: $nombre, tipo: $tipo})";
            session.run(query, Values.parameters(
                    "id", usuario.get_id(),
                    "nombre", usuario.getNombre(),
                    "tipo", usuario.getTipo()
            ));
        } catch (Exception e) {
            System.err.println("Error guardando en Neo4j: " + e.getMessage());
        }
    }

    public static void seguirUsuario(String idSeguidor, String idSeguido) {
        Usuario usuario = obtenerUsuarioPorIdUsuario(idSeguido);
        if (usuario != null) {
            try (Session session = conexionNeo4j.getDriver().session()) {
                session.writeTransaction(tx -> {
                    tx.run("""
                                MATCH (a:Usuario {id_usuario: $idSeguidor}), (b:Usuario {id_usuario: $idSeguido})
                                MERGE (a)-[:SIGUE]->(b)
                            """, Values.parameters("idSeguidor", idSeguidor, "idSeguido", idSeguido));
                    return null;
                });
            }
        } else {
            System.out.println("El usuario que quiere seguir no existe");
        }


    }

    public static Usuario obtenerUsuarioPorIdUsuario(String idUsuario) {
        try (Session session = conexionNeo4j.getDriver().session()) {
            Record resultado = session.<Record>readTransaction(tx -> {
                String query = "MATCH (u:Usuario {id_usuario: $id_usuario}) RETURN u";
                Result result = tx.run(query, Values.parameters("id_usuario", idUsuario));
                if (result.hasNext()) {
                    return result.next(); // Retorna el Record directamente
                } else {
                    return null; // Si no hay resultado, retorna null
                }
            });

            if (resultado != null) { // Ahora 'resultado' es un Record o null
                Node nodo = resultado.get("u").asNode();
                return new Usuario(
                        nodo.get("id_usuario").asString(),
                        nodo.get("nombre").asString(),
                        nodo.get("tipo").asString()
                );
            } else {
                return null;
            }
        }
    }

    public static void DejarDeSeguirUsuario(String idSeguidor, String idSeguido) {
        String query = "MATCH (u:Usuario {id_usuario: $idUsuario1})-[r:SIGUE]->(e:Usuario {id_usuario: $idUsuario2}) DELETE r";

        try (Session session = conexionNeo4j.getDriver().session()) {

            ResultSummary summary = session.writeTransaction(new TransactionWork<ResultSummary>() {
                @Override
                public ResultSummary execute(Transaction tx) {
                    Result result = tx.run(query, Values.parameters(
                            "idUsuario1", idSeguidor,
                            "idUsuario2", idSeguido
                    ));
                    return result.consume();
                }
            });

        }
    }
    public static void crearContenido(Contenido contenido){
        try (Session session = conexionNeo4j.getDriver().session()) {
            String query = "CREATE (c:Contenido {id_contenido: $id_contenido, titulo: $titulo, categoria: $categoria})";
            session.run(query, Values.parameters(
                    "id_contenido", contenido.getId_contenido(),
                    "titulo", contenido.getTitulo(),
                    "categoria", contenido.getCategoria()));
        } catch (Exception e) {
            System.err.println("Error guardando en Neo4j: " + e.getMessage());
        }
        UsuarioContenido(contenido.getCreador().get_id(), contenido.getId_contenido());

    }
    public static void UsuarioContenido(String id_usuario, String id_contenido) {
            try (Session session = conexionNeo4j.getDriver().session()) {
                session.writeTransaction(tx -> {
                    tx.run("""
                                MATCH (a:Usuario {id_usuario: $id_creador}), (b:Contenido {id_contenido: $id_contenido})
                                MERGE (a)-[:CREA]->(b)
                            """, Values.parameters("id_creador", id_usuario, "id_contenido", id_contenido));
                    return null;
                });
            }

        }

    public static void Interaccion(String id_usuario, String id_contenido) {
        try (Session session = conexionNeo4j.getDriver().session()) {
            session.writeTransaction(tx -> {
                tx.run("""
                                MATCH (a:Usuario {id_usuario: $id_usuario}), (b:Contenido {id_contenido: $id_contenido})
                                MERGE (a)-[:LIKE]->(b)
                            """, Values.parameters("id_usuario", id_usuario, "id_contenido", id_contenido));
                return null;
            });
        }

    }

}