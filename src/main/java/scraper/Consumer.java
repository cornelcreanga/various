package scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.List;

public class Consumer implements Runnable {
    private final String link;


    public Consumer(String link) {
        this.link = link;
    }

    public void run() {

        try {
            Document doc = Jsoup.connect(link).get();
            Element signatures = doc.select("table#signatures").first();
            List<Node> nodes = signatures.childNode(2).childNodes();
            for (Node node : nodes) {
                if (node instanceof TextNode)
                    continue;
                if (node.childNodeSize()==3){
                    System.out.println("Ignore");
                }else{
                    Node name = node.childNode(2);
                    String personName = ((TextNode)(name.childNode(0).childNode(0))).getWholeText();
                    System.out.println(personName);
                    Node city = node.childNode(4);
                    String cityName = ((TextNode)(city.childNode(1).childNode(2))).getWholeText();
                    System.out.println(cityName);
                    Node comment = node.childNode(6);
                    String personComment = "";
                    if (comment.childNodeSize()>0) {
                        personComment = ((TextNode) (comment.childNode(0))).getWholeText();
                        System.out.println(personComment);
                    }
                    Node date = node.childNode(8);
                    String signDate = ((TextNode)(date.childNode(0).childNode(0).childNode(0))).getWholeText();
                    System.out.println(signDate);
                }
            }

        } catch (Exception e) {
            System.out.println("Can't parse the html page");
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws Exception{
        Document doc = Jsoup.connect("https://www.petitieonline.com/signatures/suntem_impotriva_oug_13_2017_de_modificare_a_cp/start/20").get();
        Element signatures = doc.select("table#signatures").first();
        List<Node> nodes = signatures.childNode(2).childNodes();
        for (Node node : nodes) {
            if (node instanceof TextNode)
                continue;
            if (node.childNodeSize()==3){
                System.out.println("ignore");
            }else{
                Node name = node.childNode(2);
                System.out.println(((TextNode)(name.childNode(0).childNode(0))).getWholeText());
                Node city = node.childNode(4);
                System.out.println(((TextNode)(city.childNode(1).childNode(2))).getWholeText());
                Node comment = node.childNode(6);
                if (comment.childNodeSize()>0)
                    System.out.println(((TextNode)(comment.childNode(0))).getWholeText());
                Node date = node.childNode(8);
                System.out.println(((TextNode)(date.childNode(0).childNode(0).childNode(0))).getWholeText());
            }
        }
        System.out.println("end");

    }
}
