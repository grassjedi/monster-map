package graph;

public class NoSuchPathException extends GraphException {
    public NoSuchPathException(String message) {
        super(message);
    }

    public NoSuchPathException(String message, Throwable cause) {
        super(message, cause);
    }
}
