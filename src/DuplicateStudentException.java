public class DuplicateStudentException extends Exception {
    // This is the exception thrown when a user tries to add a student with a duplicate ID
    public DuplicateStudentException(String studentID) {
        super("Student with ID " + studentID + " is already exists.");

    }
}
