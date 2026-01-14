import java.io.*;
import java.nio.file.*;
import java.util.List;

/**
 * Simple file operations
 * Demonstrates basic NIO.2 usage.
 */
public class FileOperations {

    /**
     * Exports student data to CSV file.
     */
    public static void exportStudentsToCSV(List<Student> students, String filename) throws IOException {
        Path filePath = Paths.get(filename);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            // Write header
            writer.write("StudentID,Name,Email,Phone,Age,Type,Status");
            writer.newLine();

            // Write data
            for (Student student : students) {
                String line = String.format("%s,%s,%s,%s,%d,%s,%s",
                        student.getStudentId(),
                        student.getStudentName(),
                        student.getStudentEmail(),
                        student.getStudentPhone(),
                        student.getStudentAge(),
                        student.getStudentType(),
                        student.getStudentStatus());
                writer.write(line);
                writer.newLine();
            }
        }

        System.out.println("✓ Students exported to: " + filename);
    }

    /**
     * Exports grade data to CSV file.
     */
    public static void exportGradesToCSV(List<Grade> grades, String filename) throws IOException {
        Path filePath = Paths.get(filename);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            // Write header
            writer.write("GradeID,StudentID,SubjectCode,SubjectName,Grade,LetterGrade,Date");
            writer.newLine();

            // Write data
            for (Grade grade : grades) {
                String line = String.format("%s,%s,%s,%s,%.2f,%s,%s",
                        grade.getGradeId(),
                        grade.getStudentId(),
                        grade.getSubject().getSubjectCode(),
                        grade.getSubject().getSubjectName(),
                        grade.getGradeValue(),
                        grade.getLetterGrade(),
                        grade.getDate());
                writer.write(line);
                writer.newLine();
            }
        }

        System.out.println("✓ Grades exported to: " + filename);
    }

    /**
     * Imports data from CSV file (basic implementation).
     */
    public static List<String> importFromCSV(String filename) throws IOException {
        Path filePath = Paths.get(filename);
        List<String> lines = Files.readAllLines(filePath);
        System.out.println("✓ Imported " + lines.size() + " lines from: " + filename);
        return lines;
    }
}