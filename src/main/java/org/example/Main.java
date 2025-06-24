package org.example;

import org.example.bd.ConexionNeo4j;
import org.example.model.Usuario;
import org.example.repositorio.UsuarioRepositorio;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static UsuarioRepositorio usuarioRepositorio = new UsuarioRepositorio();
    private static final Scanner scanner = new Scanner(System.in);
    private static String tipoUsuario = null;


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
                System.out.println("1. Agregar Usuario: ");
                System.out.println("2. Listar usuarios: ");
                System.out.println("3. Transmisiones en vivo ");
                System.out.println("4. Obtener Usuario por ID ");
                System.out.println("5. Modificar mail por ID: ");
                System.out.println("6. Eliminar usuario : ");
                System.out.println("0. Salir: ");

                opt = scanner.nextLine().trim();

                switch (opt) {
                    case "1":
                        addUser();
                        break;
                    case "2":
                        listUsers();
                        break;
                    case "3":
                        LivesScreen.showLiveSection();
                        break;
                    case "4":
                        getUserbyId();
                        break;
                    case "5":
                        ModificarUsuario();
                        break;
                    case "6":
                        EliminarUsuario();
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
        }
    }

    public static void addUser() {
        System.out.println("/////////");
        String[] data = new String[5];

        System.out.println("Ingrese el DNI");
        data[0] = scanner.nextLine();

        System.out.println("Ingrese el Nombre:");
        data[1] = scanner.nextLine();

        System.out.println("Ingrese el Apellido:");
        data[2] = scanner.nextLine();

        System.out.println("Ingrese el Email:");
        data[3] = scanner.nextLine();

        boolean aux = false;
        String tipo;

        do {
            System.out.println("Ingrese el tipo de Usuario: 1 para creador o 2 para espectador");
            tipo = scanner.nextLine();
            switch (tipo){
                case "2":
                case "1":
                    tipoUsuario = (tipo.equals("1")) ? "Creador" : "Espectadr";
                    data[4] = tipoUsuario;
                    aux = true;
                    break;
                default:
                    System.out.println("Tipo de usuario incorrecto");
            }
        }while (!aux);

        System.out.println("Ingrese la Edad: ");
        int edad = scanner.nextInt();

        System.out.println("Ingrese la password: ");
        String password = scanner.nextLine();

        usuarioRepositorio.guardar(new Usuario(data[0], data[1],data[2], data[3],edad,data[4], password));
    }

    private static void listUsers(){
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

    private static void getUserbyId(){
        System.out.println("Ingrese el ID del usuario");
        Usuario usuario = usuarioRepositorio.obtenerUsuario(scanner.nextLine());
        if (usuario!=null) {
            System.out.println("DNI: " + usuario.getId_usuario());
            System.out.println("Nombre: " + usuario.getNombre());
            System.out.println("Apellido: " + usuario.getApellido());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Edad: " + usuario.getEdad());
        }else {
            System.out.println("Usuario no encontrado");
        }
    }

    private static void ModificarUsuario(){
        System.out.println("Ingrese el ID del usuario");
        String id = scanner.nextLine();

        if (usuarioRepositorio.obtenerUsuario(id)==null){
            System.out.println("Usuario no existe");
        }else{
            System.out.println("Ingrese el el nuevo mail del usuario: ");
            System.out.println(usuarioRepositorio.modificarUsuario(id, scanner.nextLine()));
        }
    }

    private  static void EliminarUsuario(){
        System.out.println("Ingrese el ID del usuario");
        String id = scanner.nextLine();

        if (usuarioRepositorio.obtenerUsuario(id)==null){
            System.out.println("Usuario no existe");
        }else{
            System.out.println(usuarioRepositorio.eliminarUsuario(id));
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
