package ru.sbt.task;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class RequestProducer implements Runnable {

    private BlockingQueue<Request> queue;

    public RequestProducer(BlockingQueue<Request> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            try {
                //Thread.sleep(random.nextInt(100));
                queue.put(new Request("GET"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        //Stop generating requests
        try {
            queue.put(new Request("EXIT"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
