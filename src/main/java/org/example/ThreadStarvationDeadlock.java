package org.example;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Example of Thread starvation deadlock.
 * We use SingleThreadExecutor which processes only ONE task at a time.
 * ParentTask waits for a ChildTask result.
 * ChildTask waits in an Executor's queue for a previous task to be finished.
 * Fix: use other kind of Executor, for example Executors.newFixedThreadPool(2)
 */
public class ThreadStarvationDeadlock {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws Exception {
        var answer = executor.submit(new ParentTask());
        System.out.println("Answer is: " + answer.get());
        executor.shutdown();
    }

    static class ParentTask implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("Parent task was started");
            var result = executor.submit(new ChildTask());
            System.out.println("Child task was submitted to executor");
            return result.get(); // waiting for result forever
        }
    }

    static class ChildTask implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("Child task was started"); // never happens
            return "success"; // never happens
        }
    }
}
