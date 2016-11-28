import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by saoprb on 11/26/2016.
 */
public class SafeLock {

    static class Friend {
        private final String name;
        private final Lock lock = new ReentrantLock();

        public Friend(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public boolean impendingBow(Friend bower) {
            Boolean myLock = false;
            Boolean yourLock = false;

            try {
                //Try to get a lock on both bower and bowee
                myLock = lock.tryLock();
                //System.out.format("%s: try to lock myLock %s%n", name, myLock);
                yourLock = bower.lock.tryLock();
                //System.out.format("%s: try to lock yourLock %s%n", name, yourLock);
            } finally {
                //If unable to get a lock on both, unlock/reset the successfully locked lock
                if (!(myLock && yourLock)) {
                    if (myLock) {
                        lock.unlock();
                    }
                    if (yourLock) {
                        bower.lock.unlock();
                    }
                }
            }

            return myLock && yourLock;
        }

        public void bow(Friend bower) {
            //Bow only when successfully locked both bower and bowee
            if (impendingBow(bower)) {
                try {
                    System.out.format("%s: %s has bowed to me!%n", name, bower.getName());
                    bower.bowBack(this);
                } finally {
                    //Unlock both locks since bower has successfully finished bowing
                    lock.unlock();
                    bower.lock.unlock();
                }
            } else {
                System.out.format("%s: %s started to bow to me, but saw that I was already bowing to him.%n", name, bower.getName());
            }
        }

        public void bowBack(Friend bower) {
            System.out.format("%s: %s has bowed back to me!%n", name, bower.getName());
        }
    }

    static class BowLoop implements Runnable {
        private final Friend bower;
        private final Friend bowee;

        public BowLoop(Friend bower, Friend bowee) {
            this.bower = bower;
            this.bowee = bowee;
        }

        public void run() {
            Random random = new Random();
            for (int i = 0; i < 50; i++) {
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {}
                bowee.bow(bower);
            }
        }
    }

    public static void main(String[] args) {
        final Friend alphonse = new Friend("Alphonse");
        final Friend gaston = new Friend("Gaston");

        new Thread(new BowLoop(alphonse, gaston)).start();
        new Thread(new BowLoop(gaston, alphonse)).start();
    }
}
