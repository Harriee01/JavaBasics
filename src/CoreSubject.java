public class CoreSubject extends Subject{//the CoreSubject class inherits from Subject
    //private field specific to CoreSubject
    private final boolean mandatory = true;//value cannot be changed, set to true because core subjects are mandatory

     //This constructor calls the parent constructor(from Subject) using the 'super' keyword
    public CoreSubject(String subjectName, String subjectCode){
        super(subjectName, subjectCode);
    }

    @Override  // overriding the abstract method, displaySubjectDetails from Subject class
    public void displaySubjectDetails() {

        //Printing out the subject information
        System.out.println("Core Subject: " + getSubjectName() + "(" + getSubjectCode() + ") - Mandatory: " +  mandatory);

    }

    @Override // overriding the abstract method, getSubjectType to return 'Core'
    public String getSubjectType() {
        return "Core";
    }
    //This method checks if the subject is mandatory
    public boolean isMandatory() { // this method checks if the subject is mandatory
        return true;
    } // must return true because core subjects are mandatory
}
