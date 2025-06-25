package org.example.view;

import org.example.Main;
import org.example.crud.Neo4jCRUD;
import org.example.model.Category;
import org.example.model.Post;
import org.example.model.Usuario;
import org.example.service.AuthService;
import org.example.service.ContentService;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class ContentScreen {

    private static Scanner scanner = new Scanner(System.in);
    private static final ContentService contentService = new ContentService();
    private static final AuthService authService = new AuthService();

    // Gestión de contenidos
    // Videos, audios, textos, transmisiones en vivo, organizados por categoría y etiquetas
    // comentarios, likes y visualizaciones
    public static void showMenu() {

        System.out.println("1. Crear posteo ");
        System.out.println("2. Ver posteos ");
        System.out.println("3. Mis posteos");
        System.out.println("0. Volver ");

        String opt = scanner.nextLine().trim();

        switch (opt) {
            case "1":
                showNewPost();
                break;
            case "2":
                showAllPosts();
                break;
            case "3":
                showMyPosts();
                break;
            case "0":
                Main.showMenu();
                break;
            default:
                System.out.println("Opción no válida");
        }
    }

    private static void showMyPosts() {
        System.out.println();
        System.out.println("Tus posteos");
        Usuario loggedUser = authService.getLoggedUser();
        List<Post> posts = contentService.getMyPosts(loggedUser.get_id());
        String opt = showPostOptions(posts);
        showMyPostScreen(posts.get(Integer.parseInt(opt) - 1));
    }

    private static void showNewPost() {
        System.out.println("Escriba un posteo.");
        String post = scanner.nextLine();

        System.out.println("Escriba una categoría");
        String category = scanner.nextLine();
        Usuario loggedUser = authService.getLoggedUser();

        Post newPost = contentService.savePost(new Post(loggedUser.get_id(), post, Category.valueOf(category)));
        Neo4jCRUD.crearContenido(newPost);

        showMenu();
    }

    private static void showAllPosts() {
        List<Post> posts = contentService.getAllPost();
        String opt = showPostOptions(posts);
        showPostScreen(posts.get(Integer.parseInt(opt)-1));
    }

    private static String showPostOptions(List<Post> posts) {
        AtomicInteger index = new AtomicInteger(1);
        posts.forEach(post -> {
            System.out.println(index.getAndIncrement() + ". " + post.toString());
        });

        System.out.println("0. Volver");
        String opt = scanner.nextLine().trim();
        if (opt.equals("0")) {
            showMenu();
        }
        return opt;
    }

    private static void showMyPostScreen(Post post) {
        System.out.println();
        System.out.println(post.toString());
        System.out.println("Comentarios:");
        if(post.getComments().isEmpty()) {
            System.out.println(">> No hay comentarios <<");
        }else {
            post.getComments().forEach(comment -> {System.out.println(">> " + comment.getText() + " <<");});
        }

        System.out.println("1. Dar like");
        System.out.println("2. Comentar ");
        System.out.println("3. Editar");
        System.out.println("4. Eliminar");
        System.out.println("0. Volver ");

        String opt = scanner.nextLine().trim();
        switch (opt) {
            case "1":
                contentService.addLike(post);
                showPostScreen(contentService.getPostById(post.get_id()));
                break;
            case "2":
                System.out.println("Ingrese su comentario:");
                String comentario = scanner.nextLine();
                contentService.addComment(post, comentario);
                showPostScreen(contentService.getPostById(post.get_id()));
                break;
            case "3":
                System.out.println("Ingrese el nuevo texto del post:");
                String newText = scanner.nextLine();
                contentService.updatePost(post, newText);
                showPostScreen(contentService.getPostById(post.get_id()));
                break;
            case "4":
                System.out.println("Ingrese S para confirmar");
                String option = scanner.nextLine();
                if (option.equals("S")) {
                    contentService.delete(post);
                }
                showMyPosts();
                break;
            case "0":
                Main.showMenu();
                break;
            default:
                System.out.println("Opción no válida");
        }
    }

    private static void showPostScreen(Post post) {
        //contentService.addVisit();
        System.out.println();
        System.out.println(post.toString());
        System.out.println("Comentarios:");
        if(post.getComments().isEmpty()) {
            System.out.println(">> No hay comentarios <<");
        }else {
            post.getComments().forEach(comment -> {System.out.println(">> " + comment.getText() + " <<");});
        }

        System.out.println("1. Dar like");
        System.out.println("2. Comentar ");
        System.out.println("0. Volver ");

        String opt = scanner.nextLine().trim();
        switch (opt) {
            case "1":
                contentService.addLike(post);
                showPostScreen(contentService.getPostById(post.get_id()));
                break;
            case "2":
                System.out.println("Ingrese su comentario:");
                String comentario = scanner.nextLine();
                contentService.addComment(post, comentario);
                showPostScreen(contentService.getPostById(post.get_id()));
                break;
            case "0":
                Main.showMenu();
                break;
            default:
                System.out.println("Opción no válida");
        }
    }
}
