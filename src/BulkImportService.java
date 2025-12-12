public class BulkImportService {//this class handles importing grades from CSV files
    // imports grades from CSV file
    // returns array: [totalRows, successCount, failCount]
    public static int[] importGradesFromCSV(String filename, StudentManager studentManager,
                                            GradeManager gradeManager, CoreSubject[] coreSubjects,
                                            ElectiveSubject[] electiveSubjects) {
        int totalRows = 0;
        int successCount = 0;
        int failCount = 0;

        // stringBuilder to collect error messages
        StringBuilder errorLog = new StringBuilder();
        errorLog.append("IMPORT LOG - " + new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new java.util.Date()) + "\n");
        errorLog.append("═══════════════════════════════════════════════════\n\n");

        try {// exception handling with the try and catch block
            System.out.println("Validating file... ✓");
            // parses the CSV file
            String[][] data = CSVParser.parseGradeCSV(filename);
            totalRows = data.length;

            System.out.println("Processing grades...");

            // processes each row
            for (int i = 0; i < data.length; i++) {
                int rowNumber = i + 1;
                String studentId = data[i][0];
                String subjectName = data[i][1];
                String subjectType = data[i][2];
                String gradeString = data[i][3];

                try {
                    // validates if student exists
                    Student student = studentManager.findStudent(studentId);
                    if (student == null) {
                        throw new StudentNotFoundException(studentId);
                    }

                    // parses and validates grade
                    double grade = Double.parseDouble(gradeString);
                    if (!student.validateGrade(grade)) {
                        throw new InvalidGradeException(grade);
                    }

                    // finds matching subject
                    Subject subject = null;
                    if (subjectType.equalsIgnoreCase("Core")) {
                        for (CoreSubject coreSubject : coreSubjects) {
                            if (coreSubject.getSubjectName().equalsIgnoreCase(subjectName)) {
                                subject = coreSubject;
                                break;
                            }
                        }
                    } else if (subjectType.equalsIgnoreCase("Elective")) {
                        for (ElectiveSubject electiveSubject : electiveSubjects) {
                            if (electiveSubject.getSubjectName().equalsIgnoreCase(subjectName)) {
                                subject = electiveSubject;
                                break;
                            }
                        }
                    }

                    if (subject == null) {
                        throw new Exception("Subject '" + subjectName + "' not found");
                    }

                    // Create and add grade
                    Grade newGrade = new Grade(studentId, subject, grade);
                    gradeManager.addGrade(newGrade);
                    successCount++;

                } catch (StudentNotFoundException e) {
                    failCount++;
                    errorLog.append("Row " + rowNumber + ": Invalid student ID (" + studentId + ")\n");
                } catch (InvalidGradeException e) {
                    failCount++;
                    errorLog.append("Row " + rowNumber + ": Grade out of range (" + gradeString + ")\n");
                } catch (NumberFormatException e) {
                    failCount++;
                    errorLog.append("Row " + rowNumber + ": Invalid grade format (" + gradeString + ")\n");
                } catch (Exception e) {
                    failCount++;
                    errorLog.append("Row " + rowNumber + ": " + e.getMessage() + "\n");
                }
            }

            // Write error log to file if there were failures
            if (failCount > 0) {
                try {
                    String logFilename = "import_log_" +
                            new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + ".txt";
                    java.io.FileWriter logWriter = new java.io.FileWriter("./imports/" + logFilename);

                    errorLog.append("\nSUMMARY\n");
                    errorLog.append("Total Rows: " + totalRows + "\n");
                    errorLog.append("Successfully Imported: " + successCount + "\n");
                    errorLog.append("Failed: " + failCount + "\n");

                    logWriter.write(errorLog.toString());
                    logWriter.close();

                    System.out.println("  See " + logFilename + " for details");
                } catch (java.io.IOException e) {
                    // Could not write log file
                }
            }

        } catch (FileOperationException | InvalidCSVFormatException e) {
            System.out.println("✗ ERROR: " + e.getMessage());
            return new int[]{0, 0, 0};
        }

        return new int[]{totalRows, successCount, failCount};
    }
}
