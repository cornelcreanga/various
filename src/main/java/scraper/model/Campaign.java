package scraper.model;

import java.io.Serializable;

public class Campaign implements Cloneable, Serializable {

    private int id;
    private String name;
    private String link;

    public Campaign() {
    }

    public Campaign(String name, String link) {
        this.name = name;
        this.link = link;
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

    public String getLink() {
        return this.link;
    }
    public void setLink(String linkIn) {
        this.link = linkIn;
    }
}
