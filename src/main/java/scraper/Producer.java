package scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import scraper.dao.NotFoundException;
import scraper.dao.PetitionDao;
import scraper.dao.SQLRuntimeException;
import scraper.model.Petition;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Producer implements Runnable {
    private final ExecutorService consumer;
    private String petitionLink;
    private String petitionName;

    public Producer(ExecutorService consumer, String petitionLink, String petitionName) {
        this.consumer = consumer;
        this.petitionLink = petitionLink;
        this.petitionName = petitionName;
    }

    public void run() {
        int lastPage = 0;
        try {
            Document doc = Jsoup.connect(petitionLink).get();
            Element pagination = doc.select(".pagination").first();
            Elements childrens = pagination.children();
            TextNode lastPageNode = (TextNode) childrens.get(childrens.size() - 2).childNode(0).childNode(0);
            lastPage = Integer.parseInt(lastPageNode.getWholeText());

        } catch (Exception e) {
            System.out.println("Can't parse the html page");
            e.printStackTrace();
        }

        PetitionDao dao = new PetitionDao();
        Petition petition;
        try(Connection c = DbConnection.get()){

            try {
                petition = dao.loadByName(c, petitionName);
            }catch(NotFoundException e){
                petition = new Petition(petitionName,petitionLink);
                dao.create(c,petition);
                c.commit();
            }
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }


        for (int i = 0; i < lastPage && (!Thread.currentThread().isInterrupted()); i++) {
            String page = "https://www.petitieonline.com/signatures/sustinem_guvernul_grindeanu/start/" + i * 10;
            Runnable consume = new Consumer(page,petition.getId(),i);
            consumer.submit(consume);
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
