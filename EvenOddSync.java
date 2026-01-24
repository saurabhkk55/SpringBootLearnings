//public class EvenOddSync {
//    private final Object lock = new Object();
//    private boolean oddTurn = true;
//
//    public static void main(String[] args) {
//        EvenOddSync obj = new EvenOddSync();
//
//        Thread t1 = new Thread(() -> obj.printOdd(), "OddThread");
//        Thread t2 = new Thread(() -> obj.printEven(), "EvenThread");
//
//        t1.start();
//        t2.start();
//    }
//
//    void printOdd() {
//        for (int i=1; i<10; i=i+2) {
//            synchronized (lock) {
//                if(!oddTurn) {
//                    try {
//                        lock.wait();
//                    } catch (InterruptedException e) {
//                        System.out.println("InterruptedException: " + e);
//                    }
//                };
//                System.out.println("ODD : "+i);
//                oddTurn = false;
//                lock.notify();
//            }
//        }
//    }
//
//    void printEven() {
//        for (int i=2; i<=10; i=i+2) {
//            synchronized (lock) {
//                if(oddTurn) {
//                    try{
//                        lock.wait();
//                    } catch (InterruptedException e) {
//                        System.out.println("Interrupted");
//                    }
//                }
//                System.out.println("EVEN: "+i);
//                oddTurn = true;
//                lock.notify();
//            }
//        }
//    }
//}

// ----------------- Using Executors

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EvenOddSync {
    private final Object lock = new Object();
    private boolean oddTurn = true;

    public static void main(String[] args) {
        EvenOddSync obj = new EvenOddSync();

        // Create a thread pool of size 2 (for two tasks)
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Submit tasks instead of starting threads manually
        executor.execute(() -> obj.printOdd());
        executor.execute(() -> obj.printEven());

        executor.shutdown(); // shutdown after tasks finish
    }

    void printOdd() {
        for (int i = 1; i < 10; i = i + 2) {
            synchronized (lock) {
                while (!oddTurn) { // use while instead of if (safer for spurious wakeups)
                    try { lock.wait(); } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("ODD : " + i);
                oddTurn = false;
                lock.notify();
            }
        }
    }

    void printEven() {
        for (int i = 2; i <= 10; i = i + 2) {
            synchronized (lock) {
                while (oddTurn) {
                    try { lock.wait(); } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("EVEN: " + i);
                oddTurn = true;
                lock.notify();
            }
        }
    }
}
