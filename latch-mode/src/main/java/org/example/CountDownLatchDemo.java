package org.example;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class CountDownLatchDemo extends Thread{

    private final Latch latch;
    private final String programmer;
    private final String transportation;

    public CountDownLatchDemo(Latch latch, String programmer, String transportation) {
        this.latch = latch;
        this.programmer = programmer;
        this.transportation = transportation;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }

    public static void main (String[] args) throws InterruptedException {
        Latch latch = new CountDownLatch(4);
        for (int i=0;i<4;i++) {
            new CountDownLatchDemo(latch, "A" + i, "B").start();
        }
        latch.await();
    }
}
