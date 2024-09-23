package org.example;

/**
 * During the static class initialization we spawn and waiting for a separate Thread,
 * which is calling our class static method (thus forced to wait for a static initialization to complete).
 */
public class StaticInitializationDeadlock {

    static {
        System.out.println("Getting ready to greet the world");
        try {
            Thread t = new Thread(new Runnable(){
                @Override
                public void run() {
                    StaticInitializationDeadlock.initialize();  // call static method from static initialization block
                }
            });

            t.start();
            t.join();  // and wait for it to complete

        } catch (InterruptedException ex) {
            System.out.println("won't see me");
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    public static void initialize(){
        System.out.println("Initializing");
    }
}
