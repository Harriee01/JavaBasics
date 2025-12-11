import java.util.Scanner;// allows user input
import java.time.LocalDate;

public class Main {// the entry point of the application


//this is where the program execution begins
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);//new scanner object to read user input from console
        StudentManager studentManager = new StudentManager();// new student manager object to manage students
        GradeManager gradeManager = new GradeManager();//new grade manager object to manage student grades
        studentManager.setGradeManager(gradeManager);//connects both student and grade managers so that they can work together

        //creating arrays of available students;both core and elective
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
                    Student student;
                    if (type == 1) {
                        student = new RegularStudent(name, email, phone, age);
                    } else if (type == 2) {
                        student = new HonorsStudent(name, email, phone, age);
                    } else {
                        System.out.println("Invalid type selected.");
                        break;
                    }
                    //adding student to manager
                    studentManager.addStudent(student);

                    //Display confirmation
                    System.out.println("Student registered successfully!");
                    System.out.println("========================================");
                    System.out.println("Student ID     : " + student.getStudentId());
                    System.out.println("Name           : " + student.getStudentName());
                    System.out.println("Age            : " + student.getStudentAge());
                    System.out.println("Email          : " + student.getStudentEmail());
                    System.out.println("Phone          : " + student.getStudentPhone());
                    System.out.println("Type           : " + student.getStudentType());
                    System.out.println("Status         : " + student.getStudentStatus());
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

//                        System.out.println("Core Subjects:");
//                        System.out.println("1. Mathematics");
//                        System.out.println("2. English");
//                        System.out.println("3. Science");

                       // int corePick = Integer.parseInt(scanner.nextLine());

//                        if (corePick == 1) subject = new CoreSubject("Mathematics", "C101");
//                        else if (corePick == 2) subject = new CoreSubject("Science", "C102");
//                        else if (corePick == 3) subject = new CoreSubject("English", "C103");
//                        else {
//                            System.out.println("Invalid core subject.");
//                            break;
//                        }

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

//                    // Get grade value from user
                    System.out.println();
                    System.out.print("Enter grade (0-100): ");
                    double gradeValue = scanner.nextDouble();
                    scanner.nextLine();  // Consume newline




//                    (subjectType == 2) {
//                        System.out.println("Elective Subjects:");
//                        System.out.println("1. Music");
//                        System.out.println("2. Art");
//                        System.out.println("3. Physical Education");
//
//                        int electivePick = Integer.parseInt(scanner.nextLine());
//
//                        if (electivePick == 1) subject = new ElectiveSubject("Music", "E201");
//                        else if (electivePick == 2) subject = new ElectiveSubject("Art", "E202");
//                        else if (electivePick == 3) subject = new ElectiveSubject("Physical Education", "E203");
//                        else {
//                            System.out.println("Invalid elective subject.");
//                            break;
//                        }
//
//                    } else {
//                        System.out.println("Invalid subject type.");
//                        break;
//                    }

//                    System.out.print("Enter grade (0–100): ");
//                    double gradeValue = Double.parseDouble(scanner.nextLine());
//
//                    if (gradeValue < 0 || gradeValue > 100) {
//                        System.out.println("Invalid grade. Must be between 0 and 100.");
//                        break;
//                    }
//
//                    Grade newGrade = new Grade(studentId, subject, gradeValue);
//                    gradeManager.addGrade(newGrade);
//
//                    System.out.println("Grade recorded successfully!");
//                    break;


                    // Display grade confirmation before recording
                    System.out.println();
                    System.out.println("GRADE CONFIRMATION");
                    System.out.println("───────────────────────────────────────────────────");
                    System.out.println();
                    System.out.println("Grade ID: " + String.format("GRD%03d", gradeManager.getGradeCount() + 1));
                    System.out.println("Student: " + studentId + " - " + student1.getStudentName());
                    System.out.println("Subject: " + selectedSubject.getSubjectName() + " (" + selectedSubject.getSubjectType() + ")");
                    System.out.println("Grade: " + gradeValue + "%");
                    // get current date
                    String currentDate = new java.text.SimpleDateFormat("MM-dd-yyyy").format(new java.util.Date());
                    System.out.println("Date: " + currentDate);
                    System.out.println("───────────────────────────────────────────────────");
                    System.out.println();


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


