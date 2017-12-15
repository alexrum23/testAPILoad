package ru.sbt.task;

import java.nio.file.AccessDeniedException;

public class APIHandler {

    private API api;
    private final long NANOSEC_PER_REQUEST;
    private long timeOfPrevRequest;

    public APIHandler(int rps) {
        api=new API();
        NANOSEC_PER_REQUEST=1000000000/rps;
    }

    public synchronized void proceedRequest() throws AccessDeniedException{
        long timePassed = System.nanoTime() - timeOfPrevRequest;
        if (timeOfPrevRequest==0 || NANOSEC_PER_REQUEST<=timePassed){
            System.out.println("OK");
            executeAPIFunction();
            timeOfPrevRequest=System.nanoTime();
        }
        else {
            throw new AccessDeniedException("API");
        }
    }

    private void executeAPIFunction(){
        api.doSomething();
    }
}
