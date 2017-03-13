package scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import scraper.dao.CampaignDao;
import scraper.dao.DonationDao;
import scraper.dao.NotFoundException;
import scraper.dao.SQLRuntimeException;
import scraper.model.Campaign;
import scraper.model.Donation;

import java.sql.Connection;
import java.sql.SQLException;

public class ConsumerDonations implements Runnable {
    private String link;
    String text;
    private int donationId;


    public ConsumerDonations(String link, String text) {
        this.link = link;
        this.text = text;
    }

    @Override
    public void run() {
        Document doc = null;

        CampaignDao campaignDao = new CampaignDao();
        DonationDao donationDao = new DonationDao();
        Campaign campaign;
        try (Connection c = DbConnection.get()) {

            try {
                campaign = campaignDao.loadByName(c, text);
                return;
            } catch (NotFoundException e) {
                campaign = new Campaign(text, link);
                campaignDao.create(c, campaign);
                System.out.println("Will process camapign " + link);
            }


            try {
                System.out.println("Parsing " + link);
                doc = Jsoup.connect(link).get();
                Elements titles = doc.select(".if-tiny-hide");
                for (int i = 0; i < titles.size(); i++) {
                    Element title = titles.get(i);
                    String name = "";
                    try {
                        name = ((TextNode) title.childNode(1).childNode(0).childNode(0)).getWholeText();
                    }catch (Exception e){};
                    //System.out.println(((TextNode) title.childNode(1).childNode(1)).getWholeText());
                    String rawAmount = ((TextNode) title.childNode(1).childNode(1)).getWholeText().trim();
                    rawAmount = rawAmount.substring(1, rawAmount.indexOf(" EUR")).replaceAll("\\.", "");
                    int amount = Integer.parseInt(rawAmount);
                    donationDao.create(c,new Donation(name,amount,campaign.getId()));

                }

            } catch (Exception e) {
                System.out.println("Error occured during parsing, message is " + e.getMessage());
                System.out.println("Error during parsing link " + link);
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            c.commit();
        } catch (SQLException e) {
            System.out.println("Error occured during execution, message is " + e.getMessage());
            throw new SQLRuntimeException(e);
        }
    }
}
