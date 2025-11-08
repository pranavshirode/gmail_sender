package com.bulksender;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import jakarta.mail.MessagingException;

/**
 * Graphical User Interface for the Gmail Bulk Sender Application.
 * Provides an intuitive interface for configuring, composing, and sending bulk emails.
 */
public class BulkSenderGUI extends JFrame {
    
    // Configuration fields
    private JTextField smtpHostField;
    private JTextField smtpPortField;
    private JTextField senderEmailField;
    private JPasswordField senderPasswordField;
    private JCheckBox enableTLSBox;
    
    // Recipients fields
    private JTextArea recipientsArea;
    private JLabel recipientCountLabel;
    private JButton loadRecipientsButton;
    
    // Email composition fields
    private JTextField subjectField;
    private JTextArea bodyArea;
    private JCheckBox htmlModeBox;
    
    // Control buttons
    private JButton sendButton;
    private JButton clearButton;
    private JButton testConnectionButton;
    
    // Status area
    private JTextArea statusArea;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    
    // Application state
    private EmailConfig config;
    private RecipientManager recipientManager;
    private EmailSender emailSender;
    private boolean isSending = false;
    
    /**
     * Creates and displays the GUI.
     */
    public BulkSenderGUI() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setDefaultValues();
    }
    
    /**
     * Initializes all GUI components.
     */
    private void initializeComponents() {
        // Configuration panel components
        smtpHostField = new JTextField("smtp.gmail.com", 20);
        smtpPortField = new JTextField("587", 10);
        senderEmailField = new JTextField(25);
        senderPasswordField = new JPasswordField(25);
        enableTLSBox = new JCheckBox("Enable TLS", true);
        
        // Recipients panel components
        recipientsArea = new JTextArea(8, 30);
        recipientsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        recipientsArea.setLineWrap(true);
        recipientsArea.setWrapStyleWord(true);
        recipientCountLabel = new JLabel("Recipients: 0");
        loadRecipientsButton = new JButton("Load from File");
        
        // Email composition components
        subjectField = new JTextField(40);
        bodyArea = new JTextArea(12, 40);
        bodyArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        htmlModeBox = new JCheckBox("HTML Mode", true);
        
        // Control buttons
        sendButton = new JButton("Send Bulk Emails");
        sendButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        sendButton.setBackground(new Color(66, 133, 244));
        sendButton.setForeground(Color.WHITE);
        clearButton = new JButton("Clear All");
        testConnectionButton = new JButton("Test Connection");
        
        // Status area components
        statusArea = new JTextArea(10, 50);
        statusArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        statusArea.setEditable(false);
        statusArea.setBackground(new Color(245, 245, 245));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Ready");
        statusLabel = new JLabel("Status: Ready");
    }
    
    /**
     * Sets up the layout of all components.
     */
    private void setupLayout() {
        setTitle("Gmail Bulk Sender - GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Main container with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Left panel: Configuration and Recipients
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        
        // Configuration panel
        JPanel configPanel = createConfigPanel();
        leftPanel.add(configPanel);
        leftPanel.add(Box.createVerticalStrut(10));
        
        // Recipients panel
        JPanel recipientsPanel = createRecipientsPanel();
        leftPanel.add(recipientsPanel);
        
        // Right panel: Email composition and controls
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        
        // Email composition panel
        JPanel emailPanel = createEmailPanel();
        rightPanel.add(emailPanel);
        rightPanel.add(Box.createVerticalStrut(10));
        
        // Control buttons panel
        JPanel controlPanel = createControlPanel();
        rightPanel.add(controlPanel);
        
        // Add panels to main layout
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        // Status panel at bottom
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Set window size and center
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }
    
    /**
     * Creates the configuration panel.
     */
    private JPanel createConfigPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("SMTP Configuration"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // SMTP Host
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("SMTP Host:"), gbc);
        gbc.gridx = 1;
        panel.add(smtpHostField, gbc);
        
        // SMTP Port
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("SMTP Port:"), gbc);
        gbc.gridx = 1;
        panel.add(smtpPortField, gbc);
        
        // Enable TLS
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(enableTLSBox, gbc);
        
        // Sender Email
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Sender Email:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(senderEmailField, gbc);
        
        // Sender Password
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("App Password:"), gbc);
        gbc.gridx = 1;
        panel.add(senderPasswordField, gbc);
        
        // Info label
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html><small><i>Use Gmail App Password, not your main password</i></small></html>");
        infoLabel.setForeground(new Color(100, 100, 100));
        panel.add(infoLabel, gbc);
        
        return panel;
    }
    
    /**
     * Creates the recipients panel.
     */
    private JPanel createRecipientsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new TitledBorder("Recipients"));
        
        JScrollPane scrollPane = new JScrollPane(recipientsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(loadRecipientsButton);
        buttonPanel.add(recipientCountLabel);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates the email composition panel.
     */
    private JPanel createEmailPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new TitledBorder("Email Composition"));
        
        // Subject
        JPanel subjectPanel = new JPanel(new BorderLayout(5, 5));
        subjectPanel.add(new JLabel("Subject:"), BorderLayout.WEST);
        subjectPanel.add(subjectField, BorderLayout.CENTER);
        
        // Body
        JPanel bodyPanel = new JPanel(new BorderLayout(5, 5));
        bodyPanel.add(new JLabel("Body:"), BorderLayout.NORTH);
        JScrollPane bodyScroll = new JScrollPane(bodyArea);
        bodyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        bodyPanel.add(bodyScroll, BorderLayout.CENTER);
        
        // HTML mode checkbox
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionsPanel.add(htmlModeBox);
        
        panel.add(subjectPanel, BorderLayout.NORTH);
        panel.add(bodyPanel, BorderLayout.CENTER);
        panel.add(optionsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates the control buttons panel.
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(new TitledBorder("Actions"));
        
        panel.add(testConnectionButton);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(sendButton);
        panel.add(clearButton);
        
        return panel;
    }
    
    /**
     * Creates the status panel.
     */
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new TitledBorder("Status & Logs"));
        
        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(progressBar, BorderLayout.CENTER);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Sets up event handlers for all interactive components.
     */
    private void setupEventHandlers() {
        // Load recipients button
        loadRecipientsButton.addActionListener(e -> loadRecipientsFromFile());
        
        // Recipients area listener for count update
        recipientsArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateRecipientCount(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateRecipientCount(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateRecipientCount(); }
        });
        
        // Test connection button
        testConnectionButton.addActionListener(e -> testConnection());
        
        // Send button
        sendButton.addActionListener(e -> sendBulkEmails());
        
        // Clear button
        clearButton.addActionListener(e -> clearAll());
    }
    
    /**
     * Sets default values for the GUI.
     */
    private void setDefaultValues() {
        // Set default email body template
        String defaultBody = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "<style>\n" +
                "  body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }\n" +
                "  .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
                "  .header { background-color: #4285f4; color: white; padding: 20px; text-align: center; }\n" +
                "  .content { padding: 20px; background-color: #f9f9f9; }\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class='container'>\n" +
                "<div class='header'>\n" +
                "<h1>Hello from Gmail Bulk Sender</h1>\n" +
                "</div>\n" +
                "<div class='content'>\n" +
                "<p>This is a test email sent using the Gmail Bulk Sender application.</p>\n" +
                "<p>You can customize this email template.</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
        bodyArea.setText(defaultBody);
        subjectField.setText("Test Email from Gmail Bulk Sender");
        
        logStatus("Application started. Please configure your settings and load recipients.");
    }
    
    /**
     * Updates the recipient count label.
     */
    private void updateRecipientCount() {
        String text = recipientsArea.getText().trim();
        if (text.isEmpty()) {
            recipientCountLabel.setText("Recipients: 0");
            return;
        }
        
        String[] lines = text.split("\n");
        int count = 0;
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
                count++;
            }
        }
        recipientCountLabel.setText("Recipients: " + count);
    }
    
    /**
     * Loads recipients from a file using a file chooser.
     */
    private void loadRecipientsFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Recipients File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
            }
            
            @Override
            public String getDescription() {
                return "Text Files (*.txt)";
            }
        });
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                recipientManager = new RecipientManager();
                recipientManager.loadRecipientsFromFile(selectedFile.getAbsolutePath());
                
                // Display recipients in text area
                List<String> recipients = recipientManager.getRecipients();
                StringBuilder sb = new StringBuilder();
                for (String recipient : recipients) {
                    sb.append(recipient).append("\n");
                }
                recipientsArea.setText(sb.toString());
                
                logStatus("Loaded " + recipients.size() + " recipient(s) from: " + selectedFile.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error loading recipients file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                logStatus("ERROR: Failed to load recipients - " + e.getMessage());
            }
        }
    }
    
    /**
     * Tests the SMTP connection with current configuration.
     */
    private void testConnection() {
        if (!validateConfiguration()) {
            return;
        }
        
        try {
            createEmailConfig();
            emailSender = new EmailSender();
            emailSender.initialize(config);
            
            logStatus("✓ Connection test successful! Configuration is valid.");
            JOptionPane.showMessageDialog(this,
                "Connection test successful!\nYour configuration is valid.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            logStatus("✗ Connection test failed: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Connection test failed:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Sends bulk emails to all recipients.
     */
    private void sendBulkEmails() {
        if (isSending) {
            JOptionPane.showMessageDialog(this,
                "Email sending is already in progress. Please wait.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateConfiguration()) {
            return;
        }
        
        if (!validateRecipients()) {
            return;
        }
        
        if (!validateEmailContent()) {
            return;
        }
        
        // Confirm before sending
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to send emails to " + getRecipientCount() + " recipient(s)?",
            "Confirm Send",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Start sending in a separate thread to avoid blocking UI
        new Thread(() -> {
            isSending = true;
            sendButton.setEnabled(false);
            testConnectionButton.setEnabled(false);
            progressBar.setIndeterminate(true);
            progressBar.setString("Sending...");
            statusLabel.setText("Status: Sending emails...");
            
            try {
                // Create configuration and initialize sender
                createEmailConfig();
                emailSender = new EmailSender();
                emailSender.initialize(config);
                
                // Load recipients
                loadRecipientsFromTextArea();
                
                // Get email content
                String subject = subjectField.getText().trim();
                String body = bodyArea.getText().trim();
                
                if (!htmlModeBox.isSelected()) {
                    // Convert HTML to plain text if HTML mode is off
                    body = body.replaceAll("<[^>]+>", "");
                }
                
                logStatus("Starting bulk email send to " + recipientManager.getRecipientCount() + " recipient(s)...");
                
                // Custom sender with progress tracking
                sendEmailsWithProgress(subject, body);
                
            } catch (Exception e) {
                logStatus("✗ Fatal error: " + e.getMessage());
                JOptionPane.showMessageDialog(this,
                    "Fatal error during email sending:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            } finally {
                isSending = false;
                sendButton.setEnabled(true);
                testConnectionButton.setEnabled(true);
                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                progressBar.setString("Complete");
                statusLabel.setText("Status: Ready");
            }
        }).start();
    }
    
    /**
     * Sends emails with progress tracking.
     */
    private void sendEmailsWithProgress(String subject, String body) {
        List<String> recipients = recipientManager.getRecipients();
        int total = recipients.size();
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        
        for (int i = 0; i < recipients.size(); i++) {
            String recipient = recipients.get(i);
            try {
                emailSender.sendPersonalizedEmail(recipient, subject, body);
                successCount.incrementAndGet();
                logStatus("✓ [" + (i + 1) + "/" + total + "] Sent to: " + recipient);
            } catch (MessagingException e) {
                failureCount.incrementAndGet();
                logStatus("✗ [" + (i + 1) + "/" + total + "] Failed: " + recipient + " - " + e.getMessage());
            } catch (Exception e) {
                failureCount.incrementAndGet();
                logStatus("✗ [" + (i + 1) + "/" + total + "] Failed: " + recipient + " - " + e.getMessage());
            }
            
            // Update progress
            final int progress = (int) ((i + 1) * 100.0 / total);
            SwingUtilities.invokeLater(() -> {
                progressBar.setValue(progress);
                progressBar.setString(progress + "%");
            });
        }
        
        final int finalSuccess = successCount.get();
        final int finalFailure = failureCount.get();
        
        logStatus("========================================");
        logStatus("Bulk email operation completed!");
        logStatus("Success: " + finalSuccess + " | Failures: " + finalFailure);
        logStatus("========================================");
        
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                "Bulk email operation completed!\n\n" +
                "Success: " + finalSuccess + "\n" +
                "Failures: " + finalFailure,
                "Operation Complete",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    /**
     * Loads recipients from the text area.
     */
    private void loadRecipientsFromTextArea() {
        recipientManager = new RecipientManager();
        String text = recipientsArea.getText().trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("No recipients specified");
        }
        
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
                recipientManager.addRecipient(line);
            }
        }
    }
    
    /**
     * Gets the recipient count from the text area.
     */
    private int getRecipientCount() {
        String text = recipientsArea.getText().trim();
        if (text.isEmpty()) {
            return 0;
        }
        
        String[] lines = text.split("\n");
        int count = 0;
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Creates EmailConfig from GUI fields.
     */
    private void createEmailConfig() {
        config = new EmailConfig();
        config.setSmtpHost(smtpHostField.getText().trim());
        try {
            config.setSmtpPort(Integer.parseInt(smtpPortField.getText().trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid SMTP port number");
        }
        config.setSenderEmail(senderEmailField.getText().trim());
        config.setSenderPassword(new String(senderPasswordField.getPassword()));
        config.setEnableTLS(enableTLSBox.isSelected());
    }
    
    /**
     * Validates the configuration fields.
     */
    private boolean validateConfiguration() {
        if (smtpHostField.getText().trim().isEmpty()) {
            showError("Please enter SMTP host");
            return false;
        }
        if (smtpPortField.getText().trim().isEmpty()) {
            showError("Please enter SMTP port");
            return false;
        }
        try {
            Integer.parseInt(smtpPortField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Invalid SMTP port number");
            return false;
        }
        if (senderEmailField.getText().trim().isEmpty()) {
            showError("Please enter sender email address");
            return false;
        }
        if (senderPasswordField.getPassword().length == 0) {
            showError("Please enter sender password (App Password)");
            return false;
        }
        return true;
    }
    
    /**
     * Validates that recipients are specified.
     */
    private boolean validateRecipients() {
        if (getRecipientCount() == 0) {
            showError("Please specify at least one recipient");
            return false;
        }
        return true;
    }
    
    /**
     * Validates email content.
     */
    private boolean validateEmailContent() {
        if (subjectField.getText().trim().isEmpty()) {
            showError("Please enter email subject");
            return false;
        }
        if (bodyArea.getText().trim().isEmpty()) {
            showError("Please enter email body");
            return false;
        }
        return true;
    }
    
    /**
     * Shows an error message dialog.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Logs a status message to the status area.
     */
    private void logStatus(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = java.time.LocalTime.now().toString().substring(0, 8);
            statusArea.append("[" + timestamp + "] " + message + "\n");
            statusArea.setCaretPosition(statusArea.getDocument().getLength());
        });
    }
    
    /**
     * Clears all fields.
     */
    private void clearAll() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to clear all fields?",
            "Confirm Clear",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            senderEmailField.setText("");
            senderPasswordField.setText("");
            recipientsArea.setText("");
            subjectField.setText("");
            bodyArea.setText("");
            statusArea.setText("");
            progressBar.setValue(0);
            progressBar.setString("Ready");
            statusLabel.setText("Status: Ready");
            logStatus("All fields cleared.");
        }
    }
    
    /**
     * Main method to launch the GUI.
     */
    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system L&F fails
        }
        
        SwingUtilities.invokeLater(() -> {
            BulkSenderGUI gui = new BulkSenderGUI();
            gui.setVisible(true);
        });
    }
}

