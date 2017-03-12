package scraper.model;

import java.io.Serializable;

public class Donation  implements Cloneable, Serializable {

    private int id;
    private String name;
    private int amount;
    private int campaignId;

    public Donation() {
    }

    public Donation(String name, int amount, int campaignId) {
        this.name = name;
        this.amount = amount;
        this.campaignId = campaignId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }
}

