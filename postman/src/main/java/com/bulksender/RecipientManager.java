package com.bulksender;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Manages the list of recipients loaded from an external data source.
 * Supports loading recipient email addresses from a plain text file
 * where each line contains one email address.
 */
public class RecipientManager {
    private static final Logger logger = Logger.getLogger(RecipientManager.class.getName());
    
    private List<String> recipients;
    
    /**
     * Default constructor initializes an empty recipient list.
     */
    public RecipientManager() {
        this.recipients = new ArrayList<>();
    }
    
    /**
     * Loads recipient email addresses from a plain text file.
     * Each line in the file should contain one email address.
     * Empty lines and lines starting with '#' are ignored.
     * 
     * @param filePath Path to the file containing recipient email addresses
     * @throws IOException If the file cannot be read
     */
    public void loadRecipientsFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            throw new IOException("Recipient file not found: " + filePath);
        }
        
        try (Stream<String> lines = Files.lines(path)) {
            recipients = lines
                .map(String::trim)
                .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                .collect(Collectors.toList());
        }
        
        if (recipients.isEmpty()) {
            logger.warning("No valid recipients found in file: " + filePath);
        } else {
            logger.info("Loaded " + recipients.size() + " recipient(s) from: " + filePath);
        }
    }
    
    /**
     * Returns the list of loaded recipients.
     * 
     * @return List of recipient email addresses
     */
    public List<String> getRecipients() {
        return new ArrayList<>(recipients); // Return a copy to prevent external modification
    }
    
    /**
     * Adds a single recipient to the list.
     * 
     * @param email The email address to add
     */
    public void addRecipient(String email) {
        if (email != null && !email.trim().isEmpty()) {
            recipients.add(email.trim());
        }
    }
    
    /**
     * Returns the number of recipients.
     * 
     * @return Number of recipients
     */
    public int getRecipientCount() {
        return recipients.size();
    }
    
    /**
     * Clears all recipients from the list.
     */
    public void clear() {
        recipients.clear();
    }
}




