import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        StudentManager studentManager = new StudentManager();
        GradeManager gradeManager = new GradeManager();

        boolean running = true; //keeps the menu running until the user exits

        while (running) {
            System.out.println("\n=========================================");
            System.out.println("      STUDENT GRADE MANAGEMENT SYSTEM     ");
            System.out.println("=========================================");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Record Grade");
            System.out.println("4. View Grade Report");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) { // switch here is used to execute an action based on the user's input

                // -------------------------------------------------------
                // 1. ADD STUDENT
                // -------------------------------------------------------
                case "1":
                    System.out.println("\n--- Register New Student ---");

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

                    Student student;

                    if (type == 1) {
                        student = new RegularStudent(name, email, phone, age);
                    } else if (type == 2) {
                        student = new HonorsStudent(name, email, phone, age);
                    } else {
                        System.out.println("Invalid type selected.");
                        break;
                    }

                    studentManager.addStudent(student);
                    System.out.println("Student registered successfully!");
                    System.out.println("========================================");
                    System.out.println("Student ID     : " + student.getStudentId());
                    System.out.println("Name           : " + student.getStudentName());
                    System.out.println("Age            : " + student.getStudentAge());
                    System.out.println("Email          : " + student.getStudentEmail());
                    System.out.println("Phone          : " + student.getStudentPhone());
                    System.out.println("Type           : " + student.getStudentType());
                    System.out.println("Status         : " + student.getStudentStatus());

                    // Show Honors Eligibility **only for Honors students**
                    if (student instanceof HonorsStudent) {
                        HonorsStudent honorsStudent = (HonorsStudent) student;
                        boolean eligible = honorsStudent.checkHonorsEligibility();
                        System.out.println("Honors Eligible: " + (eligible ? "YES" : "NO"));
                    }

                    break;


                // 2. view students
                case "2":
                    studentManager.viewAllStudents();
                    break;


                // 3. record grade
                case "3":
                    System.out.print("Enter Student ID: ");
                    String studentId = scanner.nextLine();

                    Student student1 = studentManager.findStudent(studentId);

                    if (student1 == null) {
                        System.out.println("Student not found.");
                        break;
                    }

                    // Select subject type
                    System.out.println("Select subject type:");
                    System.out.println("1. Core");
                    System.out.println("2. Elective");
                    int subjectType = Integer.parseInt(scanner.nextLine());

                    Subject subject;

                    // Core subjects
                    if (subjectType == 1) {
                        System.out.println("Core Subjects:");
                        System.out.println("1. Mathematics");
                        System.out.println("2. Science");
                        System.out.println("3. English");

                        int corePick = Integer.parseInt(scanner.nextLine());

                        if (corePick == 1) subject = new CoreSubject("Mathematics", "C101");
                        else if (corePick == 2) subject = new CoreSubject("Science", "C102");
                        else if (corePick == 3) subject = new CoreSubject("English", "C103");
                        else {
                            System.out.println("Invalid core subject.");
                            break;
                        }

                    }
                    // Elective subjects
                    else if (subjectType == 2) {
                        System.out.println("Elective Subjects:");
                        System.out.println("1. Music");
                        System.out.println("2. Art");
                        System.out.println("3. Physical Education");

                        int electivePick = Integer.parseInt(scanner.nextLine());

                        if (electivePick == 1) subject = new ElectiveSubject("Music", "E201");
                        else if (electivePick == 2) subject = new ElectiveSubject("Art", "E202");
                        else if (electivePick == 3) subject = new ElectiveSubject("Physical Education", "E203");
                        else {
                            System.out.println("Invalid elective subject.");
                            break;
                        }

                    } else {
                        System.out.println("Invalid subject type.");
                        break;
                    }

                    System.out.print("Enter grade (0–100): ");
                    double gradeValue = Double.parseDouble(scanner.nextLine());

                    if (gradeValue < 0 || gradeValue > 100) {
                        System.out.println("Invalid grade. Must be between 0 and 100.");
                        break;
                    }

                    Grade newGrade = new Grade(studentId, subject, gradeValue);
                    gradeManager.addGrade(newGrade);

                    System.out.println("Grade recorded successfully!");
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


