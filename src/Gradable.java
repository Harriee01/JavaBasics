public interface Gradable {
    boolean recordGrade(double grade); //this is a method to record the grade of a student.
    //it returns true if the grade is recorded successfully, else false


    boolean validateGrade(double grade);// this method checks if a grade is valid, (between 0 and 100).
    //it returns true if valid, false if not
}
