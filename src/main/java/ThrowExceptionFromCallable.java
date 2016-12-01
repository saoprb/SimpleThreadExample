import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by saoprb on 12/1/2016.
 */
public class ThrowExceptionFromCallable {

    private static class ThrowTask implements Callable<Integer> {

        private Integer status = null;

        @Override
        public Integer call() throws Exception {
            Thread.sleep(new Random().nextInt(5*1000));
            System.out.format("ThrowTask: %s%n", Thread.currentThread().getName());
            int divideByZero = 1/0;
            status = 0;
            return status;
        }
    }

    private static class OkTask implements Callable<Integer> {

        private Integer status = null;

        @Override
        public Integer call() throws Exception {
            Thread.sleep(new Random().nextInt(5*1000));
            System.out.format("OkTask: %s%n", Thread.currentThread().getName());
            status = 0;
            return status;
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Callable<Integer> throwTask = new ThrowTask();
        Callable<Integer> okTask = new OkTask();
        List<Future<Integer>> futures = new ArrayList<>();

        futures.add(executorService.submit(okTask));
        futures.add(executorService.submit(throwTask));

        for (Future<Integer> aFuture : futures) {
            try {
                System.out.format("OkFuture: %s%n", aFuture.get());
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }

        executorService.shutdown();
    }
}
