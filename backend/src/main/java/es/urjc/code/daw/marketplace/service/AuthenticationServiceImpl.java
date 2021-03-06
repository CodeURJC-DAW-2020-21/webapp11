package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.jwt.JwtTokenService;
import es.urjc.code.daw.marketplace.security.jwt.extractor.TokenExtractor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenService tokenService;
    private final TokenExtractor tokenExtractor;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     UserService userService,
                                     JwtTokenService tokenService,
                                     TokenExtractor tokenExtractor) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    @SneakyThrows
    public void authenticate(Object principal, Object credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(principal, credentials)
        );
    }

    @Override
    public User getTokenUser() {
        String token = tokenExtractor.containsToken() ? tokenExtractor.extractToken() : StringUtils.EMPTY;
        String email = tokenService.extractTokenSubject(token);
        return userService.findUserByEmail(email);
    }

}
