package ru.levelp;

import com.google.gson.Gson;
import ru.levelp.dao.MessageDatabase;
import ru.levelp.entities.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientWorker {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private SendingThread sender;
    private String username;
//    private MessageDAO messageDatabase;

    public ClientWorker(Socket socket) {
        this.socket = socket;
        prepareStreams();
    }

//    public ClientWorker(Socket socket, MessageDAO messageDatabase) {
//        this.socket = socket;
//        this.messageDatabase = messageDatabase;
//        prepareStreams();
//    }

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
        while (username == null) {
            try {
                String username = reader.readLine();
                if (ClientManager.getInstance().hasClient(username)) {
                    writer.println("Client with same username exists\nTry another username");
                    writer.flush();
                } else {
                    this.username = username;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ClientManager.getInstance().onClientSignedIn(this);
    }

    /**
     * Запуск основной работы с клиентом (открывается возможность переписки)
     */
    public void startMessaging() {
        sender = new SendingThread();
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
        ClientManager clientManager = ClientManager.getInstance();
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
     *
     * @param message полученное сообщение в необработанном виде
     */
    public void onMessageReceived(String message) {
        Message messageObj = new Gson().fromJson(message, Message.class);
        MessageDatabase.getInstance().add(messageObj);
        if (messageObj.getReceiver() != null && messageObj.getReceiver().equals("server")) {
            //..
            if (messageObj.getMessage().equals("getHistory")) {
                //getting history from db
                //prepare response
                //send response
            }
        } else {
            sender.addMessage(messageObj); //object
        }
//        for (Message m : MessageDatabase.getInstance().getAll()) {
//            System.out.println(m.getMessage());
//        }
    }

    /**
     * Метод отправки сообщений
     *
     * @param message отправляемое сообщение //TODO: type -> Message
     */
    public void sendMessage(String message) {
        //TODO: message to json
        writer.println(message);
        writer.flush();
    }

    public String getUsername() {
        return username;
    }
}
