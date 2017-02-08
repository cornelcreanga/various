package scraper;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        //The numbers are just silly tune parameters. Refer to the API.
        //The important thing is, we are passing a bounded queue.
        ExecutorService consumer = new ThreadPoolExecutor(8,8,30, TimeUnit.SECONDS,new LinkedBlockingQueue<>(1000000));

        //No need to bound the queue for this executor.
        //Use utility method instead of the complicated Constructor.
        ExecutorService producer = Executors.newSingleThreadExecutor();

        Runnable produce = new Producer(consumer,"https://www.petitieonline.com/signatures/suntem_impotriva_oug_13_2017_de_modificare_a_cp/");
        producer.submit(produce);
        producer.shutdown();
    }

}
