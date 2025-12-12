public class GPACalculator {// this class follows the SOLID principles(Single Responsibility Principle)
    // it has only one job and that is to convert percentages to GPA

    public static double percentageToGPA(double percentage){// this static method can be called without creating an object
        //handling edge cases
        if (percentage >= 93) return 4.0;   // A  (93-100%)
        else if (percentage >= 90) return 3.7;  // A- (90-92%)
        else if (percentage >= 87) return 3.3;  // B+ (87-89%)
        else if (percentage >= 83) return 3.0;  // B  (83-86%)
        else if (percentage >= 80) return 2.7;  // B- (80-82%)
        else if (percentage >= 77) return 2.3;  // C+ (77-79%)
        else if (percentage >= 73) return 2.0;  // C  (73-76%)
        else if (percentage >= 70) return 1.7;  // C- (70-72%)
        else if (percentage >= 67) return 1.3;  // D+ (67-69%)
        else if (percentage >= 60) return 1.0;  // D  (60-66%)
        else return 0.0;                        // F  (below 60%)

    }

    // this converts GPA back to letter grade for display
    public static String gpaToLetterGrade(double gpa) {
        if (gpa >= 3.7) return "A";
        else if (gpa >= 3.3) return "A-";
        else if (gpa >= 3.0) return "B+";
        else if (gpa >= 2.7) return "B";
        else if (gpa >= 2.3) return "B-";
        else if (gpa >= 2.0) return "C+";
        else if (gpa >= 1.7) return "C";
        else if (gpa >= 1.3) return "C-";
        else if (gpa >= 1.0) return "D+";
        else if (gpa > 0.0) return "D";
        else return "F";
    }
}
