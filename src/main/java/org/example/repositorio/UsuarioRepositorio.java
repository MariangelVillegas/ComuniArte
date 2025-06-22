package org.example.repositorio;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.example.bd.Conexion;
import org.example.model.Usuario;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class UsuarioRepositorio {
    private final MongoCollection<Document> coleccion;

    public UsuarioRepositorio() {
        MongoDatabase db = Conexion.getClient().getDatabase("comuniarte");
        this.coleccion = db.getCollection("usuarios");
    }

    public void guardar(Usuario usuario) {
        Document doc = new Document("id_usuario", usuario.getId_usuario())
                .append("nombre", usuario.getNombre())
                .append("apellido", usuario.getApellido())
                .append("email", usuario.getEmail())
                .append("edad", usuario.getEdad());

        coleccion.insertOne(doc);

    }

    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        for (Document doc : coleccion.find()) {
            lista.add(new Usuario(doc.getString("id_usuario"), doc.getString("nombre"),
                    doc.getString("apellido"), doc.getString("email"), doc.getInteger("edad")));
        }
        return lista; //esto retorna una lista de usuarios
    }
    public Usuario obtenerUsuario(String id){
        return getUsuario(id);
    }

    public String modificarUsuario(String id, String mail){
        Usuario usuario = getUsuario(id);
        coleccion.updateOne(
                eq("id_usuario", usuario.getId_usuario()),  // filtro
                combine(set("email", mail))
        );
        return "Usuario de id: "+id+" modificado";
    }

    public String eliminarUsuario(String id){
        Usuario usuario = getUsuario(id);
        coleccion.deleteOne(eq("id_usuario", id));
        return "Usuario de id: "+id+" eliminado";
    }

    private Usuario getUsuario(String id){
        Document doc = coleccion.find(eq("id_usuario", id)).first();
        if (doc == null) {
            return null;
        }
        Usuario usuario = new Usuario(doc.getString("id_usuario"),doc.getString("nombre"),
                doc.getString("apellido"), doc.getString("email"), doc.getInteger(
                "edad") );
        return usuario;

    }

}
