package ru.levelp;

import java.net.Socket;
import java.util.ArrayList;

public class ClientManager {
    private ArrayList<Client> clients = new ArrayList<>();

    /**
     * Событие подключения клиента
     * @param socket - сокет клиента
     */
    public void onClientConnected(final Socket socket) {
        Thread thread = new Thread(() -> {
            Client client = new Client(socket, ClientManager.this);
            client.login();
        });
        thread.start();
    }

    /**
     * Событие окончания авторизации
     * @param client авторизованный клиент
     */
    public void onClientSignedIn(Client client) {
        clients.add(client);
        client.startMessaging();
    }

    /**
     * Событие отключения клиента
     * @param client отключаемый клиент
     */
    public void onClientDisconnected(Client client) {
        clients.remove(client);
    }

    /**
     * Рассылка сообщений клиентам
     * @param message отправляемое сообщение //TODO: type -> Message
     * @param receiver username получателя. Если null - рассылка всем
     */
    public void sendMessage(String message, String receiver) {
        if (receiver == null) {
            for (Client client : clients) {
                client.sendMessage(message);
            }
        }
    }
}
