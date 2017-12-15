package ru.sbt.task;

import org.junit.*;

import java.util.concurrent.*;

public class MainTest{

    private int RPS;
    private long accessCount;

    @Test
    public void testRPS10()
    {
        RPS = 10;

        long start = System.currentTimeMillis();
        makeRequests(RPS);
        long executionTime = System.currentTimeMillis() - start;

        Assert.assertTrue("Bandwidth check",accessCount< (double)executionTime/1000 *RPS);
    }

    @Test
    public void testRPS500()
    {
        RPS = 500;

        long start = System.currentTimeMillis();
        makeRequests(RPS);
        long executionTime = System.currentTimeMillis() - start;

        Assert.assertTrue("Bandwidth check",accessCount < (double)executionTime/1000 *RPS);
    }

    @Test
    public void testRPS10000()
    {
        RPS = 10000;

        long start = System.currentTimeMillis();
        makeRequests(RPS);
        long executionTime = System.currentTimeMillis() - start;

        Assert.assertTrue("Bandwidth check",accessCount< (double)executionTime/1000 *RPS);
    }

    private void makeRequests(int RPS){

        BlockingQueue<Request> queue = new ArrayBlockingQueue<Request>(RPS);
        RequestProducer producer = new RequestProducer(queue);
        APIHandler consumer = new APIHandler(RPS,queue);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

        try {
            while (consumerThread.isAlive()){
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        accessCount = consumer.getRequestCounter();
    }

}
