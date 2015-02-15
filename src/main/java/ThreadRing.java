import java.util.concurrent.locks.LockSupport;

/**
 * -Xss228k
 */
public class ThreadRing extends Thread {

    static final int THREAD_COUNT = 10000;
    static long startTime;

    ThreadRing nextThread;
    volatile boolean waiting = true;
    int message;

    public ThreadRing(int name) {
        super(Integer.toString(name));
    }

    @Override
    public void run() {
        while(true) {
            while (waiting) {
                LockSupport.park();
            }
            if (message == 0) {
                long endTime = System.currentTimeMillis();
                System.out.println(endTime-startTime);
                System.exit(0);
            }
            waiting = true;
            nextThread.message = message - 1;
            nextThread.waiting = false;
            LockSupport.unpark(nextThread);
        }
    }

    public static void main(String args[]) throws Exception {

        ThreadRing first = new ThreadRing(1);
        ThreadRing current = new ThreadRing(2);

        ThreadRing.startTime = System.currentTimeMillis();
        first.start(); // Thread 1

        first.nextThread = current;
        first.message = 500_000;//50_000_000
        first.waiting = false;
        for (int i = 3; i < THREAD_COUNT; i++) {
            current.nextThread = new ThreadRing(i);
            current.start();
            current = current.nextThread;
        }
        current.nextThread = new ThreadRing(THREAD_COUNT);
        current.start(); // Thread THREAD_COUNT-1

        current = current.nextThread;
        current.nextThread = first;
        current.start(); // Thread THREAD_COUNT

    }
}