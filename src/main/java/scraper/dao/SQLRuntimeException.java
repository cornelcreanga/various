package scraper.dao;

public class SQLRuntimeException extends RuntimeException {

    public SQLRuntimeException(Throwable cause) {
        super(cause);
    }

    public SQLRuntimeException(String message) {
        super(message);
    }
}
