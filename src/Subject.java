public abstract class Subject {// The Subject class is an abstract class serving as a template for all subjects
    //the fields are private and can only be accessed within this class; so it showcases encapsulation as required
    private String subjectName;
    private String subjectCode;

    //To create a new Subject object, this constructor is used; it initializes the private fields
    public Subject(String subjectName, String subjectCode) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
    }
    //These Getter methods allow the other classes to read private fields stated in the abstract Subject class
    public String getSubjectName() {return subjectName;}
    public String getSubjectCode() {return subjectCode;}

    // These Setter methods allow other classes to modify private fields within this class
    public void setSubjectName(String subjectName) {this.subjectName = subjectName;}
    public void setSubjectCode(String subjectCode) {this.subjectCode = subjectCode;}

    //These are the abstract methods that must be implemented by the child classes
    public abstract void displaySubjectDetails();// this abstract method displays the details of the subject
    public abstract String getSubjectType();// this abstract method returns the type of subject

}
