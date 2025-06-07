package org.example;

import com.mongodb.client.MongoClient;
import org.example.bd.Conexion;
import org.example.model.Usuario;
import org.example.repositorio.UsuarioRepositorio;

import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static UsuarioRepositorio usuarioRepositorio = new UsuarioRepositorio();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String opt;
        System.out.println("Bienvenidos a ComuniArte, Seleccione una opcion");

        try {
            do{
                System.out.println("1. Agregar Usuario: ");
                System.out.println("2. Listar usuarios: ");
                System.out.println("0. Salir: ");

                opt = scanner.nextLine();

                switch (opt) {
                    case "1":
                        addUser();
                        break;
                    case "2":
                        listUser();
                        break;
                }
            }while(!opt.equals("0"));
        }catch (Exception e) {
            System.err.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
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
}
