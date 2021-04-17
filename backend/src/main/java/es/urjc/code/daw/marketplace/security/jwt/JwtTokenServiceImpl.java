package es.urjc.code.daw.marketplace.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import es.urjc.code.daw.marketplace.security.jwt.config.JwtProperties;
import es.urjc.code.daw.marketplace.security.jwt.exception.CannotCreateJwtVerifierException;
import es.urjc.code.daw.marketplace.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtProperties properties;
    private final JWTVerifier tokenVerifier;

    public JwtTokenServiceImpl(JwtProperties properties) {
        this.properties = properties;
        this.tokenVerifier = getJWTVerifier();
    }

    private JWTVerifier getJWTVerifier() {
        try {
            Algorithm algorithm = HMAC512(properties.getSecret());
            return JWT.require(algorithm).withIssuer(properties.getIssuer()).build();
        } catch (JWTVerificationException exception) {
            throw new CannotCreateJwtVerifierException(exception);
        }
    }

    @Override
    public String generateTokenFor(String subject) {
        try {
            return JWT.create()
                    .withIssuer(properties.getIssuer())
                    .withAudience(properties.getAudience())
                    .withIssuedAt(TimeUtils.now())
                    .withSubject(subject)
                    .withExpiresAt(TimeUtils.sumsMillisToDate(TimeUtils.now(), properties.getExpirationMillis()))
                    .sign(HMAC512(properties.getSecret().getBytes()));
        } catch(Exception exception) {
            return StringUtils.EMPTY;
        }
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            Date tokenExpiration = tokenVerifier.verify(token).getExpiresAt();
            return tokenExpiration.after(TimeUtils.now());
        } catch(Exception exception) {
            return false;
        }
    }

    @Override
    public String extractTokenSubject(String token) {
        try {
            return tokenVerifier.verify(token).getSubject();
        } catch(Exception exception) {
            return Strings.EMPTY;
        }
    }

}
