# Gmail Bulk Sender Application

A complete, object-oriented Java application for sending bulk, personalized emails via the Gmail SMTP server. The application emphasizes separation of concerns and utilizes the Jakarta Mail API.

## Features

- **Graphical User Interface**: Modern, intuitive GUI for easy email management
- **Command-Line Interface**: Traditional CLI mode for automation and scripting
- **Secure Email Transmission**: Uses STARTTLS on port 587 for encrypted email delivery
- **Gmail App Password Support**: Requires Gmail App Password for enhanced security
- **Object-Oriented Design**: Clean separation of concerns with dedicated classes
- **HTML Email Support**: Send rich HTML-formatted emails
- **Error Handling**: Individual email failures don't stop the entire bulk operation
- **File-Based Configuration**: Easy configuration via properties file or GUI
- **Real-Time Progress Tracking**: Visual progress bar and status updates
- **Logging**: Comprehensive logging for monitoring email send status

## Project Structure

```
gmail-bulk-sender/
├── pom.xml                          # Maven build configuration
├── config.properties                # SMTP configuration file
├── recipients.txt                   # Recipient email addresses
├── README.md                        # This file
└── src/
    └── main/
        └── java/
            └── com/
                └── bulksender/
                    ├── BulkSenderApp.java      # Main entry point
                    ├── EmailConfig.java         # Configuration management
                    ├── EmailSender.java         # Core email sending logic
                    └── RecipientManager.java    # Recipient data handling
```

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Gmail account with 2-Step Verification enabled
- Gmail App Password (see setup instructions below)

## Setup Instructions

### Step 1: Install Prerequisites

#### Install Java
1. Download Java 11 or higher from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
2. Install Java and add it to your system PATH
3. Verify installation by running: `java -version`

#### Install Maven (Optional - for building from source)
1. Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
2. Extract and add Maven's `bin` directory to your system PATH
3. Verify installation by running: `mvn -version`

**Note**: If you're using the pre-built JAR file, Maven is not required.

### Step 2: Generate Gmail App Password

**CRITICAL**: Gmail no longer accepts regular passwords for SMTP. You **MUST** use an App Password.

#### Enable 2-Step Verification (if not already enabled)
1. Go to [Google Account Security](https://myaccount.google.com/security)
2. Under "Signing in to Google", click **2-Step Verification**
3. Follow the prompts to enable 2-Step Verification (you'll need your phone)

#### Generate App Password
1. Go directly to [App Passwords](https://myaccount.google.com/apppasswords)
   - Or navigate: Google Account → Security → 2-Step Verification → App passwords
2. You may be asked to sign in again
3. Under "Select app", choose **Mail**
4. Under "Select device", choose **Other (Custom name)**
5. Enter a name like "Gmail Bulk Sender" or "Java Email App"
6. Click **Generate**
7. **Copy the 16-character password** (it will look like: `abcd efgh ijkl mnop`)
   - You can copy with or without spaces - both work
   - **Save this password** - you won't be able to see it again!

**Troubleshooting App Password Generation:**
- If you don't see "App passwords" option, ensure 2-Step Verification is enabled
- If you see "App passwords aren't available", your account may not support them (check Google Account type)
- Wait a few minutes after enabling 2-Step Verification before generating App Password

### Step 3: Configure the Application

#### Option A: Using config.properties (CLI Mode)

1. Open `config.properties` in a text editor
2. Update the following lines:

```properties
# Your Gmail address (the one you use to sign in)
sender.email=your-email@gmail.com

# Your 16-character App Password (NOT your regular password!)
sender.password=abcdefghijklmnop
```

**Example:**
```properties
sender.email=pshirode16@gmail.com
sender.password=abcd efgh ijkl mnop
```

**Important Notes:**
- Use your **full Gmail address** (including @gmail.com)
- Use the **App Password** you just generated, NOT your regular Gmail password
- The password can have spaces or not - both formats work
- Never share or commit this file to version control

#### Option B: Using GUI (Recommended for First-Time Users)

1. Run the application (see "Running the Application" below)
2. Enter your Gmail address in the "Sender Email" field
3. Enter your App Password in the "App Password" field
4. Click "Test Connection" to verify your credentials work
5. If test succeeds, you're ready to send emails!

### Step 4: Add Recipients

#### Option A: Using recipients.txt file

1. Open `recipients.txt` in a text editor
2. Add one email address per line:

```
recipient1@example.com
recipient2@example.com
recipient3@example.com
```

**Format Rules:**
- One email address per line
- Lines starting with `#` are treated as comments (ignored)
- Empty lines are ignored
- No commas or semicolons needed

**Example:**
```
# My recipient list
# Created: 2024-01-15

user1@gmail.com
user2@yahoo.com
user3@company.com

# Test email
test@example.com
```

#### Option B: Using GUI

1. Run the application in GUI mode
2. Click "Load from File" to load from `recipients.txt`
3. Or type/paste email addresses directly into the Recipients text area (one per line)

### Step 5: Verify Setup

#### Test Connection (GUI Mode)
1. Launch the application in GUI mode
2. Enter your Gmail credentials
3. Click **"Test Connection"** button
4. If successful, you'll see: "✓ Connection test successful!"
5. If failed, check the error message:
   - **"535-5.7.8 Username and Password not accepted"** → Wrong password type (use App Password)
   - **"Connection timeout"** → Network/firewall issue
   - **"Authentication failed"** → Check email/password

#### Quick Test (CLI Mode)
1. Ensure `config.properties` and `recipients.txt` are configured
2. Run the application (see "Running the Application" below)
3. Check the console output for success/failure messages

## Building the Application

### Using Maven (Command Line)

```bash
# Compile the project
mvn clean compile

# Create executable JAR
mvn clean package
```

The executable JAR will be created in `target/gmail-bulk-sender-1.0.0.jar`.

### Using Batch Files (Windows)

**Quick Build:**
- Double-click `build.bat` to compile and package the application

**What it does:**
- Checks if Maven is installed
- Compiles the project
- Creates the executable JAR file
- Shows build status

## Running the Application

### Quick Start (Windows - Recommended)

**Easiest Method:**
1. Double-click `start.bat`
2. Select option 1 (Run GUI Mode)
3. Enter your credentials and start sending!

**Available Batch Files:**
- `start.bat` - Interactive menu with all options
- `run-gui.bat` - Launch GUI mode directly
- `run-cli.bat` - Launch CLI mode directly
- `build.bat` - Build the project
- `run-maven-gui.bat` - Run GUI using Maven (no JAR needed)
- `run-maven-cli.bat` - Run CLI using Maven (no JAR needed)

### GUI Mode (Recommended for First-Time Users)

The GUI provides an intuitive interface for sending bulk emails.

#### Using Batch File (Windows):
```bash
# Double-click run-gui.bat
# Or from command line:
run-gui.bat
```

#### Using JAR File:
```bash
# Make sure you've built the project first (run build.bat)
java -jar target/gmail-bulk-sender-1.0.0.jar --gui
# or
java -jar target/gmail-bulk-sender-1.0.0.jar -g
```

#### Using Maven:
```bash
mvn exec:java -Dexec.mainClass="com.bulksender.BulkSenderGUI"
```

**GUI Features:**
- **Visual Configuration**: Enter SMTP settings directly in the interface
- **Recipient Management**: Load recipients from file or enter manually
- **Email Composition**: Rich text editor for subject and HTML body
- **Test Connection**: Verify your configuration before sending
- **Progress Tracking**: Real-time progress bar and status updates
- **Status Logs**: Detailed log of each email send operation

**GUI Workflow:**
1. Enter your Gmail address and App Password
2. Click "Test Connection" to verify credentials
3. Load recipients (click "Load from File" or paste directly)
4. Enter email subject and body
5. Click "Send Bulk Emails"
6. Monitor progress in the status area

### Command-Line Mode

Best for automation, scripting, or when you prefer terminal-based tools.

#### Using Batch File (Windows):
```bash
# Double-click run-cli.bat
# Or from command line:
run-cli.bat
```

#### Using JAR File:
```bash
# Make sure you've built the project first
java -jar target/gmail-bulk-sender-1.0.0.jar
```

#### Using Maven:
```bash
mvn exec:java -Dexec.mainClass="com.bulksender.BulkSenderApp"
```

**CLI Requirements:**
- `config.properties` must exist with your credentials
- `recipients.txt` must exist with recipient email addresses

### Custom File Paths (CLI Mode)

You can specify custom paths for configuration and recipient files:

```bash
java -jar target/gmail-bulk-sender-1.0.0.jar my-config.properties my-recipients.txt
```

**Example:**
```bash
java -jar target/gmail-bulk-sender-1.0.0.jar C:\Users\You\my-config.properties C:\Users\You\my-recipients.txt
```

## Architecture

### EmailConfig.java
- Encapsulates SMTP configuration and credentials
- Loads configuration from properties files
- Provides JavaMail-compatible Properties objects
- Defaults to Gmail SMTP settings (smtp.gmail.com:587)

### RecipientManager.java
- Manages recipient email addresses
- Loads recipients from plain text files
- Supports comments and empty lines in recipient files
- Provides methods for recipient management

### EmailSender.java
- Handles JavaMail Session management
- Implements SMTP authentication
- Sends individual and bulk emails
- Includes comprehensive error handling
- Supports HTML email content

### BulkSenderApp.java
- Main entry point and orchestration
- Supports both GUI and CLI modes
- Loads configuration and recipients
- Initializes email sender
- Defines email content
- Executes bulk email operation

### BulkSenderGUI.java
- Graphical user interface for the application
- Provides intuitive form-based configuration
- File chooser for loading recipients
- Rich text composition area for email content
- Real-time progress tracking and status logging
- Connection testing functionality

## Security Best Practices

1. **Never commit credentials**: Add `config.properties` to `.gitignore`
2. **Use App Passwords**: Always use Gmail App Passwords, never main passwords
3. **Secure file permissions**: Restrict read access to configuration files
4. **Review recipients**: Verify recipient lists before sending bulk emails
5. **Rate limiting**: Be mindful of Gmail's sending limits (500 emails/day for free accounts)

## Gmail Sending Limits

- **Free Gmail accounts**: 500 emails per day
- **Google Workspace accounts**: 2000 emails per day
- **Rate limit**: ~100 emails per hour

The application will continue sending even if individual emails fail, allowing you to identify and retry failed recipients.

## Customization

### Custom Email Content

Edit the `buildEmailBody()` method in `BulkSenderApp.java` to customize the email template:

```java
private static String buildEmailBody() {
    return "<html>Your custom HTML content here</html>";
}
```

### Personalized Emails

You can extend `EmailSender.java` to support personalized content by replacing placeholders in the email body with recipient-specific data.

## Troubleshooting

### Error: "535-5.7.8 Username and Password not accepted"

**This is the most common error!** It means Gmail rejected your credentials.

**Solutions:**
1. ✅ **Use App Password, not regular password**
   - You MUST use a Gmail App Password (16 characters)
   - Regular Gmail passwords will NOT work
   - See "Step 2: Generate Gmail App Password" above

2. ✅ **Verify 2-Step Verification is enabled**
   - Go to [Google Account Security](https://myaccount.google.com/security)
   - Ensure "2-Step Verification" is ON
   - App Passwords require 2-Step Verification

3. ✅ **Check your email address**
   - Use your full Gmail address (e.g., `yourname@gmail.com`)
   - Ensure it matches the account where you generated the App Password

4. ✅ **Regenerate App Password**
   - Sometimes App Passwords need to be regenerated
   - Generate a new one and update `config.properties`

5. ✅ **Wait a few minutes**
   - After enabling 2-Step Verification, wait 5-10 minutes before generating App Password

### Authentication Failed (General)
- Verify you're using a Gmail App Password, not your main password
- Ensure 2-Step Verification is enabled on your Google account
- Check that the App Password is correctly copied (spaces are optional)
- Verify the email address in `config.properties` matches your Gmail account
- Try generating a new App Password

### Connection Timeout
- Verify your internet connection
- Check firewall settings (allow port 587)
- Ensure port 587 is not blocked by your network/ISP
- Try using a different network (some corporate networks block SMTP)
- Check if antivirus is blocking the connection

### No Recipients Loaded
- Verify `recipients.txt` exists and is readable
- Check that the file contains valid email addresses
- Ensure email addresses are on separate lines
- Remove any commas or special characters (except @)
- Check file encoding (should be UTF-8 or plain text)

### All Emails Failing
- **First**: Test connection using "Test Connection" button in GUI
- Check the specific error message in the status log
- Verify SMTP settings:
  - Host: `smtp.gmail.com`
  - Port: `587`
  - TLS: Enabled
- Check Gmail sending limits (see "Gmail Sending Limits" below)
- Ensure you're not sending too fast (add delays if needed)

### Build Errors
- **"Maven not found"**: Install Maven and add to PATH, or use pre-built JAR
- **"Java not found"**: Install Java 11+ and add to PATH
- **"Dependencies not found"**: Run `mvn clean install` to download dependencies

### GUI Not Starting
- Ensure Java is installed: `java -version`
- Check if JAR file exists: `target/gmail-bulk-sender-1.0.0.jar`
- Try building first: Run `build.bat` or `mvn clean package`
- Check Java version (must be 11 or higher)

### Still Having Issues?
1. Check the error message in the GUI status log or console output
2. Verify all prerequisites are installed (Java, Maven if building)
3. Test connection before sending bulk emails
4. Start with a single recipient to isolate issues
5. Review Gmail sending limits (you may have hit daily/hourly limits)

## Dependencies

- **Jakarta Mail API 2.1.2**: Email API specification
- **Angus Mail 2.0.2**: Jakarta Mail implementation

## License

This project is provided as-is for educational and development purposes.

## Support

For issues related to:
- **Gmail authentication**: See [Google Account Help](https://support.google.com/accounts)
- **JavaMail**: See [Jakarta Mail Documentation](https://jakarta.ee/specifications/mail/)
- **Application bugs**: Review logs for detailed error messages

