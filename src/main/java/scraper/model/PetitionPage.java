package scraper.model;

import java.io.Serializable;

public class PetitionPage implements Cloneable, Serializable {

    /**
     * Persistent Instance variables. This data is directly
     * mapped to the columns of database table.
     */
    private int id;
    private int petitionId;
    private int page;



    /**
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public PetitionPage () {

    }

    public PetitionPage (int idIn) {

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



    /**
     * setAll allows to set all persistent variables in one method call.
     * This is useful, when all data is available and it is needed to
     * set the initial state of this object. Note that this method will
     * directly modify instance variales, without going trough the
     * individual set-methods.
     */

    public void setAll(int idIn,
                       int petitionIdIn,
                       int pageIn) {
        this.id = idIn;
        this.petitionId = petitionIdIn;
        this.page = pageIn;
    }


    /**
     * hasEqualMapping-method will compare two PetitionPage instances
     * and return true if they contain same values in all persistent instance
     * variables. If hasEqualMapping returns true, it does not mean the objects
     * are the same instance. However it does mean that in that moment, they
     * are mapped to the same row in database.
     */
    public boolean hasEqualMapping(PetitionPage valueObject) {

        if (valueObject.getId() != this.id) {
            return(false);
        }
        if (valueObject.getPetitionId() != this.petitionId) {
            return(false);
        }
        if (valueObject.getPage() != this.page) {
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
        out.append("\nclass PetitionPage, mapping to table petition_page\n");
        out.append("Persistent attributes: \n");
        out.append("id = " + this.id + "\n");
        out.append("petitionId = " + this.petitionId + "\n");
        out.append("page = " + this.page + "\n");
        return out.toString();
    }


    /**
     * Clone will return identical deep copy of this valueObject.
     * Note, that this method is different than the clone() which
     * is defined in java.lang.Object. Here, the retuned cloned object
     * will also have all its attributes cloned.
     */
    public Object clone() {
        PetitionPage cloned = new PetitionPage();

        cloned.setId(this.id);
        cloned.setPetitionId(this.petitionId);
        cloned.setPage(this.page);
        return cloned;
    }



}

