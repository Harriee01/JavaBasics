public class GradeManager {// uses composition; manages all the grades in the system

private Grade[] grades = new Grade[200];  // Array to store all grades with length 200
private int gradeCount = 0;               // Tracks how many grades have been added, starting with zero

// Method to add a grade to the array
public void addGrade(Grade grade) {
    if (gradeCount < grades.length) {
        grades[gradeCount] = grade;// add grade
        gradeCount++;//increment count
    } else {
        System.out.println("Grade storage is full.");
    }
}

//method to get all grades for a specific student
public Grade[] viewGradesByStudent(String studentId) {
    int count = 0;//counting how many grades a particular student has
    for (int i = 0; i < gradeCount; i++) {
        if (grades[i].getStudentId().equals(studentId)) {
            count++;
        }
    }
    // creating an array of the exact size needed for a student's grades
    Grade[] studentGrades = new Grade[count];
    int index = 0;
    //filling the array with student's grades
    for (int i = 0; i < gradeCount; i++) {
        if (grades[i].getStudentId().equals(studentId)) {
            studentGrades[index] = grades[i];
            index++;
        }
    }
    return studentGrades;
}


// This method calculates the average of the core subject grades
public double calculateCoreAverage(String studentId) {
    double total = 0; //sum of all core grades
    int counts = 0; //number of core subjects

    //Looping through all grades
    for (int i = 0; i < gradeCount; i++) {
        //Nested if statement to check if a grade belongs to the student in question and whether the subject is a core subject
        if (grades[i].getStudentId().equals(studentId)) {
            if (grades[i].getSubject() instanceof CoreSubject) {
                total += grades[i].getGrade();
                counts++;


            }
        }

    }

    if (counts ==0) return 0.0; // to avoid division by zero
    return total/counts; //returns the average
}

// this method calculates the average of the elective subject grades; same logic as calculating the average of core subjects
public double calculateElectiveAverage(String studentId){
    double total = 0;
    int counts = 0;
    for (int i = 0; i < gradeCount; i++) {
        if (grades[i].getStudentId().equals(studentId)) {
            if (grades[i].getSubject() instanceof ElectiveSubject) {
                total += grades[i].getGrade();
                counts++;
            }
        }
    }
    if (counts ==0) return 0.0;
    return total/counts;
}

//this method calculates overall average(all subjects)
public double calculateOverallAverage(String studentId) {
    double total = 0;
    int count = 0;

    for (int i = 0; i < gradeCount; i++) {
        if (grades[i].getStudentId().equals(studentId)) {
            total += grades[i].getGrade();
            count++;
        }
    }

    if (count == 0) return 0.0;
    return total / count;
    }

    //this method get the count of grades for a specific student
    public int getGradeCountForStudent(String studentId) {
        int count = 0;
        for (int i = 0; i < gradeCount; i++) {
            if (grades[i].getStudentId().equals(studentId)) {
                count++;
            }
        }
        return count;
    }

    // this method gets the  total number of grades
    public int getGradeCount() {
        return gradeCount;
    }

    // this method gets all grades (for statistics)
    public Grade[] getAllGrades() {
        Grade[] allGrades = new Grade[gradeCount];
        for (int i = 0; i < gradeCount; i++) {
            allGrades[i] = grades[i];
        }
        return allGrades;
    }

}



























//        double coreAverage = calculateCoreAverage(studentId)
//        double electiveAverage = calculateElectiveAverage(studentId);
//        double overallAverage = calculateOverallAverage(studentId);
//
//        System.out.println("\n--- Averages ---");
//        System.out.println("Core Subjects Average: " + (coreAverage >= 0 ? coreAverage : "N/A"));
//        System.out.println("Elective Subjects Average: " + (electiveAverage >= 0 ? electiveAverage : "N/A"));
//        System.out.println("Overall Average: " + (overallAverage >= 0 ? overallAverage : "N/A"));
//
//        // Performance summary
//        System.out.println("\n--- Performance Summary ---");
//        if (overallAverage >= 0) {
//            if (overallAverage >= 50) System.out.println("Status: Passing");
//            else System.out.println("Status: Failing");
//        } else {
//            System.out.println("No grades available to determine performance.");
//        }
//
//        System.out.println("--------------------------------------\n");
//    }

    // Calculates average of CORE subjects
//    public double calculateCoreAverage(String studentId) {
//        double sum = 0;
//        int count = 0;
//
//        for (int i = 0; i < gradeCount; i++) {
//            Grade grade = grades[i];
//            if (grade != null && grade.getStudentId().equals(studentId)) {
//                if (grade.getSubject().getSubjectType().equals("Core")) {
//                    sum += grade.getGrade();
//                    count++;
//                }
//            }
//        }
//
//        return (count == 0) ? -1 : (sum / count); // -1 means no core grades
//    }
//
//    // Calculates average of ELECTIVE subjects
//    public double calculateElectiveAverage(String studentId) {
//        double sum = 0;
//        int count = 0;
//
//        for (int i = 0; i < gradeCount; i++) {
//            Grade grade = grades[i];
//            if (grade != null && grade.getStudentId().equals(studentId)) {
//                if (grade.getSubject().getSubjectType().equals("Elective")) {
//                    sum += grade.getGrade();
//                    count++;
//                }
//            }
//        }
//
//        return (count == 0) ? -1 : (sum / count); // -1 means no elective grades
//    }
//
//    // Calculates overall average (Core + Elective)
//    public double calculateOverallAverage(String studentId) {
//        double sum = 0;
//        int count = 0;
//
//        for (int i = 0; i < gradeCount; i++) {
//            Grade grade = grades[i];
//            if (grade != null && grade.getStudentId().equals(studentId)) {
//                sum += grade.getGrade();
//                count++;
//            }
//        }
//
//        return (count == 0) ? -1 : (sum / count); // -1 means no grades
//    }
//
//    // Returns how many grades are stored
//    public int getGradeCount() {
//        return gradeCount;
//    }
//}
