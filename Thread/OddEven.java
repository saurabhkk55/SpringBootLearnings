package Thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

public class OddEven {
    final static Object objLock = new Object();
    static boolean isOddTurn = true;

    public static void main(String[] args) {
        OddEven oddEven = new OddEven();
        ExecutorService executors = Executors.newFixedThreadPool(2);
        executors.submit(() -> {
            try {
                oddEven.odd();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        executors.submit(() -> {
            try {
                oddEven.even();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executors.shutdown();
    }


    void odd() throws InterruptedException {
        for(int i=1; i<=10; i=i+2) {
            synchronized (objLock) {
                while(!isOddTurn) {
                    // System.out.println("Waiting for Odd S");
                    objLock.wait();
                    // System.out.println("Waiting for Odd X");
                }
                System.out.print(i + " ");
                isOddTurn = false;
                objLock.notify();
            }
        }
    }

    void even() throws InterruptedException {
        for(int i=2; i<=10; i=i+2) {
            synchronized (objLock) {
                while(isOddTurn) {
                    // System.out.println("Waiting for Even S");
                    objLock.wait();
                    // System.out.println("Waiting for Even X");
                }
                System.out.print(i + " ");
                isOddTurn = true;
                objLock.notify();
            }
        }
    }
}
