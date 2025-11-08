package com.bulksender;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.logging.Logger;

/**
 * Handles the actual session management and email transmission.
 * Manages JavaMail Session objects and provides methods for sending
 * bulk personalized emails via Gmail SMTP.
 */
public class EmailSender {
    private static final Logger logger = Logger.getLogger(EmailSender.class.getName());
    
    private Session mailSession;
    private EmailConfig config;
    
    /**
     * Default constructor.
     */
    public EmailSender() {
        // Session will be initialized via initialize() method
    }
    
    /**
     * Initializes the Session object using the provided configuration and authenticator.
     * Sets up SMTP authentication with Gmail credentials.
     * 
     * @param config The EmailConfig containing SMTP settings and credentials
     */
    public void initialize(EmailConfig config) {
        this.config = config;
        
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                    config.getSenderEmail(),
                    config.getSenderPassword()
                );
            }
        };
        
        this.mailSession = Session.getInstance(config.getProperties(), authenticator);
        logger.info("Email session initialized for: " + config.getSenderEmail());
    }
    
    /**
     * Sends bulk emails to all recipients in the provided RecipientManager.
     * Each email is sent individually with error handling to prevent one failure
     * from stopping the entire bulk operation.
     * 
     * @param recipients The RecipientManager containing the list of recipients
     * @param subject The email subject line
     * @param body The email body (supports HTML content)
     * @throws IllegalStateException If the session has not been initialized
     */
    public void sendBulkEmail(RecipientManager recipients, String subject, String body) {
        if (mailSession == null || config == null) {
            throw new IllegalStateException("EmailSender must be initialized before sending emails");
        }
        
        List<String> recipientList = recipients.getRecipients();
        
        if (recipientList.isEmpty()) {
            logger.warning("No recipients to send emails to");
            return;
        }
        
        logger.info("Starting bulk email send to " + recipientList.size() + " recipient(s)");
        
        int successCount = 0;
        int failureCount = 0;
        
        for (String recipientEmail : recipientList) {
            try {
                sendEmail(recipientEmail, subject, body);
                successCount++;
                logger.info("✓ Email sent successfully to: " + recipientEmail);
            } catch (MessagingException e) {
                failureCount++;
                logger.severe("✗ Failed to send email to: " + recipientEmail + 
                             " - Error: " + e.getMessage());
            } catch (Exception e) {
                failureCount++;
                logger.severe("✗ Unexpected error sending to: " + recipientEmail + 
                             " - Error: " + e.getMessage());
            }
        }
        
        logger.info("Bulk email operation completed. Success: " + successCount + 
                   ", Failures: " + failureCount);
    }
    
    /**
     * Sends a single email to the specified recipient.
     * 
     * @param recipientEmail The recipient's email address
     * @param subject The email subject line
     * @param body The email body (supports HTML content)
     * @throws MessagingException If there's an error sending the email
     */
    private void sendEmail(String recipientEmail, String subject, String body) 
            throws MessagingException {
        MimeMessage message = new MimeMessage(mailSession);
        
        // Set sender
        message.setFrom(new InternetAddress(config.getSenderEmail()));
        
        // Set recipient
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        
        // Set subject
        message.setSubject(subject);
        
        // Set body with HTML content type
        message.setContent(body, "text/html; charset=utf-8");
        
        // Send the message
        Transport.send(message);
    }
    
    /**
     * Sends a personalized email to a single recipient with custom content.
     * Useful for sending emails with personalized placeholders.
     * 
     * @param recipientEmail The recipient's email address
     * @param subject The email subject line (can contain placeholders)
     * @param body The email body (can contain placeholders for personalization)
     * @throws MessagingException If there's an error sending the email
     */
    public void sendPersonalizedEmail(String recipientEmail, String subject, String body) 
            throws MessagingException {
        sendEmail(recipientEmail, subject, body);
    }
}

