package scraper;

import org.apache.commons.lang.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import scraper.model.PetitionSignature;

import java.sql.Timestamp;
import java.util.List;

public class ConsumerDonations implements Runnable {
    private String link;
    String text;
    private int donationId;


    public ConsumerDonations(String link,String text) {
        this.link = link;
        this.text = text;
    }

    @Override
    public void run() {
        Document doc = null;
        try {
            System.out.println("Parsing "+link);
            doc = Jsoup.connect(link).get();
            Elements titles = doc.select(".if-tiny-hide");

            System.out.println(((TextNode)titles.first().childNode(1).childNode(0).childNode(0)).getWholeText());
            System.out.println(((TextNode)titles.first().childNode(1).childNode(1)).getWholeText());
            String rawAmount = ((TextNode)titles.first().childNode(1).childNode(1)).getWholeText().trim();
            rawAmount = rawAmount.substring(1,rawAmount.indexOf(" EUR")).replaceAll("\\.","");
            int amount = Integer.parseInt(rawAmount);



            System.out.println(titles.size());

        } catch (Exception e) {
            System.out.println("Error occured during parsing, message is "+e.getMessage());
            System.out.println("Error during parsing link "+link);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
