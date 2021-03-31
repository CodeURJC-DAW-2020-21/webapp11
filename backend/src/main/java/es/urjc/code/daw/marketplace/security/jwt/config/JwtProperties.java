package es.urjc.code.daw.marketplace.security.jwt.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * A simple properties class that contains all the json web
 * tokens information (i.e. secret, issuer, expiration time).
 */
@Component
@Data
@ConfigurationProperties("jsonwebtoken")
@PropertySource("classpath:jsonwebtoken.properties")
public class JwtProperties {

    @Value("${jsonwebtoken.secret}")
    private String secret;

    @Value("${jsonwebtoken.issuer}")
    private String issuer;

    @Value("${jsonwebtoken.audience}")
    private String audience;

    @Value("${jsonwebtoken.expiration_millis}")
    private int expirationMillis;

}
