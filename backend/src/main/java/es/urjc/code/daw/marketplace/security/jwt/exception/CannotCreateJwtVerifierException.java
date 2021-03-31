package es.urjc.code.daw.marketplace.security.jwt.exception;

public class CannotCreateJwtVerifierException extends RuntimeException {

    public CannotCreateJwtVerifierException(Throwable cause) {
        super("Can't create a token verifier with the provided details", cause);
    }

}
