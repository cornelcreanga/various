package scraper.model;

import java.io.Serializable;

public class PetitionSignature implements Cloneable, Serializable {

    /**
     * Persistent Instance variables. This data is directly
     * mapped to the columns of database table.
     */
    private int id;
    private String name;
    private String comment;
    private String city;
    private java.sql.Date signDate;
    private int petitionId;



    /**
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public PetitionSignature () {

    }

    public PetitionSignature (int idIn) {

        this.id = idIn;

    }


    /**
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

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

    public java.sql.Date getSignDate() {
        return this.signDate;
    }
    public void setSignDate(java.sql.Date signDateIn) {
        this.signDate = signDateIn;
    }

    public int getPetitionId() {
        return this.petitionId;
    }
    public void setPetitionId(int petitionIdIn) {
        this.petitionId = petitionIdIn;
    }



    /**
     * setAll allows to set all persistent variables in one method call.
     * This is useful, when all data is available and it is needed to
     * set the initial state of this object. Note that this method will
     * directly modify instance variales, without going trough the
     * individual set-methods.
     */

    public void setAll(int idIn,
                       String nameIn,
                       String commentIn,
                       String cityIn,
                       java.sql.Date signDateIn,
                       int petitionIdIn) {
        this.id = idIn;
        this.name = nameIn;
        this.comment = commentIn;
        this.city = cityIn;
        this.signDate = signDateIn;
        this.petitionId = petitionIdIn;
    }


    /**
     * hasEqualMapping-method will compare two PetitionSignature instances
     * and return true if they contain same values in all persistent instance
     * variables. If hasEqualMapping returns true, it does not mean the objects
     * are the same instance. However it does mean that in that moment, they
     * are mapped to the same row in database.
     */
    public boolean hasEqualMapping(PetitionSignature valueObject) {

        if (valueObject.getId() != this.id) {
            return(false);
        }
        if (this.name == null) {
            if (valueObject.getName() != null)
                return(false);
        } else if (!this.name.equals(valueObject.getName())) {
            return(false);
        }
        if (this.comment == null) {
            if (valueObject.getComment() != null)
                return(false);
        } else if (!this.comment.equals(valueObject.getComment())) {
            return(false);
        }
        if (this.city == null) {
            if (valueObject.getCity() != null)
                return(false);
        } else if (!this.city.equals(valueObject.getCity())) {
            return(false);
        }
        if (this.signDate == null) {
            if (valueObject.getSignDate() != null)
                return(false);
        } else if (!this.signDate.equals(valueObject.getSignDate())) {
            return(false);
        }
        if (valueObject.getPetitionId() != this.petitionId) {
            return(false);
        }

        return true;
    }



    /**
     * toString will return String object representing the state of this
     * valueObject. This is useful during application development, and
     * possibly when application is writing object states in textlog.
     */
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("\nclass PetitionSignature, mapping to table petition_signature\n");
        out.append("Persistent attributes: \n");
        out.append("id = " + this.id + "\n");
        out.append("name = " + this.name + "\n");
        out.append("comment = " + this.comment + "\n");
        out.append("city = " + this.city + "\n");
        out.append("signDate = " + this.signDate + "\n");
        out.append("petitionId = " + this.petitionId + "\n");
        return out.toString();
    }


    /**
     * Clone will return identical deep copy of this valueObject.
     * Note, that this method is different than the clone() which
     * is defined in java.lang.Object. Here, the retuned cloned object
     * will also have all its attributes cloned.
     */
    public Object clone() {
        PetitionSignature cloned = new PetitionSignature();

        cloned.setId(this.id);
        if (this.name != null)
            cloned.setName(new String(this.name));
        if (this.comment != null)
            cloned.setComment(new String(this.comment));
        if (this.city != null)
            cloned.setCity(new String(this.city));
        if (this.signDate != null)
            cloned.setSignDate((java.sql.Date)this.signDate.clone());
        cloned.setPetitionId(this.petitionId);
        return cloned;
    }


}
