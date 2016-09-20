package ru.levelp;

import com.google.gson.Gson;
import ru.levelp.entities.Message;

import java.util.ArrayList;


public class SendingThread extends Thread {
    private volatile ArrayList<String> queue; //TODO: <Message>
    private boolean alive = true;

    public SendingThread() {
        queue = new ArrayList<String>();
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

    //main-thread
    public void addMessage(Message message) {
        queue.add(new Gson().toJson(message));
    }

    public void stopSending() {
        alive = false;
    }
}
