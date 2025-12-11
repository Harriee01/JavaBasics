import java.text.SimpleDateFormat;
import java.util.Date;
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
            //displays menu
            System.out.println("\n=========================================");
            System.out.println("      STUDENT GRADE MANAGEMENT SYSTEM     ");
            System.out.println("=========================================");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Record Grade");
            System.out.println("4. View Grade Report");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();//user's choice

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

                    // Show Honors Eligibility **only for Honors students**
//                    if (student instanceof HonorsStudent) {
//                        HonorsStudent honorsStudent = (HonorsStudent) student;
//                        boolean eligible = honorsStudent.checkHonorsEligibility();
//                        System.out.println("Honors Eligible: " + (eligible ? "YES" : "NO"));
//                    }

                    break;


                // 2. view students
                case "2":
                    studentManager.viewAllStudents();//this calls the viewAllStudents method to display all students
                    System.out.println();
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    break;


                // 3. record grade
                case "3":
                    System.out.print("Enter Student ID: ");// get student ID from user
                    String studentId = scanner.nextLine();

                    Student student1 = studentManager.findStudent(studentId);//this finds the student using StudentManager
                    LocalDate currentDate = LocalDate.now();


                    if (student1 == null) {//checking if the student exists
                        System.out.println("Student not found.");
                        System.out.println("Press Enter to continue...");
                        scanner.nextLine();
                        break;
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

                    // validating the grade using the student's validateGrade method
                    if (!student1.validateGrade(gradeValue)) {
                        System.out.println("Error: Invalid grade. Grade must be between 0 and 100.");
                        System.out.print("Press Enter to continue...");
                        scanner.nextLine();
                        break;
                    }

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
                    System.out.print("Enter Student ID: ");
                    String id = scanner.nextLine();

                    Student found = studentManager.findStudent(id);

                    if (found == null) {
                        System.out.println("Student not found.");
                        break;
                    }

                    gradeManager.viewGradesByStudent(id);
                    break;


                // 5. exit system

                case "5":
                    System.out.println("Exiting system. Goodbye!");
                    running = false;
                    break;


                // INVALID OPTION

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }
}


