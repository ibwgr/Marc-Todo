package ch.ibw.appl.tudu.server.shared.model;

public class ValidationError extends RuntimeException {
    public ValidationError(String message) {
        super(message);
    }
}
