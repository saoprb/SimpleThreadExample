import java.util.Random;

/**
 * Created by saopr on 11/25/2016.
 */
public class Consumer implements Runnable {

    private final Drop drop;

    public Consumer(Drop drop) {
        this.drop = drop;
    }

    public void run() {
        Random random = new Random();

        for (String message = drop.take(); !message.equals("DONE"); message = drop.take()) {
            System.out.format("Message received: %s%n", message);
            try {
                Thread.sleep(random.nextInt(5000));
            } catch (InterruptedException e) {
            }
        }
    }
}
