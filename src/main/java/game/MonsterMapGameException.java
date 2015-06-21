package game;

public class MonsterMapGameException extends Exception {
    public MonsterMapGameException(String message) {
        super(message);
    }

    public MonsterMapGameException(String message, Throwable cause) {
        super(message, cause);
    }
}
