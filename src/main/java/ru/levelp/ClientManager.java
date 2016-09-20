package ru.levelp;

import java.net.Socket;
import java.util.ArrayList;

/*
Одиночка - Singleton
 */
public class ClientManager {
    private static ClientManager instance = new ClientManager();
    private ArrayList<ClientWorker> clients = new ArrayList<ClientWorker>();

    private ClientManager() {
    }

    public static ClientManager getInstance() {
//        if (instance == null) {
//            //1 поток здесь
//            //2 тоже тут
//            instance = new ClientManager();
//        }
        return instance;
    }

    /**
     * Событие подключения клиента
     *
     * @param socket - сокет клиента
     */
    public void onClientConnected(final Socket socket) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                ClientWorker client = new ClientWorker(socket);
                client.login();
            }
        });
//        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Событие окончания авторизации
     *
     * @param client авторизованный клиент
     */
    public void onClientSignedIn(ClientWorker client) {
        clients.add(client);
        client.startMessaging();
        System.out.println("Client " + client.getUsername() + " connected");
    }

    /**
     * Событие отключения клиента
     *
     * @param client отключаемый клиент
     */
    public void onClientDisconnected(ClientWorker client) {
        if (clients.remove(client)) {
            System.out.println("Client " + client.getUsername() + " out");
        }
    }

    /**
     * Рассылка сообщений клиентам
     *
     * @param message  отправляемое сообщение //TODO: type -> Message
     * @param receiver username получателя. Если null - рассылка всем
     */
    public void sendMessage(String message, String receiver) {
        if (receiver == null && message != null) {
            for (ClientWorker client : clients) {
                client.sendMessage(message);
            }
        }
    }

    public boolean hasClient(String username) {
        for (ClientWorker c : clients) {
            if (c.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
