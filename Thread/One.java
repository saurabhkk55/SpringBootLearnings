package Thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class One {
    public static void main(String[] args) {
        ExecutorService executors = Executors.newFixedThreadPool(3);

        executors.submit(() -> System.out.println("One"));
        executors.submit(() -> System.out.println("Two"));
        executors.submit(() -> System.out.println("Three"));
        executors.submit(new DepSvc());
        executors.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("Anonymous A is running");
            }
        });

        executors.shutdown();
    }
}

class DepSvc implements Runnable {
    DepSvc() {
        System.out.println("DepSvc instantiated");
    }

    @Override
    public void run() {
        System.out.println("DepSvc is running");
    }
}
