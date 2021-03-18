package es.urjc.code.daw.marketplace.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("mailing")
@PropertySource("classpath:mailing.properties")
public class EmailProperties {

    @Value("${mailing.host}")
    private String host;

    @Value("${mailing.port}")
    private Integer port;

    @Value("${mailing.email}")
    private String email;

    @Value("${mailing.email}")
    private String password;

}
