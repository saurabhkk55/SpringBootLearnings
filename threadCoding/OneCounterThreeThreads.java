package threadCoding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class RoundRobinTask {
    int taskId = 0;
    int counter = 1;
    int maxNumOfThreads;
    int maxCounterLimit;

    public RoundRobinTask(int maxNumOfThreads, int maxCounterLimit) {
        this.maxNumOfThreads = maxNumOfThreads;
        this.maxCounterLimit = maxCounterLimit;
    }

    synchronized void printer(int threadId) throws InterruptedException {
        while (counter <= maxCounterLimit) {
            while(threadId != taskId) {
                if(counter > maxCounterLimit) {
                    notifyAll();
                    return;
                }
                wait();
            }

            if(counter > maxCounterLimit) {
                notifyAll();
                return;
            }

            System.out.println("threadId: " + threadId + " | taskId: " + taskId + " | counter: " + counter);

            counter++;
            taskId = (taskId + 1) % maxNumOfThreads;
            notifyAll();
        }
    }
}

public class OneCounterThreeThreads {
    static void main() {
        int threadCount = 3;
        int maxCounter = 100;
        RoundRobinTask roundRobinTask = new RoundRobinTask(threadCount, maxCounter);

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(() -> {
            try {
                roundRobinTask.printer(0);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        executorService.execute(() -> {
            try {
                roundRobinTask.printer(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        executorService.execute(() -> {
            try {
                roundRobinTask.printer(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executorService.shutdown();
    }
}
