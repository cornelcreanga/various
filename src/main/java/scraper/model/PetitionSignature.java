package scraper.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class PetitionSignature implements Cloneable, Serializable {

    private int id;
    private String name;
    private String comment;
    private String city;
    private Timestamp signDate;
    private int petitionId;

    public PetitionSignature() {
    }

    public PetitionSignature(String name, String comment, String city, Timestamp signDate, int petitionId) {
        this.name = name;
        this.comment = comment;
        this.city = city;
        this.signDate = signDate;
        this.petitionId = petitionId;
    }

    public int getId() {
        return this.id;
    }
    public void setId(int idIn) {
        this.id = idIn;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String nameIn) {
        this.name = nameIn;
    }

    public String getComment() {
        return this.comment;
    }
    public void setComment(String commentIn) {
        this.comment = commentIn;
    }

    public String getCity() {
        return this.city;
    }
    public void setCity(String cityIn) {
        this.city = cityIn;
    }

    public Timestamp getSignDate() {
        return this.signDate;
    }
    public void setSignDate(Timestamp signDateIn) {
        this.signDate = signDateIn;
    }

    public int getPetitionId() {
        return this.petitionId;
    }
    public void setPetitionId(int petitionIdIn) {
        this.petitionId = petitionIdIn;
    }

}
