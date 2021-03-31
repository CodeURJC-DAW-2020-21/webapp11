package es.urjc.code.daw.marketplace.security.jwt.extractor;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class TokenExtractorImpl implements TokenExtractor {

    private static final String TOKEN_PREFIX = "Token ";

    @Override
    public boolean containsToken() {

        // Check if the request is present
        boolean hasServletRequest = getCurrentHttpRequest().isPresent();
        if(!hasServletRequest) return false;

        HttpServletRequest request = getCurrentHttpRequest().get();
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Validate that the request authorization header is present
        boolean authHeaderIsPresent = authorizationHeader != null;
        if(!authHeaderIsPresent) return false;

        // Verify that the authentication header content meets required token format
        boolean authHeaderMeetsFormat = authorizationHeader.startsWith(TOKEN_PREFIX);
        if(!authHeaderMeetsFormat) return false;

        // Ensure the provided token is not empty
        String token = authorizationHeader.substring(TOKEN_PREFIX.length());
        boolean isTokenEmpty = StringUtils.isEmpty(token) || StringUtils.isBlank(token);

        return !isTokenEmpty;
    }

    @Override
    public String extractToken() {

        // Ensure the token exists

        if(!containsToken()) return Strings.EMPTY;

        // Extract the token
        HttpServletRequest request = getCurrentHttpRequest().get();
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring(TOKEN_PREFIX.length());

        return token;
    }

    /**
     * Returns the current web server request.
     *
     * @return an instance of the current {@code HttpServletRequest}
     */
    private static Optional<HttpServletRequest> getCurrentHttpRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(requestAttributes -> ServletRequestAttributes.class.isAssignableFrom(requestAttributes.getClass()))
                .map(requestAttributes -> ((ServletRequestAttributes) requestAttributes))
                .map(ServletRequestAttributes::getRequest);
    }

}
