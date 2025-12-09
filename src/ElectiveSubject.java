public class ElectiveSubject extends Subject { // inherits from the Subject class and represents optional subjects
    private  boolean mandatory = false;// always false for elective subjects

    //This constructor calls the parent constructor using the 'super' keyword
    public ElectiveSubject(String subjectName, String subjectCode) {
        super(subjectName, subjectCode);
        this.mandatory = false; //elective subjects are not mandatory
    }

    @Override // overriding the abstract method displaySubjectDetails from the Subject class
    public void displaySubjectDetails() {
        System.out.println("Elective Subject: " + getSubjectName() +
                " (" + getSubjectCode() + ") - Mandatory: " + mandatory);// prints out subject details
    }

    @Override // overriding the  abstract method, getSubjectType to return 'Elective'
    public String getSubjectType() {
        return "Elective";
    }

    //this method checks if elective is mandatory ad returns false
    public boolean isMandatory() {
        return false;
    }
}