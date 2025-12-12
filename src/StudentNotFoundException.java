public class StudentNotFoundException extends Exception { // a custom exception to provide informative error message for a student not found in the system
    //this constructor takes the student ID to create a helpful message
    public StudentNotFoundException(String studentId) {
        //calls the parent Exception constructor with the custom message
        super("Student with ID " + studentId + " not found in the system");
    }

}
