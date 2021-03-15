package es.urjc.code.daw.marketplace.security;

import es.urjc.code.daw.marketplace.security.handlers.LoginFailureHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityProperties properties;
    private final LoginFailureHandler loginFailureHandler;

    public SecurityConfig(SecurityProperties properties,
                          LoginFailureHandler loginFailureHandler) {
        this.properties = properties;
        this.loginFailureHandler = loginFailureHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(properties.getLoggedRoutes()).authenticated();

        http.formLogin().loginPage(properties.getLoginRoute());
        http.formLogin().usernameParameter(properties.getLoginUsernameParameter());
        http.formLogin().passwordParameter(properties.getLoginPasswordParameter());

        http.formLogin().failureHandler(loginFailureHandler);

        http.logout().logoutUrl(properties.getLogoutRoute());
        http.logout().logoutSuccessUrl(properties.getLogoutRedirectRoute());

        http.headers().frameOptions().disable();
        http.headers().frameOptions().sameOrigin();
        http.csrf().disable();
    }

}
