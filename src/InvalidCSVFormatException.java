//this exception is thrown when a CSV format is incorrect
 public class InvalidCSVFormatException extends Exception {
    //this constructor takes row number and details
    public InvalidCSVFormatException(int row, String details) {
        super("Invalid CSV format at row " + row + ": " + details);//returns error message
    }
}
