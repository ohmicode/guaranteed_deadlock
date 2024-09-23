package org.example;

import java.util.concurrent.CountDownLatch;

/**
 * Example of wrong usage of CountDownLatch and synchronized block.
 * All threads a synchronized on both monitor object (BadRunnable.class) and CountDownLatch.
 * latch.countDown() happens for the first Thread owning the monitor.
 * Then first Thread is waiting for CountDownLatch and the rest of them are waiting for the monitor object.
 * Fix: move latch.countDown() outside the synchronized block.
 */
public class CountDownLatchDeadlock {

    public static class BadRunnable implements Runnable {
        private CountDownLatch latch;

        public BadRunnable(CountDownLatch latch) {
            this.latch = latch;
        }

        public void run() {
            System.out.println("Thread " + Thread.currentThread().getId() + " starting");
            synchronized (BadRunnable.class) {
                System.out.println("Thread " + Thread.currentThread().getId() + " acquired the monitor on BadRunnable.class");
                latch.countDown();
                while (true) {
                    try {
                        latch.await();
                    } catch (InterruptedException ex) {
                        continue;
                    }
                    break;
                }
            }
            System.out.println("Thread " + Thread.currentThread().getId() + " released the monitor on BadRunnable.class");
            System.out.println("Thread " + Thread.currentThread().getId() + " ending");
        }
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[20];
        CountDownLatch latch = new CountDownLatch(threads.length);
        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new Thread(new BadRunnable(latch));
            threads[i].start();
        }
    }
}
