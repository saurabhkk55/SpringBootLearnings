package threadCoding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NaturalNums2 {

    static final Object lock = new Object();

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        int n = 50;

        // Thread 1 → prints 1 to n/2
        executor.execute(() -> {
            synchronized (lock) {
                for (int i = 1; i <= n/2; i++) {
                    System.out.print(i + " ");
                }
            }
        });

        // Thread 2 → prints n/2+1 to n
        executor.execute(() -> {
            try {
                Thread.currentThread().sleep(200); // sleep for 0.2 seconds which is less than a second.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (lock) {
                for (int i = n / 2 + 1; i <= n; i++) {
                    System.out.print(i + " ");
                }
            }
        });

        executor.shutdown();
    }
}
