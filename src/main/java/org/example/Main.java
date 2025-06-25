package org.example;

import org.example.crud.MongoCRUD;
import org.example.crud.Neo4jCRUD;
import org.example.model.Contenido;
import org.example.model.Usuario;
import org.example.view.ContentScreen;
import org.example.view.LivesScreen;
import org.example.view.LoginScreen;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static MongoCRUD usuarioRepositorio = new MongoCRUD();
    private static final Scanner scanner = new Scanner(System.in);
    private static String tipoUsuario = null;
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
                System.out.println("4. Obtener Usuario por ID ");
                System.out.println("5. Modificar mail por ID: ");
                System.out.println("6. Eliminar usuario : ");
                System.out.println("7. Seguir usuario : ");
                System.out.println("8. Dejar de seguir : ");
                System.out.println("9. Crear contenido");
                System.out.println("10 Agregar like a contenido");
                System.out.println("0. Salir: ");

                opt = scanner.nextLine().trim();

                switch (opt) {
                    case "1":
                        ContentScreen.showMenu();
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
                    case "7":
                        SeguirUsuario();
                        break;
                    case "8":
                        dejarSeguirUsuario();
                        break;
                    case "9":
                        crearContenido();
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

    private static void listUsers(){
        int i = 1;
        for(Usuario usuario: usuarioRepositorio.obtenerTodos()){
            System.out.println("Usuario #" + i);
            System.out.println("DNI: " + usuario.get_id());
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
            System.out.println("DNI: " + usuario.get_id());
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
    private static void SeguirUsuario(){
        System.out.println("Ingrese el ID del seguidor");
        String idSeguidor = scanner.nextLine();
        System.out.println("Ingrese el ID del seguido");
        String idSeguido = scanner.nextLine();

        Neo4jCRUD.seguirUsuario(idSeguidor,idSeguido);

    }

    private static void dejarSeguirUsuario(){
        System.out.println("Ingrese el ID del seguidor");
        String idSeguidor = scanner.nextLine();
        System.out.println("Ingrese el ID del seguido");
        String idSeguido = scanner.nextLine();

        Neo4jCRUD.DejarDeSeguirUsuario(idSeguidor,idSeguido);

    }
    private static void crearContenido(){

        Usuario usuario = new Usuario("123","LO","98");
        System.out.println("Ingrese el id del contenido");
        String id_contenido = scanner.nextLine();
        System.out.println("Ingrese el titulo");
        String titulo = scanner.nextLine();
        System.out.println("Ingrese la categoria");
        String categoria = scanner.nextLine();

        Neo4jCRUD.crearContenido(new Contenido(id_contenido,titulo,categoria,usuario));

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
