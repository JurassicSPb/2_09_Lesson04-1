package ru.levelp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ClientManager clientManager;
    private BufferedReader reader;
    private PrintWriter writer;
    private SendingThread sender;
    private String username;

    public Client(Socket socket, ClientManager clientManager) {
        this.socket = socket;
        this.clientManager = clientManager;
        prepareStreams();
    }

    /**
     * Метод открытия потоков ввода-вывода
     */
    private void prepareStreams() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод авторизации
     */
    public void login() {
        try {
            username = reader.readLine();
            clientManager.onClientSignedIn(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Запуск основной работы с клиентом (открывается возможность переписки)
     */
    public void startMessaging() {
        sender = new SendingThread(clientManager);
        sender.start();
        try {
            while (true) {
                String message = reader.readLine();
                if (message == null) {
                    stopClient();
                    break;
                }
                onMessageReceived(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Отключение клиента
     */
    public void stopClient() {
        clientManager.onClientDisconnected(this);
        sender.stopSending();
        try {
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Событие получения сообщения
     * @param message полученное сообщение в необработанном виде
     */
    public void onMessageReceived(String message) {
        //TODO: message (json) -> message (object)
        sender.addMessage(message); //object
    }

    /**
     * Метод отправки сообщений
     * @param message отправляемое сообщение //TODO: type -> Message
     */
    public void sendMessage(String message) {
        //TODO: message to json
        writer.println(message);
        writer.flush();
    }
}
