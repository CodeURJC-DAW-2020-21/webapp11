package es.urjc.code.daw.marketplace.security.handlers;

import es.urjc.code.daw.marketplace.security.SecurityProperties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * This class is responsible of intercepting login attempts
 * and when any of them fails adds the flash error message
 * to the current request.
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final SecurityProperties securityProperties;

    public LoginFailureHandler(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        if(!Objects.isNull(exception)) {
            SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
            FlashMap flashMap = new FlashMap();
            flashMap.put("error", securityProperties.getLoginFailureMessage());
            flashMapManager.saveOutputFlashMap(flashMap, request, response);
        }
        response.sendRedirect(request.getHeader("referer"));
    }

}
