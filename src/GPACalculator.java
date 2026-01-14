public class GPACalculator {// this class follows the SOLID principles(Single Responsibility Principle)
    // it has only one job and that is to convert percentages to GPA

    public static double percentageToGPA(double percentageGrade){// this static method can be called without creating an object
        // Convert percentage to 4.0 scale
        if (percentageGrade >= 90) return 4.0;
        else if (percentageGrade >= 80) return 3.0;
        else if (percentageGrade >= 70) return 2.0;
        else if (percentageGrade >= 60) return 1.0;
        else return 0.0;

    }

    // this converts GPA back to letter grade for display
    public static String gpaToLetterGrade(double gpa) {
        if (gpa >= 3.5) return "A";
        else if (gpa >= 3.0) return "B+";
        else if (gpa >= 2.0) return "C+";
        else if (gpa >= 1.0) return "D+";
        else return "F";
    }
}
