package scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.concurrent.ExecutorService;

public class Producer implements Runnable {
    private final ExecutorService consumer;
    private String petition;

    public Producer(ExecutorService consumer, String petition) {
        this.consumer = consumer;
        this.petition = petition;
    }

    public void run() {
        int lastPage = 0;
        try {
            Document doc = Jsoup.connect(petition).get();
            Element pagination = doc.select(".pagination").first();
            Elements childrens = pagination.children();
            TextNode lastPageNode = (TextNode) childrens.get(childrens.size() - 2).childNode(0).childNode(0);
            lastPage = Integer.parseInt(lastPageNode.getWholeText());

        } catch (Exception e) {
            System.out.println("Can't parse the html page");
            e.printStackTrace();
        }
        for (int i = 0; i < lastPage && (!Thread.currentThread().isInterrupted()); i++) {
            String page = "https://www.petitieonline.com/signatures/suntem_impotriva_oug_13_2017_de_modificare_a_cp/start/" + i * 10;
            Runnable consume = new Consumer(page);
            consumer.submit(consume);
        }

        consumer.shutdown();
        System.out.println("Done.");

    }
}
