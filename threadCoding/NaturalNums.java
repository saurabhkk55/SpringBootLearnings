package threadCoding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NaturalNums {

    static final Object lock = new Object();
    static boolean firstHalfPrinted = false;

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        int n = 50;

        // Thread 1 → prints 1 to n/2
        executor.execute(() -> {
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (lock) {

                for (int i = 1; i <= n / 2; i++) {
                    System.out.print(i + " ");
                }
                firstHalfPrinted = true;   // state change
                lock.notify();             // wake up thread 2
            }
        });

        // Thread 2 → prints n/2+1 to n
        executor.execute(() -> {
            synchronized (lock) {
                while (!firstHalfPrinted) {
                    try {
                        System.out.println("_______________LOCKING_____________");
                        lock.wait();       // wait until thread 1 finishes
                        System.out.println("\n_______________LOCK IS RELEASED_____________");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                for (int i = n / 2 + 1; i <= n; i++) {
                    System.out.print(i + " ");
                }
            }
        });

        executor.shutdown();
    }
}
