package scraper;

import java.util.concurrent.*;

public class MainPetitions {

    public static void main(String[] args) throws Exception {

        ExecutorService consumer = new ThreadPoolExecutor(8,8,30, TimeUnit.SECONDS,new LinkedBlockingQueue<>(1000000));

        ExecutorService producer = Executors.newSingleThreadExecutor();

//        Runnable produce = new ProducerPetitions(
//                consumer,
//                "https://www.petitieonline.com/signatures/suntem_impotriva_oug_13_2017_de_modificare_a_cp/",
//                "suntem_impotriva_oug_13_2017_de_modificare_a_cp");

//        Runnable produce = new ProducerPetitions(
//                consumer,
//                "https://www.petitieonline.com/signatures/sustinem_guvernul_grindeanu/",
//                "sustinem_guvernul_grindeanu");

//        Runnable produce = new ProducerPetitions(
//                consumer,
//                "https://www.petitieonline.com/signatures/cetatenii_discriminati_de_presedintele_basescu_ii_cer_demisia/",
//                "cetatenii_discriminati_de_presedintele_basescu_ii_cer_demisia");

//        Runnable produce = new ProducerPetitions(
//                consumer,
//                "https://www.petitieonline.com/signatures/abuzurile_din_romania_trec_oceanul/",
//                "abuzurile_din_romania_trec_oceanul");

        Runnable produce = new ProducerPetitions(
                consumer,
                "https://www.petitieonline.com/signatures/romania-norvegia_impreuna_pentru_copiii_bodnariu/",
                "romania-norvegia_impreuna_pentru_copiii_bodnariu");


        producer.submit(produce);
        producer.awaitTermination(1,TimeUnit.DAYS);
        producer.shutdown();
    }

}
