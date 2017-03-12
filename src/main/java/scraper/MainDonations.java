package scraper;

import java.util.concurrent.*;

public class MainDonations {

    public static void main(String[] args) throws InterruptedException {


        ExecutorService consumer = new ThreadPoolExecutor(8, 8, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000000));

        ExecutorService producer = Executors.newSingleThreadExecutor();


        Runnable produce = new ProducerDonations(
                consumer,
                "https://salveazaoinima.ro/cazuri-finalizate/");


        producer.submit(produce);
        producer.awaitTermination(1, TimeUnit.DAYS);
        producer.shutdown();

    }
}
