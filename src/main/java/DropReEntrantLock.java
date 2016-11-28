import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by saopr on 11/25/2016.
 */
public class DropReEntrantLock implements Drop {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private String message;
    private boolean empty = true;

    public String take() {
        System.out.format("take lock hold count: %s%n", lock.getHoldCount());
        lock.lock();
        System.out.format("take lock hold count: %s%n", lock.getHoldCount());
        try {
            while (empty) {
                System.out.println("notEmpty.await() lock");
                notEmpty.await();
                System.out.println("notEmpty.await() release");
            }
            empty = true;
            System.out.println("empty = true");
            notFull.signal();
            System.out.println("notFull.signal()");
        } catch (InterruptedException e) {
        } finally {
            System.out.format("take un-lock hold count: %s%n", lock.getHoldCount());
            lock.unlock();
            System.out.format("take un-lock hold count: %s%n", lock.getHoldCount());
        }
        return message;
    }

    public void put(String message) {
        System.out.format("put lock hold count: %s%n", lock.getHoldCount());
        lock.lock();
        System.out.format("put lock hold count: %s%n", lock.getHoldCount());
        try {
            while (!empty) {
                System.out.println("notFull.await() lock");
                notFull.await();
                System.out.println("notFull.await() release");
            }
            empty = false;
            System.out.println("empty = false");
            this.message = message;
            notEmpty.signal();
            System.out.println("notEmpty.signal()");
        } catch (InterruptedException e) {
        } finally {
            System.out.format("put un-lock hold count: %s%n", lock.getHoldCount());
            lock.unlock();
            System.out.format("put un-lock hold count: %s%n", lock.getHoldCount());
        }
    }
}
