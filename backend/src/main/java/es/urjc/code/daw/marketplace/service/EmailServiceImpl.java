package es.urjc.code.daw.marketplace.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(String toAddress, String subject, String message) {
        try {
            MimeMessage originalMessage = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessage = new MimeMessageHelper(originalMessage, "utf-8");
            mimeMessage.setText(message, true);
            mimeMessage.setTo(toAddress);
            mimeMessage.setSubject(subject);
            mimeMessage.setFrom("<" + "dawhostservices@gmail.com" + ">");
            mimeMessage.setReplyTo("<" + "dawhostservices@gmail.com" + ">");
            emailSender.send(originalMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}