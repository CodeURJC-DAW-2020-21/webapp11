package es.urjc.code.daw.marketplace.security.jwt;

/**
 * An interface for creating, validating and generating
 * json web tokens.
 */
public interface JwtTokenService {

    /**
     * Checks whether the provided token is valid.
     *
     * <p>A token is valid when the token is not empty,
     * is verifiable and is not expired.
     *
     * @param token the given token
     *
     * @return if the provided token is valid
     */
    boolean isTokenValid(String token);

    /**
     * Extracts the subject from the token.
     *
     * <p>Normally a token contains some kind of
     * user identifier but not sensible information,
     * this method extracts that identifier from
     * the given token.
     *
     * @param token the given token
     *
     * @return the extracted subject to
     *         whom the token belongs
     */
    String extractTokenSubject(String token);

    /**
     * Generates a token for the given subject.
     *
     * <p>Generates a token by embedding the
     * issuer and enterprise information and
     * also the provided subject.
     *
     * @param subject the identifier of the owner
     *
     * @return a json web token for a subject
     */
    String generateTokenFor(String subject);

}
