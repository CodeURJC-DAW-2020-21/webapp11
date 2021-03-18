package es.urjc.code.daw.marketplace.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    private final EmailProperties emailProperties;

    public EmailConfig(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    /**
     * A bean that represents the configured java mail sender.
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(emailProperties.getHost());
        mailSender.setPort(emailProperties.getPort());
        mailSender.setUsername(emailProperties.getEmail());
        mailSender.setPassword(emailProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.debug", "false");

        return mailSender;
    }

}