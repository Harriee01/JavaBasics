import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// Tests GradeManager functionality (85%+ coverage)

class GradeManagerTest {
    private GradeManager gradeManager;
    private CoreSubject math;
    private CoreSubject english;
    private ElectiveSubject music;

    @BeforeEach
    void setUp() {
        gradeManager = new GradeManager();
        math = new CoreSubject("Mathematics", "MATH101");
        english = new CoreSubject("English", "ENG101");
        music = new ElectiveSubject("Music", "MUS101");
    }

    @Test
    @DisplayName("Test add grade")
    void testAddGrade() {
        Grade grade = new Grade("STU001", math, 85);
        gradeManager.addGrade(grade);

        assertEquals(1, gradeManager.getGradeCount(),
                "Grade count should be 1 after adding one grade");
    }

    @Test
    @DisplayName("Test add multiple grades")
    void testAddMultipleGrades() {
        gradeManager.addGrade(new Grade("STU001", math, 85));
        gradeManager.addGrade(new Grade("STU001", english, 90));
        gradeManager.addGrade(new Grade("STU002", math, 75));

        assertEquals(3, gradeManager.getGradeCount(),
                "Grade count should be 3");
    }

    @Test
    @DisplayName("Test view grades by student")
    void testViewGradesByStudent() {
        // Add grades for multiple students
        gradeManager.addGrade(new Grade("STU001", math, 85));
        gradeManager.addGrade(new Grade("STU001", english, 90));
        gradeManager.addGrade(new Grade("STU002", math, 75));

        // Get grades for STU001
        Grade[] student1Grades = gradeManager.viewGradesByStudent("STU001");
        assertEquals(2, student1Grades.length,
                "STU001 should have 2 grades");

        // Get grades for STU002
        Grade[] student2Grades = gradeManager.viewGradesByStudent("STU002");
        assertEquals(1, student2Grades.length,
                "STU002 should have 1 grade");
    }

    @Test
    @DisplayName("Test view grades by student - no grades")
    void testViewGradesByStudent_NoGrades() {
        Grade[] grades = gradeManager.viewGradesByStudent("STU999");
        assertEquals(0, grades.length,
                "Non-existent student should have 0 grades");
    }

    @Test
    @DisplayName("Test calculate core average")
    void testCalculateCoreAverage() {
        // Add core subject grades
        gradeManager.addGrade(new Grade("STU001", math, 80));
        gradeManager.addGrade(new Grade("STU001", english, 90));
        // Add elective grade (should not count)
        gradeManager.addGrade(new Grade("STU001", music, 100));

        double coreAvg = gradeManager.calculateCoreAverage("STU001");
        assertEquals(85.0, coreAvg, 0.01,
                "Core average should be 85% (80 + 90 / 2)");
    }

    @Test
    @DisplayName("Test calculate elective average")
    void testCalculateElectiveAverage() {
        // Add grades
        gradeManager.addGrade(new Grade("STU001", math, 80));  // Core
        gradeManager.addGrade(new Grade("STU001", music, 95));  // Elective

        double electiveAvg = gradeManager.calculateElectiveAverage("STU001");
        assertEquals(95.0, electiveAvg, 0.01,
                "Elective average should be 95%");
    }

    @Test
    @DisplayName("Test calculate overall average")
    void testCalculateOverallAverage() {
        gradeManager.addGrade(new Grade("STU001", math, 80));
        gradeManager.addGrade(new Grade("STU001", english, 90));
        gradeManager.addGrade(new Grade("STU001", music, 100));

        double overallAvg = gradeManager.calculateOverallAverage("STU001");
        assertEquals(90.0, overallAvg, 0.01,
                "Overall average should be 90% (80 + 90 + 100 / 3)");
    }

    @Test
    @DisplayName("Test calculate average with no grades")
    void testCalculateAverage_NoGrades() {
        // Student with no grades should return 0
        assertEquals(0.0, gradeManager.calculateOverallAverage("STU999"), 0.01);
        assertEquals(0.0, gradeManager.calculateCoreAverage("STU999"), 0.01);
        assertEquals(0.0, gradeManager.calculateElectiveAverage("STU999"), 0.01);
    }

    @Test
    @DisplayName("Test get grade count for student")
    void testGetGradeCountForStudent() {
        gradeManager.addGrade(new Grade("STU001", math, 85));
        gradeManager.addGrade(new Grade("STU001", english, 90));
        gradeManager.addGrade(new Grade("STU002", math, 75));

        assertEquals(2, gradeManager.getGradeCountForStudent("STU001"));
        assertEquals(1, gradeManager.getGradeCountForStudent("STU002"));
        assertEquals(0, gradeManager.getGradeCountForStudent("STU999"));
    }
}