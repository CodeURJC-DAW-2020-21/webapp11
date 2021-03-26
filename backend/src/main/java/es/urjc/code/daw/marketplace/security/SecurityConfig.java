package es.urjc.code.daw.marketplace.security;

import es.urjc.code.daw.marketplace.security.handlers.LoginFailureHandler;
import es.urjc.code.daw.marketplace.security.jwt.filter.JwtAccessDeniedHandler;
import es.urjc.code.daw.marketplace.security.jwt.filter.JwtAuthenticationEntryPoint;
import es.urjc.code.daw.marketplace.security.jwt.filter.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This class's responsibility is to perform the default
 * configuration of default properties for our web security
 * (i.e. public routes, private routes, http/https, etc).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final SecurityProperties properties;
    private final LoginFailureHandler loginFailureHandler;

    public SecurityConfig(JwtAuthorizationFilter jwtAuthorizationFilter,
                          JwtAccessDeniedHandler jwtAccessDeniedHandler,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          SecurityProperties properties,
                          LoginFailureHandler loginFailureHandler) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.properties = properties;
        this.loginFailureHandler = loginFailureHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requiresChannel().anyRequest().requiresSecure();

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

        http.exceptionHandling()
            .accessDeniedHandler(jwtAccessDeniedHandler)
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
