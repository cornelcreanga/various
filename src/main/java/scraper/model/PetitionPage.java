package scraper.model;

import java.io.Serializable;

public class PetitionPage implements Cloneable, Serializable {

    private int id;
    private int petitionId;
    private int page;

    public PetitionPage() {
    }

    public PetitionPage(int petitionId, int page) {
        this.petitionId = petitionId;
        this.page = page;
    }

    public int getId() {
        return this.id;
    }
    public void setId(int idIn) {
        this.id = idIn;
    }

    public int getPetitionId() {
        return this.petitionId;
    }
    public void setPetitionId(int petitionIdIn) {
        this.petitionId = petitionIdIn;
    }

    public int getPage() {
        return this.page;
    }
    public void setPage(int pageIn) {
        this.page = pageIn;
    }



}

