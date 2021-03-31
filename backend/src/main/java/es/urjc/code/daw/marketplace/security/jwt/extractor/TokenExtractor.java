package es.urjc.code.daw.marketplace.security.jwt.extractor;

/**
 * A common interface for the token extraction.
 */
public interface TokenExtractor {

    boolean containsToken();

    String extractToken();

}
