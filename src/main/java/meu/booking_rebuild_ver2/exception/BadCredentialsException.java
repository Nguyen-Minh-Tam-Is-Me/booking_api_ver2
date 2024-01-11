package meu.booking_rebuild_ver2.exception;

public class BadCredentialsException extends RuntimeException {

    private static final long serialVersionUID = -349287396200850517L;

    public BadCredentialsException() {
        super("Bad Credentials.");
    }

    public BadCredentialsException(String message) {
        super("Bad Credentials: " + message);
    }

}
