package threadCoding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class HelloWorld {
    int maxCounter;
    int counter = 1;
    int helloCounter = 0;
    int worldCounter = 0;
    boolean isHelloTurn = true;

    HelloWorld (int maxCounter) {
        this.maxCounter = maxCounter;
    }

    synchronized void printHelloAfter1Second () throws InterruptedException {
        while(counter <= maxCounter) {
            while(!isHelloTurn) {
                if(counter > maxCounter) {
                    notify();
                    return;
                }
                wait();
            }
            Thread.currentThread().sleep(1000);
            System.out.println("[" + Thread.currentThread().getName() + "] " + "HELLO " + " | COUNTER: " + counter + " | isHelloTurn: " + isHelloTurn);
            helloCounter++;
            counter++;

            if(counter > maxCounter) {
                notify();
                return;
            }

            if(helloCounter == 2) {
                helloCounter = 0;
                isHelloTurn = false;
                notify();
            }
        }
    }
    synchronized void printWorldAfter2Second () throws InterruptedException {
        while(counter <= maxCounter) {
            while(isHelloTurn) {
                if(counter > maxCounter) {
                    notify();
                    return;
                }
                wait();
            }
            // Thread.currentThread().sleep(1000);
            System.out.println("[" + Thread.currentThread().getName() + "] " + "WORLD " + " | COUNTER: " + counter + " | isHelloTurn: " + isHelloTurn);
            worldCounter++;
            counter++;

            if(counter > maxCounter) {
                notify();
                return;
            }

            if(worldCounter == 1) {
                worldCounter = 0;
                isHelloTurn = true;
                notify();
            }
        }
    }
}

public class HelloWorldMain {
    static void main() {
        HelloWorld helloWorld = new HelloWorld(10);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(() -> { try { helloWorld.printHelloAfter1Second(); } catch (InterruptedException e) { throw new RuntimeException(e); } });
        executorService.execute(() -> { try { helloWorld.printWorldAfter2Second(); } catch (InterruptedException e) { throw new RuntimeException(e); } });

        executorService.shutdown();
    }
}
