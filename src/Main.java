import java.util.Scanner;// allows user input
import java.time.LocalDate;

public class Main {// the entry point of the application


//this is where the program execution begins
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);//new scanner object to read user input from console
        StudentManager studentManager = new StudentManager();// new student manager object to manage students
        GradeManager gradeManager = new GradeManager();//new grade manager object to manage student grades
        studentManager.setGradeManager(gradeManager);//connects both student and grade managers so that they can work together

        //creating arrays of available subjects;both core and elective
        CoreSubject[] coreSubjects = {
                new CoreSubject("Mathematics", "MATH101"),
                new CoreSubject("English", "ENG102"),
                new CoreSubject("Science", "SCI103"),
        };

        ElectiveSubject[] electiveSubjects = {
                new ElectiveSubject("Music", "MUS101"),
                new ElectiveSubject("Art", "ART102"),
                new ElectiveSubject("Physical Education", "PE103"),
        };


        boolean running = true; //keeps the menu running until the user exits

        while (running) {//continues running until user chooses to exit
            //displays updated menu
            System.out.println("\n=========================================");
            System.out.println("      STUDENT GRADE MANAGEMENT SYSTEM     ");
            System.out.println("=========================================");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Record Grade");
            System.out.println("4. View Grade Report");
            System.out.println("5. Export Grade Report           [NEW]");
            System.out.println("6. Calculate Student GPA         [NEW]");
            System.out.println("7. Bulk Import Grades            [NEW]");
            System.out.println("8. View Class Statistics         [NEW]");
            System.out.println("9. Search Students               [NEW]");
            System.out.println("10. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();//user's choice

            // TRY-CATCH block for exception handling
            // This catches any exceptions and displays user-friendly messages
         try {
             switch (choice) { // switch here is used to execute an action based on the user's input


                 // 1. ADD STUDENT
                 case "1":
                     System.out.println("\n--- Register New Student ---");
                     //getting student information from the user
                     System.out.print("Enter student name: ");
                     String name = scanner.nextLine();

                     System.out.print("Enter age: ");
                     int age = Integer.parseInt(scanner.nextLine());//if user should type in words it converts it to integer

                     System.out.print("Enter email: ");
                     String email = scanner.nextLine();

                     System.out.print("Enter phone: ");
                     String phone = scanner.nextLine();

                     System.out.print("Select type (1 = Regular, 2 = Honors): ");
                     int type = Integer.parseInt(scanner.nextLine());

                     //creating the suitable student type based on the choice of the user
                     Student newstudent;
                     if (type == 1) {
                         newstudent = new RegularStudent(name, email, phone, age);
                     } else if (type == 2) {
                         newstudent = new HonorsStudent(name, email, phone, age);
                     } else {
                         System.out.println("Invalid type selected.");
                         break;
                     }
                     //adding student to manager
                     studentManager.addStudent(newstudent);

                     //Display confirmation
                     System.out.println("Student registered successfully!");
                     System.out.println("========================================");
                     System.out.println("Student ID     : " + newstudent.getStudentId());
                     System.out.println("Name           : " + newstudent.getStudentName());
                     System.out.println("Age            : " + newstudent.getStudentAge());
                     System.out.println("Email          : " + newstudent.getStudentEmail());
                     System.out.println("Phone          : " + newstudent.getStudentPhone());
                     System.out.println("Type           : " + newstudent.getStudentType());
                     System.out.println("Status         : " + newstudent.getStudentStatus());
                     System.out.println();
                     System.out.println("Press Enter to continue...");
                     scanner.nextLine();

                     break;


                 // 2. view students
                 case "2":
                     studentManager.viewAllStudents();//this calls the viewAllStudents method to display all students
                     System.out.println();
                     System.out.println("Press Enter to continue...");
                     scanner.nextLine();
                     break;


                 // 3. record grade, now with exception handling
                 case "3":
                     System.out.print("Enter Student ID: ");// get student ID from user
                     String studentId = scanner.nextLine();

                     Student student1 = studentManager.findStudent(studentId);//this finds the student using StudentManager
                     LocalDate currentDate = LocalDate.now();


                     if (student1 == null) {//checking if the student exists
                         //throw exception if the student is not found
                        throw new StudentNotFoundException(studentId);
                     }

                     // Display student details
                     System.out.println();
                     System.out.println("Student Details:");
                     System.out.println("Name: " + student1.getStudentName());
                     System.out.println("Type: " + student1.getStudentType() + " Student");
                     System.out.println("Current Average: " + String.format("%.1f%%",
                             gradeManager.calculateOverallAverage(studentId)));
                     System.out.println("Status: " + student1.getStudentStatus());
                     System.out.println();

                     // Select subject type
                     System.out.println("Select subject type:");
                     System.out.println("1. Core");
                     System.out.println("2. Elective");
                     int subjectType = Integer.parseInt(scanner.nextLine());

                     Subject selectedSubject = null;

                     // Core subjects
                     if (subjectType == 1) {
                         //show available core subjects
                         for (int i = 0; i < coreSubjects.length; i++) {
                             System.out.println((i + 1) + ". " + coreSubjects[i].getSubjectName());
                         }
                         System.out.println();
                         System.out.print("Select subject (1-" + coreSubjects.length + "): ");
                         int subjectChoice = scanner.nextInt();
                         scanner.nextLine();
                         // get the selected subject from array (subtract 1 for zero-based index)
                         selectedSubject = coreSubjects[subjectChoice - 1];

                     }
                     // Elective subjects
                     else{
                         // Show available elective subjects
                         System.out.println("Available Elective Subjects:");
                         for (int i = 0; i < electiveSubjects.length; i++) {
                             System.out.println((i + 1) + ". " + electiveSubjects[i].getSubjectName());
                         }
                         System.out.println();
                         System.out.print("Select subject (1-" + electiveSubjects.length + "): ");
                         int subjectChoice = scanner.nextInt();
                         scanner.nextLine();
                         // Get the selected subject from array
                         selectedSubject = electiveSubjects[subjectChoice - 1];
                     }

//                    // getting  grade value from user
                     System.out.println();
                     System.out.print("Enter grade (0-100): ");
                     double gradeValue = scanner.nextDouble();
                     scanner.nextLine();

                     // THROW exception if grade is invalid
                     if (!student1.validateGrade(gradeValue)) {
                         throw new InvalidGradeException(gradeValue);
                     }

                     // validating the grade using the student's validateGrade method
//                     if (!student1.validateGrade(gradeValue)) {
//                         System.out.println("Error: Invalid grade. Grade must be between 0 and 100.");
//                         System.out.print("Press Enter to continue...");
//                         scanner.nextLine();
//                         break;
//                     }

                     // displays grade confirmation before recording
                     System.out.println();
                     System.out.println("GRADE CONFIRMATION");
                     System.out.println("───────────────────────────────────────────────────");
                     System.out.println();
                     System.out.println("Grade ID: " + String.format("GRD%03d", gradeManager.getGradeCount() + 1));
                     System.out.println("Student: " + studentId + " - " + student1.getStudentName());
                     System.out.println("Subject: " + selectedSubject.getSubjectName() + " (" + selectedSubject.getSubjectType() + ")");
                     System.out.println("Grade: " + gradeValue + "%");

//                    String currentDate = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
                     System.out.println("Date: " + currentDate);// gets the current date
                     System.out.println("───────────────────────────────────────────────────");
                     System.out.println();

                     // asking for confirmation
                     System.out.print("Confirm grade? (Y/N): ");
                     String confirmation = scanner.nextLine();

                     // checking if the user confirmed
                     if (confirmation.equalsIgnoreCase("Y")) {
                         // Create a new Grade object
                         Grade newGrade = new Grade(studentId, selectedSubject, gradeValue);
                         // Add the grade to GradeManager
                         gradeManager.addGrade(newGrade);
                         System.out.println();
                         System.out.println("✓ Grade recorded successfully!");
                     } else {
                         System.out.println();
                         System.out.println("Grade recording cancelled.");
                     }
                     System.out.println();
                     System.out.print("Press Enter to continue...");
                     scanner.nextLine();
                     break;


                 // 4.View grade report

                 case "4":
                     System.out.print("Enter Student ID: ");//gets the student ID from the user
                     String reportStudentId = scanner.nextLine();

                     Student foundStudent = studentManager.findStudent(reportStudentId);//finding the student

                     if (foundStudent == null) {// checking if the student exists
                         System.out.println("Student not found.");
                         System.out.print("Press Enter to continue...");
                         scanner.nextLine();
                         break;
                     }

                     // displaying student header information
                     System.out.println();
                     System.out.println("Student: " + reportStudentId + " - " + foundStudent.getStudentName());
                     System.out.println("Type: " + foundStudent.getStudentType() + " Student");

                     // calculating current average
                     double currentAvg = gradeManager.calculateOverallAverage(reportStudentId);
                     System.out.println("Current Average: " + String.format("%.1f%%", currentAvg));

                     // determining and displaying passing status
                     String passingStatus;
                     if (currentAvg == 0) {
                         passingStatus = "No grades recorded";
                     } else if (foundStudent.isPassing()) {
                         passingStatus = "PASSING ";
                     } else {
                         passingStatus = "FAILING ";
                     }
                     System.out.println("Status: " + passingStatus);
                     System.out.println();

                     // getting all grades for a particular student
                     Grade[] studentGrades = gradeManager.viewGradesByStudent(reportStudentId);

                     // checking if the student has any grades
                     if (studentGrades.length == 0) {
                         System.out.println("No grades recorded for this student.");
                         System.out.println("───────────────────────────────────────────────────");
                         System.out.println();
                         System.out.print("Press Enter to continue...");
                         scanner.nextLine();
                         break;
                     }

                     // displaying grade history in table format
                     System.out.println("GRADE HISTORY");
                     System.out.println("───────────────────────────────────────────────────────────────────────────────");
                     System.out.printf("%-10s | %-12s | %-20s | %-10s | %-10s%n",
                             "GRD ID", "DATE", "SUBJECT", "TYPE", "GRADE");
                     System.out.println("───────────────────────────────────────────────────────────────────────────────");

                     // looping through grades in reverse order (newest first)
                     for (int i = studentGrades.length - 1; i >= 0; i--) {
                         Grade newgrade = studentGrades[i];
                         System.out.printf("%-10s | %-12s | %-20s | %-10s | %-10.1f%%%n",
                                 newgrade.getGradeId(),
                                 newgrade.getDate(),
                                 newgrade.getSubject().getSubjectName(),
                                 newgrade.getSubject().getSubjectType(),
                                 newgrade.getGrade());
                     }
                     System.out.println("───────────────────────────────────────────────────────────────────────────────");

                     // calculating and displaying averages by category
                     System.out.println();
                     System.out.println("Total Grades: " + studentGrades.length);

                     // calculating core subjects average
                     double coreAvg = gradeManager.calculateCoreAverage(reportStudentId);
                     System.out.println("Core Subjects Average: " + String.format("%.1f%%", coreAvg));

                     // calculating elective subjects average
                     double electiveAvg = gradeManager.calculateElectiveAverage(reportStudentId);
                     System.out.println("Elective Subjects Average: " + String.format("%.1f%%", electiveAvg));

                     // displaying overall average
                     System.out.println("Overall Average: " + String.format("%.1f%%", currentAvg));
                     System.out.println();

                     // displaying performance summary
                     System.out.println("Performance Summary:");

                     // checking if the student is passing all core subjects
                     if (coreAvg >= foundStudent.getPassingGrade()) {
                         System.out.println("Passing all core subjects");
                     } else if (coreAvg > 0) {
                         System.out.println("Not passing core subjects (need " +
                                 foundStudent.getPassingGrade() + "%)");
                     }

                     // checking the overall passing status
                     if (currentAvg >= foundStudent.getPassingGrade()) {
                         System.out.println("Meeting passing grade requirement (" +
                                 foundStudent.getPassingGrade() + "%)");
                     } else if (currentAvg > 0) {
                         System.out.println("Below passing grade requirement (" +
                                 foundStudent.getPassingGrade() + "%)");
                     }

                     // for honors students, this checks honors eligibility
                     if (foundStudent instanceof HonorsStudent) {
                         HonorsStudent honorsStudent01 = (HonorsStudent) foundStudent;
                         // updating honors eligibility
                         honorsStudent01.checkHonorsEligibility();
                         if (honorsStudent01.isHonorsEligible()) {
                             System.out.println("Eligible for honors recognition (85%+)");
                         } else if (currentAvg > 0) {
                             System.out.println("Not eligible for honors (need 85%+)");
                         }
                     }

                     System.out.println();
                     System.out.print("Press Enter to continue...");
                     scanner.nextLine();
                     break;


//                gradeManager.viewGradesByStudent(reportStudentId);
//                    break;


                 case "5":  // EXPORT GRADE REPORT (NEW)
                     System.out.println();
                     System.out.print("Enter choice: 5");
                     System.out.println();
                     System.out.println();
                     System.out.println("═══════════════════════════════════════════════════");
                     System.out.println("EXPORT GRADE REPORT");
                     System.out.println("───────────────────────────────────────────────────");
                     System.out.println();

                     System.out.print("Enter Student ID: ");
                     String exportStudentId = scanner.nextLine();

                     Student exportStudent = studentManager.findStudent(exportStudentId);

                     if (exportStudent == null) {
                         throw new StudentNotFoundException(exportStudentId);
                     }

                     System.out.println();
                     System.out.println("Student: " + exportStudentId + " - " + exportStudent.getStudentName());
                     System.out.println("Type: " + exportStudent.getStudentType() + " Student");

                     int totalGrades = gradeManager.getGradeCountForStudent(exportStudentId);
                     System.out.println("Total Grades: " + totalGrades);
                     System.out.println();

                     System.out.println("Export options:");
                     System.out.println("1. Summary Report (overview only)");
                     System.out.println("2. Detailed Report (all grades)");
                     System.out.println("3. Both");
                     System.out.println();
                     System.out.print("Select option (1-3): ");
                     int exportOption = scanner.nextInt();
                     scanner.nextLine();

                     System.out.println();
                     System.out.print("Enter filename (without extension): ");
                     String filename = scanner.nextLine();

                     boolean detailed = (exportOption == 2 || exportOption == 3);
                     FileExporter.exportGradeReport(exportStudent, gradeManager, filename, detailed);

                     System.out.println();
                     System.out.print("Press Enter to continue...");
                     scanner.nextLine();
                     break;

                 case "6":  // CALCULATE STUDENT GPA (NEW)
                     System.out.println();
                     System.out.print("Enter choice: 6");
                     System.out.println();
                     System.out.println();
                     System.out.println("═══════════════════════════════════════════════════");
                     System.out.println("CALCULATE STUDENT GPA");
                     System.out.println("───────────────────────────────────────────────────");
                     System.out.println();

                     System.out.print("Enter Student ID: ");
                     String gpaStudentId = scanner.nextLine();

                     Student gpaStudent = studentManager.findStudent(gpaStudentId);

                     if (gpaStudent == null) {
                         throw new StudentNotFoundException(gpaStudentId);
                     }

                     System.out.println();
                     System.out.println("Student: " + gpaStudentId + " - " + gpaStudent.getStudentName());
                     System.out.println("Type: " + gpaStudent.getStudentType() + " Student");

                     double overallAvg = gradeManager.calculateOverallAverage(gpaStudentId);
                     System.out.println("Overall Average: " + String.format("%.1f%%", overallAvg));
                     System.out.println();

                     // Check if student has grades
                     Grade[] gpaGrades = gradeManager.viewGradesByStudent(gpaStudentId);
                     if (gpaGrades.length == 0) {
                         System.out.println("No grades recorded for this student.");
                         System.out.print("Press Enter to continue...");
                         scanner.nextLine();
                         break;
                     }

                     System.out.println("GPA CALCULATION (4.0 Scale)");
                     System.out.println();
                     System.out.println("───────────────────────────────────────────────────");
                     System.out.printf("%-20s | %-10s | %-15s%n", "Subject", "Grade", "GPA Points");
                     System.out.println("───────────────────────────────────────────────────");

                     // Display each subject with its GPA conversion
                     for (Grade g : gpaGrades) {
                         double gpa = GPACalculator.percentageToGPA(g.getGrade());
                         String letterGrade = GPACalculator.gpaToLetterGrade(gpa);
                         System.out.printf("%-20s | %-10s | %.1f (%s)%n",
                                 g.getSubject().getSubjectName(),
                                 String.format("%.0f%%", g.getGrade()),
                                 gpa,
                                 letterGrade);
                     }
                     System.out.println("───────────────────────────────────────────────────");
                     System.out.println();

                     // Calculate cumulative GPA
                     double cumulativeGPA = gpaStudent.calculateGPA();
                     String letterGrade = GPACalculator.gpaToLetterGrade(cumulativeGPA);

                     System.out.println("Cumulative GPA: " + String.format("%.2f", cumulativeGPA) + " / 4.0");
                     System.out.println("Letter Grade: " + letterGrade);

                     // Calculate class rank (simplified)
                     Student[] allStudents = studentManager.getAllStudents();
                     int rank = 1;
                     for (Student s : allStudents) {
                         if (s.calculateGPA() > cumulativeGPA) {
                             rank++;
                         }
                     }
                     System.out.println("Class Rank: " + rank + " of " + studentManager.getStudentCount());
                     System.out.println();

                     // Performance analysis
                     System.out.println("Performance Analysis:");
                     if (cumulativeGPA >= 3.5) {
                         System.out.println("✓ Excellent performance (" + String.format("%.2f", cumulativeGPA) + " GPA)");
                     } else if (cumulativeGPA >= 3.0) {
                         System.out.println("✓ Good performance (" + String.format("%.2f", cumulativeGPA) + " GPA)");
                     } else if (cumulativeGPA >= 2.0) {
                         System.out.println("  Average performance (" + String.format("%.2f", cumulativeGPA) + " GPA)");
                     } else {
                         System.out.println("✗ Below average performance (" + String.format("%.2f", cumulativeGPA) + " GPA)");
                     }

                     if (gpaStudent instanceof HonorsStudent) {
                         HonorsStudent honorsStudent3 = (HonorsStudent) gpaStudent;
                         if (honorsStudent3.checkHonorsEligibility()) {
                             System.out.println("✓ Honors eligibility maintained");
                         }
                         System.out.println("✓ Above class average (" + String.format("%.2f", studentManager.getAverageClassGrade() / 100 * 4.0) + " GPA)");
                     }

                     System.out.println();
                     System.out.print("Press Enter to continue...");
                     scanner.nextLine();
                     break;

                 case "7":  // BULK IMPORT GRADES (NEW)
                     System.out.println();
                     System.out.print("Enter choice: 7");
                     System.out.println();
                     System.out.println();
                     System.out.println("═══════════════════════════════════════════════════");
                     System.out.println("BULK IMPORT GRADES");
                     System.out.println("───────────────────────────────────────────────────");
                     System.out.println();

                     System.out.println("Place your CSV file in: ./imports/");
                     System.out.println();
                     System.out.println("CSV Format Required:");
                     System.out.println("StudentID,SubjectName,SubjectType,Grade");
                     System.out.println("Example: STU001,Mathematics,Core,85");
                     System.out.println();

                     System.out.print("Enter filename (without extension): ");
                     String csvFilename = scanner.nextLine();

                     System.out.println();

                     // Import grades using BulkImportService
                     int[] results = BulkImportService.importGradesFromCSV(
                             csvFilename, studentManager, gradeManager,
                             coreSubjects, electiveSubjects
                     );

                     // Display import summary
                     System.out.println();
                     System.out.println("IMPORT SUMMARY");
                     System.out.println("───────────────────────────────────────────────────");
                     System.out.println();
                     System.out.println("Total Rows: " + results[0]);
                     System.out.println("Successfully Imported: " + results[1]);
                     System.out.println("Failed: " + results[2]);
                     System.out.println();

                     if (results[1] > 0) {
                         System.out.println("✓ Import completed!");
                         System.out.println("  " + results[1] + " grades added to system");
                     }

                     if (results[2] > 0) {
                         System.out.println();
                         System.out.println("Failed Records:");
                     }

                     System.out.println();
                     System.out.print("Press Enter to continue...");
                     scanner.nextLine();
                     break;

                 case "8":  // VIEW CLASS STATISTICS (NEW)
                     System.out.println();
                     System.out.print("Enter choice: 8");
                     System.out.println();
                     System.out.println();
                     System.out.println("═══════════════════════════════════════════════════");
                     System.out.println("CLASS STATISTICS");
                     System.out.println("───────────────────────────────────────────────────");
                     System.out.println();

                     System.out.println("Total Students: " + studentManager.getStudentCount());
                     System.out.println("Total Grades Recorded: " + gradeManager.getGradeCount());
                     System.out.println();

                     // Get all grades for analysis
                     Grade[] allGrades = gradeManager.getAllGrades();

                     if (allGrades.length == 0) {
                         System.out.println("No grades recorded yet.");
                         System.out.print("Press Enter to continue...");
                         scanner.nextLine();
                         break;
                     }

                     // Convert grades to array of doubles for statistics
                     double[] gradeValues = new double[allGrades.length];
                     for (int i = 0; i < allGrades.length; i++) {
                         gradeValues[i] = allGrades[i].getGrade();
                     }

                     // GRADE DISTRIBUTION - count grades in each range
                     System.out.println("GRADE DISTRIBUTION");
                     System.out.println("───────────────────────────────────────────────────");

                     int countA = 0, countB = 0, countC = 0, countD = 0, countF = 0;
                     for (double grade : gradeValues) {
                         if (grade >= 90) countA++;
                         else if (grade >= 80) countB++;
                         else if (grade >= 70) countC++;
                         else if (grade >= 60) countD++;
                         else countF++;
                     }

                     // Display with percentages and visual bar
                     System.out.printf("90-100%% (A): %-20s %.1f%% (%d grades)%n",
                             createBar(countA, allGrades.length),
                             (countA * 100.0 / allGrades.length), countA);
                     System.out.printf("80-89%%  (B): %-20s %.1f%% (%d grades)%n",
                             createBar(countB, allGrades.length),
                             (countB * 100.0 / allGrades.length), countB);
                     System.out.printf("70-79%%  (C): %-20s %.1f%% (%d grades)%n",
                             createBar(countC, allGrades.length),
                             (countC * 100.0 / allGrades.length), countC);
                     System.out.printf("60-69%%  (D): %-20s %.1f%% (%d grades)%n",
                             createBar(countD, allGrades.length),
                             (countD * 100.0 / allGrades.length), countD);
                     System.out.printf("0-59%%   (F): %-20s %.1f%% (%d grade)%n",
                             createBar(countF, allGrades.length),
                             (countF * 100.0 / allGrades.length), countF);
                     System.out.println();

                     // STATISTICAL ANALYSIS
                     System.out.println("STATISTICAL ANALYSIS");
                     System.out.println("───────────────────────────────────────────────────");
                     System.out.println();
                     System.out.println("Mean (Average):     " + String.format("%.1f%%", StatisticsCalculator.calculateMean(gradeValues)));
                     System.out.println("Median:             " + String.format("%.1f%%", StatisticsCalculator.calculateMedian(gradeValues)));
                     System.out.println("Mode:               " + String.format("%.1f%%", StatisticsCalculator.calculateMode(gradeValues)));
                     System.out.println("Standard Deviation: " + String.format("%.1f%%", StatisticsCalculator.calculateStandardDeviation(gradeValues)));

                     double min = StatisticsCalculator.findMin(gradeValues);
                     double max = StatisticsCalculator.findMax(gradeValues);
                     System.out.println("Range:              " + String.format("%.1f%% (%.0f%% ~ %.0f%%)", max - min, min, max));
                     System.out.println();

                     // Find highest and lowest grades with student names
                     System.out.println("Highest Grade:      " + String.format("%.0f%%", max));
                     for (Grade grades2 : allGrades) {
                         if (grades2.getGrade() == max) {
                             Student student4 = studentManager.findStudent(grades2.getStudentId());
                             System.out.println("                    (" + grades2.getStudentId() + " - " +
                                     (student4 != null ? student4.getStudentName() : "Unknown") + " in " + grades2.getSubject().getSubjectName() + ")");
                             break;
                         }
                     }

                     System.out.println("Lowest Grade:       " + String.format("%.0f%%", min));
                     for (Grade grades3 : allGrades) {
                         if (grades3.getGrade() == min) {
                             Student student5 = studentManager.findStudent(grades3.getStudentId());
                             System.out.println("                    (" + grades3.getStudentId() + " - " +
                                     (student5 != null ? student5.getStudentName() : "Unknown") + " in " + grades3.getSubject().getSubjectName() + ")");
                             break;
                         }
                     }
                     System.out.println();

                     // SUBJECT PERFORMANCE
                     System.out.println("SUBJECT PERFORMANCE");
                     System.out.println("───────────────────────────────────────────────────");
                     System.out.println();

                     // Calculate averages for core subjects
                     System.out.println("Core Subjects:");
                     double totalCoreAvg = 0;
                     for (CoreSubject coreSubject : coreSubjects) {
                         Grade[] subjectGrades = gradeManager.getGradesBySubject(coreSubject.getSubjectName());
                         if (subjectGrades.length > 0) {
                             double[] subjectValues = new double[subjectGrades.length];
                             for (int i = 0; i < subjectGrades.length; i++) {
                                 subjectValues[i] = subjectGrades[i].getGrade();
                             }
                             double avg = StatisticsCalculator.calculateMean(subjectValues);
                             totalCoreAvg += avg;
                             System.out.println("  " + coreSubject.getSubjectName() + ": " + String.format("%.1f%%", avg));
                         }
                     }
                     double coreAvgOverall = totalCoreAvg / coreSubjects.length;
                     System.out.println("                    " + String.format("%.1f%%", coreAvgOverall) + " average");
                     System.out.println();

                     // Calculate averages for elective subjects
                     System.out.println("Elective Subjects:");
                     double totalElectiveAvg = 0;
                     for (ElectiveSubject electiveSubject : electiveSubjects) {
                         Grade[] subjectGrades = gradeManager.getGradesBySubject(electiveSubject.getSubjectName());
                         if (subjectGrades.length > 0) {
                             double[] subjectValues = new double[subjectGrades.length];
                             for (int i = 0; i < subjectGrades.length; i++) {
                                 subjectValues[i] = subjectGrades[i].getGrade();
                             }
                             double avg = StatisticsCalculator.calculateMean(subjectValues);
                             totalElectiveAvg += avg;
                             System.out.println("  " + electiveSubject.getSubjectName() + ": " + String.format("%.1f%%", avg));
                         }
                     }
                     double electiveAvgOverall = totalElectiveAvg / electiveSubjects.length;
                     System.out.println("                    " + String.format("%.1f%%", electiveAvgOverall) + " average");
                     System.out.println();

                     // STUDENT TYPE COMPARISON
                     System.out.println("STUDENT TYPE COMPARISON");
                     System.out.println("───────────────────────────────────────────────────");
                     System.out.println();

                     // Calculate averages for regular vs honors students
                     Student[] regularStudents = studentManager.searchByType("Regular");
                     Student[] honorsStudents = studentManager.searchByType("Honors");

                     double regularAvg = 0;
                     int regularCount = 0;
                     for (Student student5 : regularStudents) {
                         double avg = student5.calculateAverageGrade();
                         if (avg > 0) {
                             regularAvg += avg;
                             regularCount++;
                         }
                     }
                     if (regularCount > 0) regularAvg /= regularCount;

                     double honorsAvg = 0;
                     int honorsCount = 0;
                     for (Student student5 : honorsStudents) {
                         double avg = student5.calculateAverageGrade();
                         if (avg > 0) {
                             honorsAvg += avg;
                             honorsCount++;
                         }
                     }
                     if (honorsCount > 0) honorsAvg /= honorsCount;

                     System.out.println("Regular Students:   " + String.format("%.1f%%", regularAvg) +
                             " average (" + regularStudents.length + " students)");
                     System.out.println("Honors Students:    " + String.format("%.1f%%", honorsAvg) +
                             " average (" + honorsStudents.length + " students)");
                     System.out.println();

                     System.out.print("Press Enter to continue...");
                     scanner.nextLine();
                     break;


                 // 5. exit system

//                 case "5":
//                     System.out.println("Exiting system. Goodbye!");
//                     running = false;
//                     break;
//
//
//                 // INVALID OPTION
//
//                 default:
//                     System.out.println("Invalid choice. Try again.");
//             }
         } catch (StudentNotFoundException exception) {
             // CATCH specific exception for student not found
             System.out.println();
             System.out.println("✗ ERROR: " + exception.getClass().getSimpleName());
             System.out.println("  " + exception.getMessage());
             System.out.println();

             // Show available student IDs to help user
             System.out.println("Available student IDs: ");
             Student[] allStudents = studentManager.getAllStudents();
             for (int i = 0; i < allStudents.length && i < 5; i++) {
                 System.out.print(allStudents[i].getStudentId());
                 if (i < allStudents.length - 1 && i < 4) System.out.print(", ");
             }
             System.out.println();
             System.out.println();
             System.out.print("Try again? (Y/N): ");
             String retry = scanner.nextLine();
             if (retry.equalsIgnoreCase("Y")) {
                 // User can retry the operation
             }
         } catch (InvalidGradeException e){
                 // CATCH specific exception for invalid grade
                 System.out.println();
                 System.out.println("✗ ERROR: " + e.getClass().getSimpleName());
                 System.out.println("  " + e.getMessage());
                 System.out.println();
                 System.out.print("Try again? (Y/N): ");
                 scanner.nextLine();
             }
             catch (Exception e) {
                 // CATCH any other unexpected exceptions
                 System.out.println();
                 System.out.println("✗ An unexpected error occurred:");
                 System.out.println("  " + e.getMessage());
                 System.out.println();
                 System.out.print("Press Enter to continue...");
                 scanner.nextLine();
             }



            scanner.close();
        }
         }

         }

         // HELPER METHOD - creates visual bar for grade distribution
    // Takes count and total, returns string of █ characters
    private static String createBar(int count, int total) {
        int barLength = (int) ((count * 20.0) / total);  // Scale to max 20 characters
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < barLength; i++) {
            bar.append("█");
        }
        return bar.toString();
    }




