package org.example.model;

public class Contenido {

    private String id_contenido;
    private String titulo;
    private String categoria;
    private Usuario creador;

    public Contenido(String id_contenido, String titulo, String categoria, Usuario usuario) {
        this.id_contenido = id_contenido;
        this.titulo = titulo;
        this.categoria = categoria;
        this.creador= usuario;
    }

    public String getId_contenido() {
        return id_contenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public Usuario getCreador() {
        return creador;
    }
}
