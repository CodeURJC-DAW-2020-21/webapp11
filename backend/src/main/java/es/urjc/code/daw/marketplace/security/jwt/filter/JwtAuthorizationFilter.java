package es.urjc.code.daw.marketplace.security.jwt.filter;

import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.jwt.JwtTokenService;
import es.urjc.code.daw.marketplace.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Token ";

    private final JwtTokenService tokenService;
    private final UserService userService;

    public JwtAuthorizationFilter(JwtTokenService tokenService,
                                  UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    @Transactional
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // If the request method is options send ok status
        if(RequestMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(OK.value());
            filterChain.doFilter(request, response);
            return;
        }

        // If user is already authenticated
        if(SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        boolean authHeaderIsPresent = authHeader != null;

        // If the authorization header is not present
        if(!authHeaderIsPresent) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        boolean authHeaderMeetsFormat = authHeader.startsWith(TOKEN_PREFIX);

        // If token does not start with the required prefix
        if(!authHeaderMeetsFormat) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(TOKEN_PREFIX.length());

        // If the token is empty or blank
        if(StringUtils.isEmpty(token) || StringUtils.isBlank(token)) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        // If the token is not valid
        if(!tokenService.isTokenValid(token)) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        // Try to extract the subject from the token
        String tokenUserId;
        try {
            tokenUserId = tokenService.extractTokenSubject(token);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        // Try to parse the token subject
        long userId;
        try {
            userId = Long.parseLong(tokenUserId);
        } catch(Exception e) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        // If subject user does not exist
        if(!userService.existsUserById(userId)) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        // Find the user and its associated authorities
        User user = userService.findUserById(userId);

        // Map the authorities to a spring known format
        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        // Create spring authentication and set it
        var userPasswordAuthToken = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(userPasswordAuthToken);
        filterChain.doFilter(request, response);

    }

}