package org.example.service;

import org.example.model.Comment;
import org.example.model.Post;
import org.example.model.Usuario;
import org.example.repositorio.ContentRepository;

import java.util.List;

public class ContentService {

    private ContentRepository contentRepository;
    private AuthService authService;

    public ContentService() {
        this.contentRepository = new ContentRepository();
        this.authService = new AuthService();
    }

    public void savePost(Post post) {
        contentRepository.createPost(post);

        contentRepository.getAllPosts();
    }

    public List<Post> getAllPost() {
        return contentRepository.getAllPosts();
    }

    public void addLike(Post post) {
        contentRepository.incrementLikes(post.get_id());
    }

    public Post getPostById(String id) {
        return contentRepository.getPostById(id);
    }

    public void addComment(Post post, String comentario) {
        Usuario user = authService.getLoggedUser();
        post.addComment(new Comment(user.get_id(), comentario));
        contentRepository.updatePost(post);
    }
}
