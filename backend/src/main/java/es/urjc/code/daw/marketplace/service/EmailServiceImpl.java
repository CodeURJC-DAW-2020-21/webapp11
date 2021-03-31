package es.urjc.code.daw.marketplace.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * A simple implementation for the {@link EmailService}.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(String toAddress, String subject, String message) {
        MimeMessage originalMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessage = new MimeMessageHelper(originalMessage, "utf-8");
        try {
            mimeMessage.setText(message, true);
            mimeMessage.setTo(toAddress);
            mimeMessage.setSubject(subject);
            mimeMessage.setFrom("<" + "dawhostservices@gmail.com" + ">");
            mimeMessage.setReplyTo("<" + "dawhostservices@gmail.com" + ">");

            emailSender.send(originalMessage);

            final String loggerMsg = String.format("[EmailService] Email successfully sent to %s with subject %s", toAddress, subject);
            LOGGER.info(loggerMsg);
        } catch (MessagingException e) {
            final String loggerMsg = String.format("[EmailService] Email sending failed %s", e.getLocalizedMessage());
            LOGGER.error(loggerMsg);
        }
    }

}