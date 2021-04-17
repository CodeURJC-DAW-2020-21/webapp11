package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.User;

/**
 * An common interface to represent authentication.
 */
public interface AuthenticationService {

    /**
     * Performs the authentication with the given details.
     *
     * @param principal Represents the username (or email)
     * @param credentials Represents the password
     *
     * @throws RuntimeException if the authentication fails
     */
    void authenticate(Object principal, Object credentials) throws RuntimeException;

    /**
     * Returns the currently logged in user with token.
     *
     * @throws RuntimeException if the authentication fails
     */
    User getTokenUser();

}
