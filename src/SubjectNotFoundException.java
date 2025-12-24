public class SubjectNotFoundException extends Exception{
    //This exception is thrown when a user tries looking for a subject that is not in the system
    public SubjectNotFoundException(String subjectCode){
        super("Subject with code " + subjectCode + " not found");
    }
}
