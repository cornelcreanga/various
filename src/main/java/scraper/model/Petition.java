package scraper.model;

import java.io.Serializable;

public class Petition implements Cloneable, Serializable {

    /**
     * Persistent Instance variables. This data is directly
     * mapped to the columns of database table.
     */
    private int id;
    private String name;
    private String link;



    /**
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public Petition () {

    }

    public Petition (int idIn) {

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

    public String getLink() {
        return this.link;
    }
    public void setLink(String linkIn) {
        this.link = linkIn;
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
                       String linkIn) {
        this.id = idIn;
        this.name = nameIn;
        this.link = linkIn;
    }


    /**
     * hasEqualMapping-method will compare two Petition instances
     * and return true if they contain same values in all persistent instance
     * variables. If hasEqualMapping returns true, it does not mean the objects
     * are the same instance. However it does mean that in that moment, they
     * are mapped to the same row in database.
     */
    public boolean hasEqualMapping(Petition valueObject) {

        if (valueObject.getId() != this.id) {
            return(false);
        }
        if (this.name == null) {
            if (valueObject.getName() != null)
                return(false);
        } else if (!this.name.equals(valueObject.getName())) {
            return(false);
        }
        if (this.link == null) {
            if (valueObject.getLink() != null)
                return(false);
        } else if (!this.link.equals(valueObject.getLink())) {
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
        out.append("\nclass Petition, mapping to table petition\n");
        out.append("Persistent attributes: \n");
        out.append("id = " + this.id + "\n");
        out.append("name = " + this.name + "\n");
        out.append("link = " + this.link + "\n");
        return out.toString();
    }


    /**
     * Clone will return identical deep copy of this valueObject.
     * Note, that this method is different than the clone() which
     * is defined in java.lang.Object. Here, the retuned cloned object
     * will also have all its attributes cloned.
     */
    public Object clone() {
        Petition cloned = new Petition();

        cloned.setId(this.id);
        if (this.name != null)
            cloned.setName(new String(this.name));
        if (this.link != null)
            cloned.setLink(new String(this.link));
        return cloned;
    }


}
