package scraper;

public class Consumer implements Runnable {
    private final String link;


    public Consumer(String link) {
        this.link = link;
    }

    public void run() {
        System.out.println(link);
    }
}
