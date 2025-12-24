import java.io.IOException; //to handle the error that can occur when writing to a log file
import java.util.logging.*; // importing everything from the java.util.logging package

//This is the logging utility to provide consistent logging throughout the system

public class AppLogger {
    // Single instance of logger (Singleton pattern)
    private static Logger logger;

    // Static initializer that runs once when class is loaded
    static {
        try {
            initializeLogger(); // Set up logger configuration
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    //This method initializes the logger with file and console handlers
    private static void initializeLogger() throws IOException {
        //this get the global logger instance
        logger = Logger.getLogger(AppLogger.class.getName());

        //this removes default handlers to avoid duplicate logging
        logger.setUseParentHandlers(false);

        // creating console handler for all messages
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO); // Show INFO and above in console
        consoleHandler.setFormatter(new SimpleFormatter()); // Simple text format

        // creating file handler for all messages (append mode)
        FileHandler fileHandler = new FileHandler("grade_management.log", true);
        fileHandler.setLevel(Level.ALL); // Log ALL levels to file
        fileHandler.setFormatter(new SimpleFormatter()); // Simple text format

        //this adds handlers to logger
        logger.addHandler(consoleHandler);
        logger.addHandler(fileHandler);

        // set logger level
        logger.setLevel(Level.ALL);

        logger.info("Logger initialized successfully.");
    }


     //Logs an informational message
    public static void info(String message) {
        logger.info(message);
    }

    //Logs a warning message
    public static void warning(String message) {
        logger.warning(message);
    }

    //Logs an error message
    public static void error(String message) {
        logger.severe(message);
    }


}
