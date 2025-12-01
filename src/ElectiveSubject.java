public class ElectiveSubject extends Subject {
    private final boolean mandatory = false;

    public ElectiveSubject(String subjectName, String subjectCode) {
        super(subjectName, subjectCode);
    }

    @Override
    public void displaySubjectDetails() {
        System.out.println("Elective Subject: " + getSubjectName() +
                " (" + getSubjectCode() + ") - Mandatory: " + mandatory);
    }

    @Override
    public String getSubjectType() {
        return "Elective";
    }

    public boolean isMandatory() {
        return false;
    }
}