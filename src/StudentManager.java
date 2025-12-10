public class StudentManager {// uses composition(it HAS-A array of Students); manages all students in the system
    //private fields specific to StudentManager for managing students
    private Student[] students;  // array to store all students
    private int studentCount;    // tracks number of current registered students

    //referencing the GradeManager class for the sake of calculating averages
    private GradeManager gradeManager;

    //this constructor creates an empty student array
    public StudentManager() {
        students = new Student[50];  // initializing the  array with maximum size of 50
        studentCount = 0;  // starting with no(zero) students
    }

    // this method sets the  GradeManager reference
    public void setGradeManager(GradeManager gradeManager01) {
        this.gradeManager = gradeManager01;
    }


    // this method adds a student to the array
    public void addStudent(Student student) {
        if (studentCount < students.length) {// checking if the array is not full
            students[studentCount] = student;// the student is added at the next available position
            student.setGradeManager(gradeManager);// the gradeManager reference for the student is set
            studentCount++;// increment count
        } else {
            System.out.println("Student storage is full.");
        }
    }

    // this method finds a student by ID
    public Student findStudent(String studentId) {
        for (int i = 0; i < studentCount; i++) {//looping through all the students
            if (students[i].getStudentId().equals(studentId)) {//checking if the current student's ID is a match
                return students[i];// found so it returns the student
            }
        }
        return null;// not found so it returns null
    }

    // this method displays all students
    public void viewAllStudents() {
        if (studentCount == 0) {//checking if there are any students
            System.out.println("No students registered.");
            return;
        }

        System.out.println("\n=== Student List ===");
        //looping through to display each student
        for (int i = 0; i < studentCount; i++) {
            Student student = students[i];
            double avgGrade = student.calculateAverageGrade();//calculates actual average grade for the student
            String statusText = avgGrade ==0 ? "Active": (student.isPassing() ? "Passed" : "Failed");
            System.out.println("----------------------------------");
            System.out.println("ID: " + student.getStudentId());
            System.out.println("Name: " + student.getStudentName());
            System.out.println("Type: " + student.getStudentType());
            System.out.println("Average Grade: " + avgGrade);
            System.out.println("Passing Grade: " + student.getPassingGrade());
            System.out.println("Status: " + statusText);

            // showing additional information for Honors students
            if (student instanceof HonorsStudent) {
                HonorsStudent honorsStudent01 = (HonorsStudent) student;  // Type casting
                // counting enrolled subjects (grades) for the student
                int enrolledSubjects = gradeManager.getGradeCountForStudent(student.getStudentId());
                System.out.println("           | Enrolled Subjects: " + enrolledSubjects +
                        " | Passing Grade: " + student.getPassingGrade() + "% | Honors Eligible");
            } else {
                // counting enrolled subjects for regular student
                int enrolledSubjects = gradeManager.getGradeCountForStudent(student.getStudentId());
                System.out.println("           | Enrolled Subjects: " + enrolledSubjects +
                        " | Passing Grade: " + student.getPassingGrade() + "%");
            }
            System.out.println("----------------------------------");
        }


        System.out.println("\nTotal Students: " + getStudentCount());
        System.out.println("Class Average: " + getAverageClassGrade());
    }

    //this method calculates the average grade of all students
    public double getAverageClassGrade() {
        if (studentCount == 0){ //checking if there are no students
            return 0.0;
        }
        double totalAverage = 0.0;// the sum of all student averages
        int countStudentsWithGrades = 0;//takes the count of students who have grades
        for (int i = 0; i < studentCount; i++) {//looping through all the students and summing their averages
            double studentAvg = students[i].calculateAverageGrade();
            if (studentAvg > 0) {// only count students who have at least one grade
                totalAverage += studentAvg;
                countStudentsWithGrades++;
            }
        }
        if(countStudentsWithGrades == 0){//avoiding division by zero
            return 0.0;
        }

        return totalAverage / countStudentsWithGrades;// return the average of all student averages
    }

    // this method returns the total number of students
    public int getStudentCount() {
        return studentCount;
    }
}

