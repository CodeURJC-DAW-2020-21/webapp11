package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.jwt.JwtTokenService;
import es.urjc.code.daw.marketplace.security.jwt.extractor.TokenExtractor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@Service
public class UserTokenAuthorizationServiceImpl implements UserTokenAuthorizationService {

    private static final String OPERATOR_ROLE_NAME = "ROLE_ADMIN";

    private final JwtTokenService tokenService;
    private final TokenExtractor tokenExtractor;
    private final UserService userService;

    public UserTokenAuthorizationServiceImpl(JwtTokenService tokenService,
                                        TokenExtractor tokenExtractor,
                                        UserService userService) {
        this.tokenService = tokenService;
        this.tokenExtractor = tokenExtractor;
        this.userService = userService;
    }

    @Override
    public boolean requesterCanManipulateUser(Long userId) {
        String modifiedEmail = extractTokenEmail();
        User modifierUser = userService.findUserByEmail(modifiedEmail);
        if(modifierUser.getId().longValue() == userId.longValue()) return true;
        return modifierUser.isAdmin();
    }

    @Override
    public boolean requesterIsOperator() {
        String modifiedEmail = extractTokenEmail();
        User modifierUser = userService.findUserByEmail(modifiedEmail);
        return modifierUser.isAdmin();
    }

    private String extractTokenEmail() {
        String extractedToken = tokenExtractor.containsToken() ? tokenExtractor.extractToken() : Strings.EMPTY;
        return tokenService.extractTokenSubject(extractedToken);
    }

}
