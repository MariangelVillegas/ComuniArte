package org.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 1. Gestión de contenidos
// Videos, audios, textos, transmisiones en vivo, organizados por categoría y etiquetas
// comentarios, likes y visualizaciones
public class Post {
    private String _id;
    private String id_user;
    private String text;
    private Category category;
    private List<Tag> tags;
    private List<Comment> comments;
    private int likes;
    private int visits;
    private LocalDateTime createdAt;

    public Post(String id_user, String text, Category category) {
        this.id_user = id_user;
        this.text = text;
        this.category = category;
        tags = new ArrayList<>();
        comments = new ArrayList<>();
        this.likes = 0;
        this.visits = 0;
        this.createdAt = LocalDateTime.now();
    }

    public Post(String _id, String id_user, String text, Category category, List<Tag> tags, List<Comment> comments, int likes, int visits, LocalDateTime createdAt) {
        this._id = _id;
        this.id_user = id_user;
        this.text = text;
        this.category = category;
        this.tags = tags;
        this.comments = new ArrayList<>(comments);
        this.likes = likes;
        this.visits = visits;
        this.createdAt = createdAt;
    }

    public String get_id() {
        return _id;
    }

    public String getId_user() {
        return id_user;
    }

    public String getText() {
        return text;
    }

    public Category getCategory() {
        return category;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public int getLikes() {
        return likes;
    }

    public int getVisits() {
        return visits;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {return createdAt;}
    @Override
    public String toString() {
        return text + "\n" +
                ">> Creado:" + createdAt + " >> likes: " + likes + " >> visitas: " + visits + "\n" +
                ">> Categoria: " + category;
    }

    public void addComment(Comment comentario) {
        comments.add(comentario);
    }
}
