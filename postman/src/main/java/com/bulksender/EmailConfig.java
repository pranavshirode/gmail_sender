package com.bulksender;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Encapsulates all necessary SMTP and sender configuration details.
 * Handles loading configuration from properties files and provides
 * JavaMail-compatible Properties objects.
 */
public class EmailConfig {
    private static final Logger logger = Logger.getLogger(EmailConfig.class.getName());
    
    // Default Gmail SMTP settings
    private String smtpHost = "smtp.gmail.com";
    private int smtpPort = 587;
    private String senderEmail;
    private String senderPassword; // Should be Gmail App Password, not main password
    private boolean enableTLS = true;
    
    /**
     * Default constructor with default Gmail SMTP settings.
     */
    public EmailConfig() {
        // Default values already set
    }
    
    /**
     * Constructor with explicit values.
     * 
     * @param senderEmail The Gmail address to send from
     * @param senderPassword The Gmail App Password (NOT the main password)
     */
    public EmailConfig(String senderEmail, String senderPassword) {
        this.senderEmail = senderEmail;
        this.senderPassword = senderPassword;
    }
    
    /**
     * Loads configuration from a properties file.
     * Expected properties:
     * - sender.email
     * - sender.password (Gmail App Password)
     * - smtp.host (optional, defaults to smtp.gmail.com)
     * - smtp.port (optional, defaults to 587)
     * - smtp.tls.enable (optional, defaults to true)
     * 
     * @param path Path to the configuration file
     * @return EmailConfig instance with loaded values
     * @throws IOException If the file cannot be read
     */
    public static EmailConfig loadConfig(String path) throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
        }
        
        EmailConfig config = new EmailConfig();
        
        // Required properties
        String email = props.getProperty("sender.email");
        String password = props.getProperty("sender.password");
        
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("sender.email is required in config file");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("sender.password is required in config file");
        }
        
        config.senderEmail = email.trim();
        config.senderPassword = password.trim();
        
        // Optional properties
        String host = props.getProperty("smtp.host");
        if (host != null && !host.trim().isEmpty()) {
            config.smtpHost = host.trim();
        }
        
        String port = props.getProperty("smtp.port");
        if (port != null && !port.trim().isEmpty()) {
            try {
                config.smtpPort = Integer.parseInt(port.trim());
            } catch (NumberFormatException e) {
                logger.warning("Invalid smtp.port value, using default: 587");
            }
        }
        
        String tls = props.getProperty("smtp.tls.enable");
        if (tls != null) {
            config.enableTLS = Boolean.parseBoolean(tls.trim());
        }
        
        logger.info("Configuration loaded successfully from: " + path);
        return config;
    }
    
    /**
     * Returns a configured Properties object suitable for use with JavaMail Session.
     * Configures STARTTLS on port 587 for secure email transmission.
     * 
     * @return Properties object configured for Gmail SMTP
     */
    public Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", String.valueOf(smtpPort));
        props.put("mail.smtp.auth", "true");
        
        if (enableTLS) {
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
        }
        
        // Additional properties for reliability
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");
        
        return props;
    }
    
    // Getters
    public String getSmtpHost() {
        return smtpHost;
    }
    
    public int getSmtpPort() {
        return smtpPort;
    }
    
    public String getSenderEmail() {
        return senderEmail;
    }
    
    public String getSenderPassword() {
        return senderPassword;
    }
    
    public boolean isEnableTLS() {
        return enableTLS;
    }
    
    // Setters
    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }
    
    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }
    
    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }
    
    public void setSenderPassword(String senderPassword) {
        this.senderPassword = senderPassword;
    }
    
    public void setEnableTLS(boolean enableTLS) {
        this.enableTLS = enableTLS;
    }
}




