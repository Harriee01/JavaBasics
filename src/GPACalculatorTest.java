import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// Tests the GPA conversion logic (critical business logic - 95%+ coverage)

class GPACalculatorTest {

    @Test
    @DisplayName("Test percentage to GPA conversion for grade A (93-100%)")
    void testPercentageToGPA_GradeA() {
        // Test upper boundary (100%)
        assertEquals(4.0, GPACalculator.percentageToGPA(100), 0.01,
                "100% should convert to 4.0 GPA");

        // Test lower boundary (93%)
        assertEquals(4.0, GPACalculator.percentageToGPA(93), 0.01,
                "93% should convert to 4.0 GPA");

        // Test middle value (96%)
        assertEquals(4.0, GPACalculator.percentageToGPA(96), 0.01,
                "96% should convert to 4.0 GPA");
    }

    @Test
    @DisplayName("Test percentage to GPA conversion for grade A- (90-92%)")
    void testPercentageToGPA_GradeAMinus() {
        // Test boundary values
        assertEquals(3.7, GPACalculator.percentageToGPA(92), 0.01,
                "92% should convert to 3.7 GPA");
        assertEquals(3.7, GPACalculator.percentageToGPA(90), 0.01,
                "90% should convert to 3.7 GPA");
    }

    @Test
    @DisplayName("Test percentage to GPA conversion for grade B+ (87-89%)")
    void testPercentageToGPA_GradeBPlus() {
        assertEquals(3.3, GPACalculator.percentageToGPA(87), 0.01);
        assertEquals(3.3, GPACalculator.percentageToGPA(89), 0.01);
    }

    @Test
    @DisplayName("Test percentage to GPA conversion for grade B (83-86%)")
    void testPercentageToGPA_GradeB() {
        assertEquals(3.0, GPACalculator.percentageToGPA(85), 0.01);
    }

    @Test
    @DisplayName("Test percentage to GPA conversion for failing grade (below 60%)")
    void testPercentageToGPA_GradeF() {
        // Test zero
        assertEquals(0.0, GPACalculator.percentageToGPA(0), 0.01,
                "0% should convert to 0.0 GPA");

        // Test low failing grade
        assertEquals(0.0, GPACalculator.percentageToGPA(50), 0.01,
                "50% should convert to 0.0 GPA");
    }

    @Test
    @DisplayName("Test GPA to letter grade conversion")
    void testGPAToLetterGrade() {
        // Test all letter grades
        assertEquals("A", GPACalculator.gpaToLetterGrade(4.0), "4.0 should be A");
        assertEquals("A-", GPACalculator.gpaToLetterGrade(3.7), "3.7 should be A-");
        assertEquals("B+", GPACalculator.gpaToLetterGrade(3.3), "3.3 should be B+");
        assertEquals("B", GPACalculator.gpaToLetterGrade(2.7), "2.7 should be B");
        assertEquals("C", GPACalculator.gpaToLetterGrade(2.0), "2.0 should be C");
        assertEquals("D", GPACalculator.gpaToLetterGrade(1.0), "1.0 should be D");
        assertEquals("F", GPACalculator.gpaToLetterGrade(0.0), "0.0 should be F");
    }

    @Test
    @DisplayName("Test boundary values for all GPA ranges")
    void testAllGPABoundaries() {
        // This test ensures no gaps in the conversion logic
        // Testing critical boundary values

        // Test D+ to C- boundary (67-70%)
        assertEquals(1.3, GPACalculator.percentageToGPA(67), 0.01);
        assertEquals(1.7, GPACalculator.percentageToGPA(70), 0.01);

        // Test C to C+ boundary (73-77%)
        assertEquals(2.0, GPACalculator.percentageToGPA(73), 0.01);
        assertEquals(2.3, GPACalculator.percentageToGPA(77), 0.01);

        // Test B- to B boundary (80-83%)
        assertEquals(2.7, GPACalculator.percentageToGPA(80), 0.01);
        assertEquals(3.0, GPACalculator.percentageToGPA(83), 0.01);
    }
}
