package Record.CompletableFuture;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo {
    static void main(String[] args) {
        // thenApply() - Transform the Result - like map in java 8 streams
        // Demo-1
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println("Fetching user...");
                    return "Saurabh";
                });

        CompletableFuture<String> upperCaseFuture = future
                .thenApply(name -> {
                    return name.toUpperCase();
                });

        System.out.println(upperCaseFuture.join());

        // thenAccept() - Consume the Result - like consumer function interface or forEach in java 8 streams
        // Demo-1
        CompletableFuture<Void> future1 =
                CompletableFuture
                        .supplyAsync(() -> "Hello")
                        .thenAccept(value -> {
                            System.out.println(value);
                        });

        future1.join();

        // Demo-2
        CompletableFuture
                .supplyAsync(() -> fetchOrder())
                .thenAccept(order -> saveOrder(order));

        // thenRun() - Execute Another Task
        // Demo-1
        CompletableFuture<Void> future2 = CompletableFuture
                .supplyAsync(() -> {
                    return "Done";
                })
                .thenRun(() -> {
                    System.out.println("Cleanup completed.");
                });

        future2.join();
    }

    static int fetchOrder() {
        return 1;
    }

    static void saveOrder(int id) {
        System.out.println("Order id: " + id);
    }
}

//Output:
//Fetching user...
//SAURABH
//Hello
//Order id: 1
//Cleanup completed.
