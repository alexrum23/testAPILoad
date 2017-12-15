package ru.sbt.task;

import org.junit.*;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainTest{

    private int RPS;
    private int NUMBER_OF_THREADS;
    private APIHandler apiHandler;
    private ExecutorService service;
    private Random random;
    private int accessCount;

    @Before
    public void setUp(){
        NUMBER_OF_THREADS=100;
        service = Executors.newFixedThreadPool(4);
        random = new Random();
        int accessCount=0;
    }

    @Test
    public void testRPS10()
    {
        RPS = 10;
        apiHandler = new APIHandler(RPS);

        long start = System.currentTimeMillis();
        makeRequests(apiHandler);
        long executionTime = System.currentTimeMillis() - start;

        Assert.assertTrue("Bandwidth check",accessCount< (double)executionTime/1000 *RPS);
    }

    @Test
    public void testRPS500()
    {
        RPS = 500;
        apiHandler = new APIHandler(RPS);
        long start = System.currentTimeMillis();
        makeRequests(apiHandler);
        long executionTime = System.currentTimeMillis() - start;

        Assert.assertTrue("Bandwidth check",accessCount< (double)executionTime/1000 *RPS);
    }

    @Test
    public void testRPS10000()
    {
        RPS = 10000;
        apiHandler = new APIHandler(RPS);
        long start = System.currentTimeMillis();
        makeRequests(apiHandler);
        long executionTime = System.currentTimeMillis() - start;

        Assert.assertTrue("Bandwidth check",accessCount< (double)executionTime/1000 *RPS);
    }

    private void makeRequests(APIHandler apiHandler){

        for (int i = 0; i<NUMBER_OF_THREADS; i++){
            service.submit(()-> {
                try {
                    //randomize request time
                    Thread.sleep(random.nextInt(100));
                    apiHandler.proceedRequest();
                    countAccessedRequests();
                } catch (Exception e) {
                    System.err.println("Access to API denied");
                }
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void countAccessedRequests(){
        accessCount++;
    }

}
