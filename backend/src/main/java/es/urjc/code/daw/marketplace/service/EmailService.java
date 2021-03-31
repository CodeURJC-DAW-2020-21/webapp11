package es.urjc.code.daw.marketplace.service;

public interface EmailService {

     /**
      * Sends an email to the specified address given the subject and html message.
      *
      * @param toAddress The destination address to whom send the email
      * @param subject The topic what is the mail about
      * @param message The html message
      */
     void sendEmail(String toAddress, String subject, String message);

}

