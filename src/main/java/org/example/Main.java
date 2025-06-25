package org.example;

import org.example.crud.MongoCRUD;
import org.example.crud.Neo4jCRUD;
import org.example.view.ContentScreen;
import org.example.view.LivesScreen;
import org.example.view.LoginScreen;
import org.example.view.UsersScreen;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static MongoCRUD usuarioRepositorio = new MongoCRUD();
    private static final Scanner scanner = new Scanner(System.in);
    static Neo4jCRUD neo4jCRUD = new Neo4jCRUD();

    public static void main(String[] args) throws IOException {
        RedisServer redisServer = getRedisServer();
        LoginScreen.mainMenu();
        redisServer.stop();
    }

    public static void showMenu() {
        String opt;
        System.out.println("Bienvenidos a ComuniArte, Seleccione una opcion");
        try {
            do{
                //1. Gestión de contenidos
                System.out.println("1. Gestión de contenido ");
                System.out.println("2. Listar usuarios: ");
                System.out.println("3. Transmisiones en vivo ");
                System.out.println("10 Agregar like a contenido");
                System.out.println("0. Salir: ");

                opt = scanner.nextLine().trim();

                switch (opt) {
                    case "1":
                        ContentScreen.showMenu();
                        break;
                    case "2":
                        UsersScreen.showUsers();
                        break;
                    case "3":
                        LivesScreen.showLiveSection();
                        break;
                    case "10":
                        likeContenido();
                        break;
                    case "0":
                        System.out.println("Saliendo");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            }while(!opt.equals("0"));
        }catch (Exception e) {
            System.err.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
            showMenu();
        }
    }

    private static void likeContenido(){
        Neo4jCRUD.Interaccion("123","233");
    }



    private static RedisServer getRedisServer() throws IOException {
        RedisServer redisServer = new RedisServer(6379);
        redisServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Program is stopping, executing cleanup code.");
            redisServer.stop();
        }));
        return redisServer;
    }
}
