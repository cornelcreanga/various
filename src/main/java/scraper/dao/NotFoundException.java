package scraper.dao;

public class NotFoundException extends RuntimeException {

    /**
     * Constructor for NotFoundException. The input message is
     * returned in toString() message.
     */
    public NotFoundException(String msg) {
        super(msg);
    }

}
