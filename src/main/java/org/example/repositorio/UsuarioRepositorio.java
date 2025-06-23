package org.example.repositorio;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.bd.Conexion;
import org.example.bd.ConexionNeo4j;
import org.example.model.Usuario;
import org.neo4j.driver.Session;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class UsuarioRepositorio {
    private final MongoCollection<Document> coleccion;
    private final ConexionNeo4j conexionNeo4j;

    public UsuarioRepositorio() {
        MongoDatabase db = Conexion.getClient().getDatabase("comuniarte");
        this.coleccion = db.getCollection("usuarios");
        this.conexionNeo4j = new ConexionNeo4j();
    }

    public void guardar(Usuario usuario) {
        Document doc = new Document("id_usuario", usuario.getId_usuario())
                .append("nombre", usuario.getNombre())
                .append("apellido", usuario.getApellido())
                .append("email", usuario.getEmail())
                .append("edad", usuario.getEdad())
                .append("Tipo de usuario", usuario.getTipo());

        coleccion.insertOne(doc);
        guardarEnNeo4j(usuario);

    }

    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        for (Document doc : coleccion.find()) {
            lista.add(new Usuario(doc.getString("id_usuario"), doc.getString("nombre"),
                    doc.getString("apellido"), doc.getString("email"), doc.getInteger("edad"),
                    doc.getString("tipo")));
        }
        return lista; //esto retorna una lista de usuarios
    }

    public Usuario obtenerUsuario(String id) {
        return getUsuario(id);
    }

    public String modificarUsuario(String id, String mail) {
        Usuario usuario = getUsuario(id);
        coleccion.updateOne(
                eq("id_usuario", usuario.getId_usuario()),  // filtro
                combine(set("email", mail))
        );
        return "Usuario de id: " + id + " modificado";
    }

    public String eliminarUsuario(String id) {
        Usuario usuario = getUsuario(id);
        coleccion.deleteOne(eq("id_usuario", id));
        return "Usuario de id: " + id + " eliminado";
    }

    private Usuario getUsuario(String id) {
        Document doc = coleccion.find(eq("id_usuario", id)).first();
        if (doc == null) {
            return null;
        }
        Usuario usuario = new Usuario(doc.getString("id_usuario"), doc.getString("nombre"),
                doc.getString("apellido"), doc.getString("email"), doc.getInteger(
                "edad"), doc.getString("tipo"));
        return usuario;

    }

    private void guardarEnNeo4j(Usuario usuario) {
        try (Session session = conexionNeo4j.getDriver().session()) {
            String query = "CREATE (u:Usuario {id_usuario: $id, nombre: $nombre, tipo: $tipo})";
            session.run(query, org.neo4j.driver.Values.parameters(
                            "id", usuario.getId_usuario(),
                            "nombre", usuario.getNombre(),
                            "tipo", usuario.getTipo()
                    ));
        } catch (Exception e) {
            System.err.println("Error guardando en Neo4j: " + e.getMessage());
        }
    }


}
