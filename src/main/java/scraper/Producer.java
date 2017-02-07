package scraper;

import java.util.concurrent.ExecutorService;

public class Producer implements Runnable {
    private final ExecutorService consumer;

    public Producer(ExecutorService consumer) {
        this.consumer = consumer;
    }

    public void run() {

        for (int i = 0; i < 100 && (!Thread.currentThread().isInterrupted()); i++) {
            Runnable consume = new Consumer("link " + i);
            consumer.submit(consume);
        }
        consumer.shutdown();
        System.out.println("Done.");

    }
}
