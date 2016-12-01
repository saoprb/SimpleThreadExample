import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by saoprb on 12/1/2016.
 */
public class ThrowExceptionFromCallable {

    private final static class TaskResult {
        private final String threadName;
        private final String className;
        private final Integer result;

        public TaskResult(String threadName, String className, Integer result) {
            this.threadName = threadName;
            this.className = className;
            this.result = result;
        }

        public String toString() {
            return String.format("ThreadName: %s, ClassName: %s, Result: %s", threadName, className, result);
        }
    }

    private static class ThrowTask implements Callable<TaskResult> {
        @Override
        public TaskResult call() throws Exception {
            Thread.sleep(new Random().nextInt(5*1000));
            System.out.format("ThrowTask: %s%n", Thread.currentThread().getName());
            int divideByZero = 1/0;
            return new TaskResult(Thread.currentThread().getName(), this.getClass().getName(), 0);
        }
    }

    private static class OkTask implements Callable<TaskResult> {
        @Override
        public TaskResult call() throws Exception {
            Thread.sleep(new Random().nextInt(5*1000));
            System.out.format("OkTask: %s%n", Thread.currentThread().getName());
            return new TaskResult(Thread.currentThread().getName(), this.getClass().getName(), 0);
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Callable<TaskResult> throwTask = new ThrowTask();
        Callable<TaskResult> okTask = new OkTask();
        List<Future<TaskResult>> futures = new ArrayList<>();

        futures.add(executorService.submit(okTask));
        futures.add(executorService.submit(throwTask));

        for (Future<TaskResult> aFuture : futures) {
            try {
                System.out.format("Task Result: %s%n", aFuture.get().toString());
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }

        executorService.shutdown();
    }
}
