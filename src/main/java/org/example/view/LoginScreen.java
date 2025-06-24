package org.example.view;

import org.example.service.AuthService;
import org.example.Main;
import org.example.model.Usuario;

import java.util.Scanner;

public class LoginScreen {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();

    public static void mainMenu() {
        System.out.println("1. Iniciar Sesion");
        System.out.println("2. Crear cuenta");

        String opt = scanner.nextLine();

        switch (opt) {
            case "1":
                System.out.println("Ingrese email:");
                String email = scanner.nextLine();
                System.out.println("Ingrese password:");
                String password = scanner.nextLine();
                if(authService.loginUser(email, password) == null){
                    mainMenu();
                }
                Main.showMenu();
                break;
            case "2":
                addUser();
                mainMenu();
                break;
            default:
                mainMenu();
                break;
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
                    String tipoUsuario = (tipo.equals("1")) ? "Creador" : "Espectadr";
                    data[4] = tipoUsuario;
                    aux = true;
                    break;
                default:
                    System.out.println("Tipo de usuario incorrecto");
            }
        }while (!aux);

        System.out.println("Ingrese la Edad: ");
        String edad = scanner.nextLine();

        System.out.println("Ingrese la password: ");
        String password = scanner.nextLine();

        authService.signUpUser(new Usuario(data[0], data[1],data[2], data[3],Integer.parseInt(edad),data[4], password));
    }
}
