public class CSVParser {//this class handles parsing CSV files for bulk import; also follows SOLID principles (Single Responsibility Principle)

    // parses a CSV file and returns an array of grade data
    // each row is arranged in this manner : StudentID,SubjectName,SubjectType,Grade
    public static String[][] parseGradeCSV(String filename) throws FileOperationException, InvalidCSVFormatException {
        try {// exception handling with try and catch
            // creates file path
            String filepath = "./imports/" + filename + ".csv";
            java.io.File file = new java.io.File(filepath);

            // checks if  the file exists
            if (!file.exists()) {
                throw new FileOperationException("File not found: " + filepath);
            }

            // reads the file
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));

            // counts the  lines first to create array
            int lineCount = 0;
            while (reader.readLine() != null) {
                lineCount++;
            }
            reader.close();

            // creates array to store parsed data
            String[][] data = new String[lineCount][4];  // 4 columns: ID, Subject, Type, Grade

            // reads file again and parse
            reader = new java.io.BufferedReader(new java.io.FileReader(file));
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null) {
                // splits by comma
                String[] parts = line.split(",");

                // validates format (must have 4 parts)
                if (parts.length != 4) {
                    reader.close();
                    throw new InvalidCSVFormatException(row + 1, "Expected 4 columns, found " + parts.length);
                }

                // stores data (trims whitespace)
                data[row][0] = parts[0].trim();  // Student ID
                data[row][1] = parts[1].trim();  // Subject Name
                data[row][2] = parts[2].trim();  // Subject Type
                data[row][3] = parts[3].trim();  // Grade

                row++;
            }

            reader.close();
            return data;

        } catch (java.io.IOException e) {
            throw new FileOperationException("Error reading file: " + e.getMessage());
        }
    }


}
