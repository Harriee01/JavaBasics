public abstract class Subject {
    private String subjectName;
    private String subjectCode;

    //Constructor
    public Subject(String subjectName, String subjectCode) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
    }
    //Getter methods
    public String getSubjectName() {return subjectName;}
    public String getSubjectCode() {return subjectCode;}

    // Setter methods
    public void setSubjectName(String subjectName) {this.subjectName = subjectName;}
    public void setSubjectCode(String subjectCode) {this.subjectCode = subjectCode;}

    //abstract classes
    public abstract void displaySubjectDetails();
    public abstract String getSubjectType();

}
