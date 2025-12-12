// this exception is thrown when file operations fail
class FileOperationException extends Exception {
    //this constructor  takes error details
    public FileOperationException(String message) {
        super("File operation failed: " + message); //returns error message
    }
}
