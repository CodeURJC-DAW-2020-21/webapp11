package es.urjc.code.daw.marketplace.security.user;

import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This class is the implementation of the {@link UserDetailsService}
 * to be able to load the {@link UserDetails} associated to the logged
 * user.
 */
@Service
@Qualifier("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findUserByEmail(email);
        if(user == null) {
            final String message = String.format("[UserDetailsService] No user found with email '%s'", email);
            LOGGER.info(message);
            throw new UsernameNotFoundException(message);
        } else {
            final String message = String.format("[UserDetailsService] Found user with email '%s'", email);
            LOGGER.info(message);
            return new UserPrincipal(user);
        }
    }

}