package scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import scraper.dao.*;
import scraper.model.Campaign;
import scraper.model.Petition;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ProducerDonations implements Runnable {
    private final ExecutorService consumer;
    private String link;

    public ProducerDonations(ExecutorService consumer, String link) {
        this.consumer = consumer;
        this.link = link;
    }

    public void run() {
        try {
            Document doc = Jsoup.connect(link).get();
            Elements titles = doc.select(".block-title");
            for (int i = 0; i < titles.size(); i++) {
                Element title = titles.get(i);
                Element children = title.children().get(0);
                String text = children.text();
                String link = children.attributes().get("href").trim();
                //https://salveazaoinima.ro/lista-donatori/?campaign=bianca-bodea
                String campaignId = link.substring(link.indexOf("campaigns/")+"campaigns/".length(),link.length()-1);
                CampaignDao dao = new CampaignDao();
                Campaign campaign;
                try(Connection c = DbConnection.get()){

                    try {
                        campaign = dao.loadByName(c, text);
                        continue;
                    }catch(NotFoundException e){
                        System.out.println("Will process camapign "+link);
                    }
                } catch (SQLException e) {
                    System.out.println("Error occured during execution, message is "+e.getMessage());
                    throw new SQLRuntimeException(e);
                }

                Runnable consume = new ConsumerDonations("https://salveazaoinima.ro/lista-donatori/?campaign="+campaignId,text);
                consumer.submit(consume);
            }

            //TextNode lastPageNode = (TextNode) childrens.get(childrens.size() - 2).childNode(0).childNode(0);
            //lastPage = Integer.parseInt(lastPageNode.getWholeText());

        } catch (Exception e) {
            System.out.println("Can't parse the html page");
            e.printStackTrace();
        }


        try {
            consumer.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        consumer.shutdown();
        System.out.println("Done.");

    }
}

