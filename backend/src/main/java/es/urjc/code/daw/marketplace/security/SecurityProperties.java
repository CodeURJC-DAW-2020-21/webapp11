package es.urjc.code.daw.marketplace.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("security")
@PropertySource("classpath:security.properties")
public class SecurityProperties {

    @Value("${routes.logged}")
    private String[] loggedRoutes;

    @Value("${auth.route.login}")
    private String loginRoute;

    @Value("${auth.route.logout}")
    private String logoutRoute;

    @Value("${auth.route.logout_redirect}")
    private String logoutRedirectRoute;

    @Value("${auth.route.successful}")
    private String loginSuccessfulRoute;

    @Value("${auth.route.failure}")
    private String loginFailedRoute;

    @Value("${auth.form.parameter.username}")
    private String loginUsernameParameter;

    @Value("${auth.form.parameter.password}")
    private String loginPasswordParameter;

    @Value("${auth.form.messages.failed}")
    private String loginFailureMessage;

    @Value("${auth.account.admin.email}")
    private String adminEmail;

    @Value("${auth.account.admin.password}")
    private String adminPassword;

}
