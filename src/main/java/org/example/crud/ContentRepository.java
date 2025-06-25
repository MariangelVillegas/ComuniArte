package org.example.repositorio;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.bd.Conexion;
import org.example.model.Category;
import org.example.model.Comment;
import org.example.model.Post;
import org.example.model.Tag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ContentRepository {
    private final MongoCollection<Document> coleccion;

    public ContentRepository() {
        MongoDatabase db = Conexion.getClient().getDatabase("comuniarte");
        this.coleccion = db.getCollection("content");
    }

    public String createPost(Post post) {
        Document doc = new Document()
                .append("id_user", post.getId_user())
                .append("text", post.getText())
                .append("category", post.getCategory().toString())
                .append("tags", post.getTags())
                .append("comments", post.getComments())
                .append("likes", post.getLikes())
                .append("visits", post.getVisits())
                .append("createdAt", post.getCreatedAt());

        coleccion.insertOne(doc);
        return doc.getObjectId("_id").toHexString();
    }

    public Post getPostById(String id) {
        Document doc = coleccion.find(eq("_id", new ObjectId(id))).first();
        if (doc != null) {
            return mapToPost(doc);
        }
        return null;
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        for (Document doc : coleccion.find()) {
            posts.add(mapToPost(doc));
        }
        return posts;
    }

    public void incrementLikes(String postId) {
        coleccion.updateOne(
                eq("_id", new ObjectId(postId)),
                Updates.inc("likes", 1)
        );
    }

    public void updatePost(Post post) {

        List<Document> commentsDoc = post.getComments().stream()
                .map(c -> new Document("userId", c.getUser())
                        .append("text", c.getText())
                        .append("createdAt", c.getCreatedAt()))
                .toList();

        Document doc = new Document()
                .append("id_user", post.getId_user())
                .append("text", post.getText())
                .append("category", post.getCategory().toString())
                .append("tags", post.getTags())
                .append("comments", commentsDoc)
                .append("likes", post.getLikes())
                .append("visits", post.getVisits())
                .append("createdAt", post.getCreatedAt());

        coleccion.replaceOne(Filters.eq("_id", new ObjectId(post.get_id())), doc);
    }

    public boolean deletePost(String id) {
        return coleccion.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount() > 0;
    }

    private Post mapToPost(Document doc) {
        //deletePost(doc.getObjectId("_id").toHexString());
        String _id = doc.getObjectId("_id").toHexString();
        String id_user = doc.getString("id_user");
        String text = doc.getString("text");
        String categoryStr = doc.getString("category");
        List<Tag> tags = doc.getList("tags", Tag.class, new ArrayList<>());
        List<Document> commentDocs = doc.getList("comments", Document.class, new ArrayList<>());
        List<Comment> comments = commentDocs.stream().map(d -> new Comment(
                d.getString("userId"),
                d.getString("text"),
                LocalDateTime.ofInstant(d.getDate("createdAt").toInstant(), ZoneId.systemDefault())
        )).toList();
        int likes = doc.getInteger("likes", 0);
        int visits = doc.getInteger("visits", 0);

        LocalDateTime createdAt = LocalDateTime.now();
        if(doc.getDate("createdAt") != null) {

            createdAt = LocalDateTime.ofInstant(doc.getDate("createdAt").toInstant(), ZoneId.systemDefault());
        }

        return new Post(_id, id_user, text, Category.valueOf(categoryStr), tags, comments, likes, visits, createdAt);
    }
}
