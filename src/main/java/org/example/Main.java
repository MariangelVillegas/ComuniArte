package org.example;

import org.example.model.Usuario;
import org.example.redis.RedisQueueManager;
import org.example.repositorio.UsuarioRepositorio;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.Scanner;

import static org.example.redis.RedisQueueManager.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static UsuarioRepositorio usuarioRepositorio = new UsuarioRepositorio();
    static Scanner scanner = new Scanner(System.in);
    private static final RedisQueueManager redisQueue = new RedisQueueManager();

    public static void main(String[] args) throws IOException {
        RedisServer redisServer = getRedisServer();
        redisQueue.subscribe(LIVE_CHANNEL);

        showMenu();

        redisServer.stop();
    }

    private static void showMenu() {
        String opt;
        System.out.println("Bienvenidos a ComuniArte, Seleccione una opcion");
        try {
            do{
                System.out.println("1. Agregar Usuario: ");
                System.out.println("2. Listar usuarios: ");
                System.out.println("3. Directos ");
                System.out.println("0. Salir: ");

                opt = scanner.nextLine();

                switch (opt) {
                    case "1":
                        addUser();
                        break;
                    case "2":
                        listUser();
                        break;
                    case "3":
                        showLiveSection();
                }
            }while(!opt.equals("0"));
        }catch (Exception e) {
            System.err.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showLiveSection() {
        String opt;

            System.out.println("1. Iniciar directo");
            System.out.println("2. Unirse");
            System.out.println("0. Volver ");

            opt = scanner.nextLine();

            switch (opt) {
                case "1":
                    //enviar notificación
                    redisQueue.publish(LIVE_CHANNEL, "Comenzó un nuevo directo");
                    redisQueue.subscribe(QUESTIONS_CHANNEL);

                    showHostMenu();
                    break;
                case "2":
                    showViewerMenu();
                    break;
                case "0":
                    showMenu();
            }
    }

    private static void showHostMenu() {
        System.out.println("Iniciando directo");
        String opt;

            System.out.println("1. Unirse al chat");
            System.out.println("2. Terminar directo");
            System.out.println("0. Volver");

            opt = scanner.nextLine();

            switch (opt) {
                case "1":
                    startChat();
                    break;
                case "2":
                    //finalizar suscripción
                    break;
                case "0":
                    showLiveSection();
            }
    }

    private static void showViewerMenu() {
        String opt;

            System.out.println("1. Enviar pregunta");
            System.out.println("2. Unirse al chat");
            System.out.println("0. Volver");

            opt = scanner.nextLine();

            switch (opt) {
                case "1":
                    System.out.println("Ingrese la pregunta");
                    opt = scanner.nextLine();
                    redisQueue.publish(QUESTIONS_CHANNEL, opt);
                    showViewerMenu();
                    break;
                case "2":
                    startChat();
                    break;
                case "3":
                    showLiveSection();
            }
    }

    public static void startChat() {
        String opt;
        System.out.println("Bienvenido al chat, presione 0 para salir ");
        redisQueue.subscribe(CHAT_CHANNEL);
        boolean finish = false;
        while (!finish) {
            opt = scanner.nextLine();
            if (opt.equals("0")) finish = true;
            else redisQueue.publish(CHAT_CHANNEL, opt);
        }
        redisQueue.
    }

    public static void addUser() {
        System.out.println("/////////");
        String[] data = new String[4];

        System.out.println("Ingrese el DNI");
        data[0] = scanner.nextLine();

        System.out.println("Ingrese el Nombre:");
        data[1] = scanner.nextLine();

        System.out.println("Ingrese el Apellido:");
        data[2] = scanner.nextLine();

        System.out.println("Ingrese el Email:");
        data[3] = scanner.nextLine();

        System.out.println("Ingrese la Edad: ");
        int edad = scanner.nextInt();
        scanner.nextLine();

        usuarioRepositorio.guardar(new Usuario(data[0], data[1],data[2], data[3],edad));
    }

    private static void listUser(){
        int i = 1;
        for(Usuario usuario: usuarioRepositorio.obtenerTodos()){
            System.out.println("Usuario #" + i);
            System.out.println("DNI: " + usuario.getId_usuario());
            System.out.println("Nombre: " + usuario.getNombre());
            System.out.println("Apellido: " + usuario.getApellido());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Edad: " + usuario.getEdad());
            System.out.println("!!!!!!!!!!!!!!");
            i+=1;
        }
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
