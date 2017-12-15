package ru.sbt.task;

import java.util.concurrent.BlockingQueue;

public class APIHandler implements Runnable{

    private API api;
    private final int NANOSEC_PER_REQUEST;
    private long requestCounter=0;

    private BlockingQueue<Request> queue;

    public APIHandler(int rps, BlockingQueue<Request> queue) {
        api=new API();
        NANOSEC_PER_REQUEST=1000000000/rps;
        this.queue=queue;
    }

    private void executeAPIFunction(){
        api.doSomething();
    }

    @Override
    public void run() {
        long current_time;
        try {
            while (queue.take().getType().equals("GET")){
                System.out.println("OK");
                executeAPIFunction();
                requestCounter++;
                current_time = System.nanoTime();
                while (current_time+NANOSEC_PER_REQUEST>=System.nanoTime());//busy waiting
            }
        }catch (InterruptedException e){
            System.err.println("API Request is denied");
        }
    }

    public long getRequestCounter() {
        return requestCounter;
    }
}
