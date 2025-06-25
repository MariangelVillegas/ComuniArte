package org.example.view;

import org.example.Main;
import org.example.crud.MongoCRUD;
import org.example.crud.Neo4jCRUD;
import org.example.model.Usuario;
import org.example.service.AuthService;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class UsersScreen {

    private static final Scanner sc = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    static MongoCRUD usuarioRepositorio = new MongoCRUD();

    public static void showUsers() {
        System.out.println();
        System.out.println("Lista de usuarios");
        AtomicInteger index = new AtomicInteger(1);
        List<Usuario> usuarios = usuarioRepositorio.obtenerTodos();
        usuarios.forEach(user -> {
            System.out.println(index.getAndIncrement() + ". " +user.toString());
        });
        System.out.println("0. Volver");

        String opt = sc.nextLine();

        if (opt.equals("0")) {
            Main.showMenu();
        }

        showUserMenu(usuarios.get(Integer.parseInt(opt)-1));
    }

    private static void showUserMenu(Usuario usuario) {

        String loggedUserId = authService.getLoggedUser().get_id();
        boolean isFollowed = Neo4jCRUD.usuarioSigueA(loggedUserId, usuario.get_id());
        if(isFollowed)
            System.out.println("1. Dejar de seguir");
        else
            System.out.println("1. Seguir");
        System.out.println("2. Editar");
        System.out.println("3. Eliminar");
        System.out.println("1.");
        System.out.println("1.");
        System.out.println("1.");
        System.out.println("1.");

        String op = sc.nextLine();

        switch (op) {
            case "1":
                if(isFollowed){
                    unfollow(usuario);
                }else {
                    seguirUsuario(usuario);
                }
                break;
            case "2":
                modificarUsuario(usuario);
                break;
            case "3":
                eliminarUsuario(usuario);
                break;
        }
    }

    private static void unfollow(Usuario usuario) {
        String idSeguidor = authService.getLoggedUser().get_id();
        String idSeguido = usuario.get_id();

        Neo4jCRUD.DejarDeSeguirUsuario(idSeguidor, idSeguido);
        showUserMenu(usuario);
    }

    private static void modificarUsuario(Usuario usuario) {
        System.out.println("Ingrese el el nuevo nombre del usuario: ");
        System.out.println(usuarioRepositorio.modificarUsuario(usuario.get_id(), sc.nextLine()));
        showUserMenu(usuario);
    }

    private  static void eliminarUsuario(Usuario usuario) {
        System.out.println("Ingrese S para confimar: ");
        if(sc.nextLine().equals("S")){
            usuarioRepositorio.eliminarUsuario(usuario.get_id());
        }
        showUserMenu(usuario);
    }

    private static void seguirUsuario(Usuario usuarioSeguido) {

        String idSeguidor = authService.getLoggedUser().get_id();
        String idSeguido = usuarioSeguido.get_id();

        Neo4jCRUD.seguirUsuario(idSeguidor,idSeguido);
        showUserMenu(usuarioSeguido);
    }
}
