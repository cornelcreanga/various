package scraper;

import org.apache.commons.lang.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import scraper.dao.NotFoundException;
import scraper.dao.PetitionPageDao;
import scraper.dao.PetitionSignatureDao;
import scraper.dao.SQLRuntimeException;
import scraper.model.Petition;
import scraper.model.PetitionPage;
import scraper.model.PetitionSignature;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.List;

public class ConsumerPetitions implements Runnable {
    public static final char[] DELIMITERS = new char[]{' ', '-'};
    private final String link;
    private int petitionId;
    private int petitionPage;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    public ConsumerPetitions(String link, int petitionId, int petitionPage) {
        this.link = link;
        this.petitionId = petitionId;
        this.petitionPage = petitionPage;
    }

    public void run() {
        PetitionPageDao petitionPageDao = new PetitionPageDao();
        PetitionSignatureDao petitionSignatureDao = new PetitionSignatureDao();
        try(Connection c = DbConnection.get()){
            boolean found = true;
            try {
                petitionPageDao.loadByPetitionAndPage(c,petitionId,petitionPage);
            }catch(NotFoundException e){
                found = false;
            }
            if (found)
                return;

            Document doc = null;
            try {
                System.out.println("Parsing "+link);
                doc = Jsoup.connect(link).get();
                Element signatures = doc.select("table#signatures").first();
                List<Node> nodes = signatures.childNode(2).childNodes();
                for (Node node : nodes) {
                    if (node instanceof TextNode)
                        continue;
                    if (node.childNodeSize()==3){
                        petitionSignatureDao.create(c,new PetitionSignature("ANONIM",null,null,null,petitionId));
                    }else{
                        Node name = node.childNode(2);
                        String personName;
                        if (name.childNode(0).childNode(0) instanceof TextNode)
                            personName = WordUtils.capitalizeFully(removeAccents(((TextNode)(name.childNode(0).childNode(0))).getWholeText().trim()), DELIMITERS);
                        else {
                            try {
                                personName = WordUtils.capitalizeFully(((TextNode) (name.childNode(0).childNode(0).childNode(0))).getWholeText().trim(), DELIMITERS);
                            }catch (Exception e){
                                personName = WordUtils.capitalizeFully(((TextNode) (name.childNode(0).childNode(0).childNode(1))).getWholeText().trim(), DELIMITERS);
                            }
                        }
                        Node city = node.childNode(4);
                        String cityName = ((TextNode)(city.childNode(1).childNode(2))).getWholeText().trim();
                        cityName = WordUtils.capitalizeFully(removeAccents(cityName), DELIMITERS);

                        String personComment = "";
                        String signDate = "";
                        if (node.childNodeSize()==8){
                            Node date = node.childNode(6);
                            signDate = ((TextNode)(date.childNode(0).childNode(0).childNode(0))).getWholeText().trim();
                        }else {

                            Node comment = node.childNode(6);
                            if (comment.childNodeSize() > 0) {
                                personComment = ((TextNode) (comment.childNode(0))).getWholeText().trim();
                            }
                            Node date = node.childNode(8);
                            signDate = ((TextNode) (date.childNode(0).childNode(0).childNode(0))).getWholeText().trim();
                        }

                        petitionSignatureDao.create(c,new PetitionSignature(personName,personComment,cityName,new Timestamp(sdf.parse(signDate).getTime()),petitionId));
                    }
                }

            } catch (Exception e) {
                System.out.println("Error occured during parsing, message is "+e.getMessage());
                System.out.println("Error during parsing link "+link);
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            petitionPageDao.create(c,new PetitionPage(petitionId,petitionPage));
            c.commit();
        } catch (SQLException e) {
            System.out.println("Error occured during execution, message is "+e.getMessage());
            throw new SQLRuntimeException(e);
        }

    }

    public static String removeAccents(String text) {
        return text == null ? null :
                Normalizer.normalize(text, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public static void main(String[] args) throws Exception{
        //https://www.petitieonline.com/signatures/cetatenii_discriminati_de_presedintele_basescu_ii_cer_demisia/start/141000
        String link = "https://www.petitieonline.com/signatures/suntem_impotriva_oug_13_2017_de_modificare_a_cp/start/118470";
        try {
            System.out.println("Parsing "+link);
            Document doc = Jsoup.connect(link).get();
            Element signatures = doc.select("table#signatures").first();
            List<Node> nodes = signatures.childNode(2).childNodes();
            for (Node node : nodes) {
                if (node instanceof TextNode)
                    continue;
                if (node.childNodeSize()==3){
                    System.out.println("anonim");
                }else{

                    Node name = node.childNode(2);
                    //System.out.println(name);
                    String personName;
                    if (name.childNode(0).childNode(0) instanceof TextNode)
                        personName = WordUtils.capitalizeFully(((TextNode)(name.childNode(0).childNode(0))).getWholeText().trim(), DELIMITERS);
                    else {
                        try {
                            personName = WordUtils.capitalizeFully(((TextNode) (name.childNode(0).childNode(0).childNode(0))).getWholeText().trim(), DELIMITERS);
                        }catch (Exception e){
                            personName = WordUtils.capitalizeFully(((TextNode) (name.childNode(0).childNode(0).childNode(1))).getWholeText().trim(), DELIMITERS);
                        }
                    }
                    System.out.println("person:"+personName);
//8 childs
                    Node city = node.childNode(4);
                    String cityName = ((TextNode)(city.childNode(1).childNode(2))).getWholeText().trim();
                    cityName = WordUtils.capitalizeFully(removeAccents(cityName), DELIMITERS);

                    if (node.childNodeSize()==8){
                        Node date = node.childNode(6);
                        String signDate = ((TextNode)(date.childNode(0).childNode(0).childNode(0))).getWholeText().trim();
                        System.out.println(signDate);
                    }else {

                        Node comment = node.childNode(6);
                        String personComment = "";
                        if (comment.childNodeSize() > 0) {
                            personComment = ((TextNode) (comment.childNode(0))).getWholeText().trim();
                        }
                        Node date = node.childNode(8);
                        String signDate = ((TextNode) (date.childNode(0).childNode(0).childNode(0))).getWholeText().trim();
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Can't parse the html page");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
//        Document doc = Jsoup.connect("https://www.petitieonline.com/signatures/suntem_impotriva_oug_13_2017_de_modificare_a_cp/start/20").get();
//        Element signatures = doc.select("table#signatures").first();
//        List<Node> nodes = signatures.childNode(2).childNodes();
//        for (Node node : nodes) {
//            if (node instanceof TextNode)
//                continue;
//            if (node.childNodeSize()==3){
//                System.out.println("ignore");
//            }else{
//                Node name = node.childNode(2);
//                System.out.println(((TextNode)(name.childNode(0).childNode(0))).getWholeText());
//                Node city = node.childNode(4);
//                System.out.println(((TextNode)(city.childNode(1).childNode(2))).getWholeText());
//                Node comment = node.childNode(6);
//                if (comment.childNodeSize()>0)
//                    System.out.println(((TextNode)(comment.childNode(0))).getWholeText());
//                Node date = node.childNode(8);
//                System.out.println(((TextNode)(date.childNode(0).childNode(0).childNode(0))).getWholeText());
//            }
//        }
        System.out.println(removeAccents("București"));
        System.out.println("end");
        System.out.println(WordUtils.capitalizeFully("vincentiu ipate-georgescu", DELIMITERS));

    }
}
