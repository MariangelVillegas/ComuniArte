package org.example.repositorio;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.bd.Conexion;
import org.example.model.Usuario;

import java.util.ArrayList;
import java.util.List;

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
}
