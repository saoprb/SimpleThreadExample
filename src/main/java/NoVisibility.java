import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * Created by saoprb on 11/29/2016.
 */
public class NoVisibility {
    private static boolean ready;
    private static int number;

    private static class ReaderRunnable implements Runnable {
        @Override
        public void run() {
            while (!ready) {
                Thread.yield();
            }
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new Thread(new ReaderRunnable()).start();
        number = 42;
        ready = true;
    }
}
