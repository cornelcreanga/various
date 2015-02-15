package benchmark;

public class NaiveCpuTest {

    /**
     * 8 13500           25700       31636       83656             18159             15113
     * 4  9700           13000       16555       42505             11896             7966
     * 2  8000           8700        10829       21478             11696             7508
     * 1  7700(mac mini) 7300(home)  10349(work) 18792 (aurelian)  11348 Xeon W3550  7362 i7-2720QM
     *
     */

    public static void main(String args[]) throws InterruptedException {
        for (int i = 1; i <= 8; ) {
            long t1 = System.currentTimeMillis();
            Thread[] threads = new Thread[i];
            for (int counter = 0; counter < threads.length; counter++) {
                threads[counter] = new Thread(new WorkerFibonacci());
                threads[counter].start();
            }
            for (Thread t : threads)
                t.join();
            long t2 = System.currentTimeMillis();
            System.out.println(t2 - t1);
            i *= 2;
            System.gc();
            System.gc();
            System.gc();
        }
    }
}

class Fibonacci {
    public static long fib(int n) {
        if (n <= 1) return n;
        else return fib(n - 1) + fib(n - 2);
    }
}

class WorkerFibonacci implements Runnable {
    public void run() {
        for (int i = 0; i < 45; i++)
            Fibonacci.fib(i);
    }
}




