package ru.levelp;

import java.io.PrintWriter;
import java.util.ArrayList;

public class SendingThread extends Thread {
    private ClientManager clientManager;
    private ArrayList<String> queue; //TODO: <Message>
    private boolean alive = true;

    public SendingThread(ClientManager clientManager) {
        this.clientManager = clientManager;
        queue = new ArrayList<>();
    }

    @Override
    public void run() {
        while (alive) {
            if (queue.isEmpty()) {
                Thread.yield();
            } else if (alive){
                clientManager.sendMessage(queue.get(0), null);
                queue.remove(0);
            }
        }
    }

    //TODO: message -> type Message
    public void addMessage(String message) {
        queue.add(message);
    }

    public void stopSending() {
        alive = false;
    }
}
