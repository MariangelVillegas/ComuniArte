package org.example;

import org.example.model.Event;
import org.example.model.Usuario;

import java.util.List;
import java.util.Scanner;


public class LivesScreen {

    static Scanner scanner = new Scanner(System.in);
    private static final EventsService eventsService = new EventsService();

    public static void showLiveSection() {
        eventsService.subscribeEvents();
        String opt;
        System.out.println("Nombre del usuario:");
        String userName = scanner.nextLine();
        Usuario user = eventsService.saveLoggedUser("1", userName);

        System.out.println("1. Iniciar directo");
        System.out.println("2. Unirse");
        System.out.println("0. Volver ");

        opt = scanner.nextLine();

        switch (opt) {
            case "1":
                Event event = eventsService.startLive(user.getId_usuario(), user.getNombre());
                showHostMenu(event);
                break;
            case "2":
                showLiveEvents(userName);
                break;
            case "0":
                break;
        }
    }

    private static void showLiveEvents(String userName) {
        System.out.println("Seleccione el evento:");

        String opt;
        int i = 1;

        List<Event> lives = eventsService.getLives();
        lives.forEach(live -> {
            System.out.println(i + ". " + live.getUserId());
        });
        System.out.println("0. Volver");

        opt = scanner.nextLine();

        if (opt.equals("0")) {
            showLiveSection();
        }

        showViewerMenu(lives.get(Integer.parseInt(opt)-1), userName);
    }

    private static void showHostMenu(Event event) {
        System.out.println("Iniciando directo");
        String opt;

        System.out.println("1. Unirse al chat");
        System.out.println("2. Ver preguntas");
        System.out.println("3. Ver donaciones");
        System.out.println("4. Terminar directo");
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
                break;
            default:
                showHostMenu(event);
        }
    }

    private static void showViewerMenu(Event event, String userName) {
        String opt;

        System.out.println("1. Enviar pregunta");
        System.out.println("2. Unirse al chat");
        System.out.println("0. Volver");

        opt = scanner.nextLine();

        switch (opt) {
            case "1":
                System.out.println("Ingrese la pregunta");
                opt = scanner.nextLine();
                eventsService.addNewQuestion(event.getUserId(), opt);
                showViewerMenu(event, userName);
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
        //redisQueue.subscribe(CHAT_CHANNEL);
        boolean finish = false;
        while (!finish) {
            opt = scanner.nextLine();
            if (opt.equals("0")) finish = true;
          //  else redisQueue.publish(CHAT_CHANNEL, opt);
        }
    }
}
