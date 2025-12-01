public class CoreSubject extends Subject{
    private final boolean mandatory = true;//value cannot be changed, set to true because core subjects are mandatory

    public CoreSubject(String subjectName, String subjectCode){
        super(subjectName, subjectCode);
    }

    @Override
    public void displaySubjectDetails() {

        System.out.println("Core Subject: " + getSubjectName() + "(" + getSubjectCode() + ") - Mandatory: " +  mandatory);

    }

    @Override
    public String getSubjectType() {
        return "Core";
    }

    public boolean isMandatory() { // this method checks if the subject is mandatory
        return true;
    }
}
