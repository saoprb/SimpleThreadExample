/**
 * Created by saopr on 11/25/2016.
 */
public class ProducerConsumerExample {

    public static void main(String[] args) {
//        Drop drop = new DropSimple();
//        new Thread(new Consumer(drop)).start();
//        new Thread(new Producer(drop)).start();

        Drop drop2 = new DropReEntrantLock();
        new Thread(new Consumer(drop2)).start();
        new Thread(new Producer(drop2)).start();
    }

}
