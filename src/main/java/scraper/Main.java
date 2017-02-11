package scraper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws Exception {

        ExecutorService consumer = new ThreadPoolExecutor(8,8,30, TimeUnit.SECONDS,new LinkedBlockingQueue<>(1000000));

        ExecutorService producer = Executors.newSingleThreadExecutor();

//        Runnable produce = new Producer(
//                consumer,
//                "https://www.petitieonline.com/signatures/suntem_impotriva_oug_13_2017_de_modificare_a_cp/",
//                "suntem_impotriva_oug_13_2017_de_modificare_a_cp");
        Runnable produce = new Producer(
                consumer,
                "https://www.petitieonline.com/signatures/sustinem_guvernul_grindeanu/",
                "sustinem_guvernul_grindeanu");
        producer.submit(produce);
        producer.awaitTermination(1,TimeUnit.DAYS);
        producer.shutdown();
    }

}
