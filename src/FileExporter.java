public class FileExporter {//this class handles all file export operations hence separating file handling from the business logic
    //It also follows the SOLID principles (Single Responsibility Principle)

    //exports student grade report to text file
    //returns true if successful else false
    public static boolean exportGradeReport(Student student, GradeManager gradeManager,
                                            String filename, boolean detailed) {
        try {
            // creates reports directory if it doesn't exist
            java.io.File reportsDir = new java.io.File("./reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();  //  directory is created
            }

            // creates the file path
            String filepath = "./reports/" + filename + ".txt";
            // creates te  FileWriter to write to file
            java.io.FileWriter writer = new java.io.FileWriter(filepath);

            // writes student information
            writer.write("STUDENT GRADE REPORT\n");
            writer.write("═══════════════════════════════════════════════════\n\n");
            writer.write(student.exportToText());
            writer.write("\n");

            // gets all grades for a particular student
            Grade[] grades = gradeManager.viewGradesByStudent(student.getStudentId());
            writer.write("Total Grades: " + grades.length + "\n\n");

            if (detailed && grades.length > 0) {
                // writes detailed grade information
                writer.write("GRADE HISTORY\n");
                writer.write("───────────────────────────────────────────────────\n");

                // loops through grades in reverse order (newest first)
                for (int i = grades.length - 1; i >= 0; i--) {
                    Grade g = grades[i];
                    writer.write(String.format("%-10s | %-12s | %-20s | %-10s | %.1f%%\n",
                            g.getGradeId(), g.getDate(), g.getSubject().getSubjectName(),
                            g.getSubject().getSubjectType(), g.getGrade()));
                }
                writer.write("───────────────────────────────────────────────────\n\n");
            }

            // writes averages and performance summary
            double coreAvg = gradeManager.calculateCoreAverage(student.getStudentId());
            double electiveAvg = gradeManager.calculateElectiveAverage(student.getStudentId());
            double overallAvg = gradeManager.calculateOverallAverage(student.getStudentId());

            writer.write("AVERAGES\n");
            writer.write("Core Subjects: " + String.format("%.1f%%\n", coreAvg));
            writer.write("Elective Subjects: " + String.format("%.1f%%\n", electiveAvg));
            writer.write("Overall Average: " + String.format("%.1f%%\n\n", overallAvg));

            // writes performance summary
            writer.write("PERFORMANCE SUMMARY\n");
            if (student.isPassing()) {
                writer.write("✓ Meeting passing grade requirement (" + student.getPassingGrade() + "%)\n");
            } else if (overallAvg > 0) {
                writer.write("✗ Below passing grade requirement (" + student.getPassingGrade() + "%)\n");
            }

            // closes the file writer
            writer.close();

            // gets the file size for display
            java.io.File file = new java.io.File(filepath);
            double fileSizeKB = file.length() / 1024.0;

            // displays success message
            System.out.println("✓ Report exported successfully!");
            System.out.println("  File: " + filename + ".txt");
            System.out.println("  Location: ./reports/");
            System.out.println("  Size: " + String.format("%.1f", fileSizeKB) + " KB");
            System.out.println("  Contains: " + grades.length + " grades, averages, performance summary");

            return true;
        } catch (java.io.IOException e) {
            // handles file writing errors
            System.out.println("✗ ERROR: Failed to export report");
            System.out.println("  " + e.getMessage());
            return false;
        }
    }


}
