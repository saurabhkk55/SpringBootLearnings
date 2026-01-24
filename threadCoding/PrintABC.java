package threadCoding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class PrintABCRoundRobinTask {
    int taskId = 0;
    int counter = 1;
    int maxNumOfThreads;
    int maxCounterLimit;

    public PrintABCRoundRobinTask(int maxNumOfThreads, int maxCounterLimit) {
        this.maxNumOfThreads = maxNumOfThreads;
        this.maxCounterLimit = maxCounterLimit;
    }

    synchronized void printer(int threadId) throws InterruptedException {
        while (counter <= maxCounterLimit) {
            while(threadId != taskId) {
                if (counter > maxCounterLimit) {
                    notifyAll();
                    return;
                }
                wait();
            }

            if(counter > maxCounterLimit) {
                notifyAll();
                return;
            }

            switch (threadId) {
                case 0 -> System.out.println("[" + Thread.currentThread().getName() + "] A");
                case 1 -> System.out.println("[" + Thread.currentThread().getName() + "] B");
                case 2 -> System.out.println("[" + Thread.currentThread().getName() + "] C");
                default -> System.out.println("Never");
            }

            counter++;
            taskId = (taskId + 1) % maxNumOfThreads;
            notifyAll();
        }
    }
}

public class PrintABC {
    static void main() {
        int threadCount = 3;
        int maxCounter = 10;
        PrintABCRoundRobinTask printABCRoundRobinTask = new PrintABCRoundRobinTask(threadCount, maxCounter);

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(() -> tryCatch(printABCRoundRobinTask, 0));
        executorService.execute(() -> tryCatch(printABCRoundRobinTask, 1));
        executorService.execute(() -> tryCatch(printABCRoundRobinTask, 2));

        executorService.shutdown();
    }

    static void tryCatch(PrintABCRoundRobinTask printABCRoundRobinTask, int i) {
        try {
            printABCRoundRobinTask.printer(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
