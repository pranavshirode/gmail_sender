package com.bulksender;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main entry point for the Gmail Bulk Sender Application.
 * Orchestrates the application flow: loading configuration, loading recipients,
 * initializing the email sender, and sending bulk emails.
 */
public class BulkSenderApp {
    private static final Logger logger = Logger.getLogger(BulkSenderApp.class.getName());
    
    // Default file paths
    private static final String DEFAULT_CONFIG_PATH = "config.properties";
    private static final String DEFAULT_RECIPIENTS_PATH = "recipients.txt";
    
    /**
     * Main method to run the bulk email sender application.
     * 
     * @param args Command-line arguments:
     *             --gui or -g: Launch GUI mode
     *             [0] - Optional: path to config file (default: config.properties)
     *             [1] - Optional: path to recipients file (default: recipients.txt)
     */
    public static void main(String[] args) {
        // Check for GUI mode
        if (args.length > 0 && (args[0].equals("--gui") || args[0].equals("-g"))) {
            launchGUI();
            return;
        }
        
        printInstructions();
        
        // Determine file paths from arguments or use defaults
        String configPath = args.length > 0 ? args[0] : DEFAULT_CONFIG_PATH;
        String recipientsPath = args.length > 1 ? args[1] : DEFAULT_RECIPIENTS_PATH;
        
        try {
            // Step 1: Load configuration
            logger.info("Loading configuration from: " + configPath);
            EmailConfig config = EmailConfig.loadConfig(configPath);
            
            // Step 2: Load recipients
            logger.info("Loading recipients from: " + recipientsPath);
            RecipientManager recipientManager = new RecipientManager();
            recipientManager.loadRecipientsFromFile(recipientsPath);
            
            if (recipientManager.getRecipientCount() == 0) {
                logger.severe("No recipients loaded. Exiting.");
                return;
            }
            
            // Step 3: Initialize email sender
            logger.info("Initializing email sender...");
            EmailSender emailSender = new EmailSender();
            emailSender.initialize(config);
            
            // Step 4: Define email content
            String subject = "Test Email from Gmail Bulk Sender";
            String body = buildEmailBody();
            
            // Step 5: Send bulk emails
            logger.info("Starting bulk email send operation...");
            emailSender.sendBulkEmail(recipientManager, subject, body);
            
            logger.info("Application completed successfully.");
            
        } catch (IOException e) {
            logger.severe("File I/O error: " + e.getMessage());
            logger.severe("Please ensure the configuration and recipient files exist and are readable.");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            logger.severe("Configuration error: " + e.getMessage());
            logger.severe("Please check your configuration file format.");
            System.exit(1);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            System.exit(1);
        }
    }
    
    /**
     * Prints instructions for setting up and using the application.
     */
    private static void printInstructions() {
        System.out.println("==========================================");
        System.out.println("  Gmail Bulk Sender Application");
        System.out.println("==========================================");
        System.out.println();
        System.out.println("REQUIRED FILES:");
        System.out.println("  1. config.properties - Contains Gmail SMTP configuration");
        System.out.println("     Required properties:");
        System.out.println("       - sender.email=your-email@gmail.com");
        System.out.println("       - sender.password=your-app-password");
        System.out.println();
        System.out.println("  2. recipients.txt - Contains recipient email addresses");
        System.out.println("     Format: One email address per line");
        System.out.println();
        System.out.println("IMPORTANT SECURITY NOTES:");
        System.out.println("  - Use Gmail App Password, NOT your main Gmail password");
        System.out.println("  - To generate an App Password:");
        System.out.println("    1. Go to Google Account settings");
        System.out.println("    2. Security > 2-Step Verification > App passwords");
        System.out.println("    3. Generate a new app password for 'Mail'");
        System.out.println();
        System.out.println("USAGE:");
        System.out.println("  java -jar gmail-bulk-sender.jar [config-file] [recipients-file]");
        System.out.println("  Default: config.properties recipients.txt");
        System.out.println();
        System.out.println("==========================================");
        System.out.println();
    }
    
    /**
     * Launches the GUI version of the application.
     */
    private static void launchGUI() {
        try {
            // Set system look and feel for better appearance
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system L&F fails
        }
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            BulkSenderGUI gui = new BulkSenderGUI();
            gui.setVisible(true);
        });
    }
    
    /**
     * Builds the HTML email body content.
     * This is a sample template that can be customized.
     * 
     * @return HTML-formatted email body
     */
    private static String buildEmailBody() {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<meta charset='UTF-8'>" +
               "<style>" +
               "  body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
               "  .container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
               "  .header { background-color: #4285f4; color: white; padding: 20px; text-align: center; }" +
               "  .content { padding: 20px; background-color: #f9f9f9; }" +
               "  .footer { text-align: center; padding: 10px; font-size: 12px; color: #666; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='container'>" +
               "<div class='header'>" +
               "<h1>Hello from Gmail Bulk Sender</h1>" +
               "</div>" +
               "<div class='content'>" +
               "<p>This is a test email sent using the Gmail Bulk Sender application.</p>" +
               "<p>The application uses JavaMail API with STARTTLS encryption for secure transmission.</p>" +
               "<p>You can customize this email template to include personalized content.</p>" +
               "</div>" +
               "<div class='footer'>" +
               "<p>This email was sent automatically. Please do not reply.</p>" +
               "</div>" +
               "</div>" +
               "</body>" +
               "</html>";
    }
}

