package ru.levelp;

import java.util.ArrayList;


public class SendingThread extends Thread {
    private volatile ArrayList<String> queue; //TODO: <Message>
    private boolean alive = true;

    public SendingThread() {
        queue = new ArrayList<>();
    }

    @Override
    public void run() {
        while (alive) {
            if (queue.isEmpty()) {
                Thread.yield();
            } else if (alive){
                ClientManager.getInstance().sendMessage(queue.get(0), null);
                queue.remove(0);
            }
        }
    }

    //TODO: message -> type Message
    //main-thread
    public void addMessage(String message) {
        queue.add(message);
    }

    public void stopSending() {
        alive = false;
    }
}
