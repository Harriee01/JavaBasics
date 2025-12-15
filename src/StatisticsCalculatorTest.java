import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// Tests statistical calculations (critical business logic - 95%+ coverage)

class StatisticsCalculatorTest {

    @Test
    @DisplayName("Test mean calculation with valid data")
    void testCalculateMean_ValidData() {
        // Test simple array
        double[] grades = {80, 85, 90, 95, 100};
        assertEquals(90.0, StatisticsCalculator.calculateMean(grades), 0.01,
                "Mean of 80,85,90,95,100 should be 90");

        // Test with decimals
        double[] grades2 = {85.5, 92.3, 78.9};
        assertEquals(85.57, StatisticsCalculator.calculateMean(grades2), 0.01,
                "Mean should handle decimal values");
    }

    @Test
    @DisplayName("Test mean calculation with empty array")
    void testCalculateMean_EmptyArray() {
        // Edge case: empty array should return 0
        double[] emptyGrades = {};
        assertEquals(0.0, StatisticsCalculator.calculateMean(emptyGrades), 0.01,
                "Mean of empty array should be 0");
    }

    @Test
    @DisplayName("Test mean calculation with single value")
    void testCalculateMean_SingleValue() {
        // Edge case: single value should return that value
        double[] singleGrade = {85.5};
        assertEquals(85.5, StatisticsCalculator.calculateMean(singleGrade), 0.01,
                "Mean of single value should be that value");
    }

    @Test
    @DisplayName("Test median calculation with odd number of values")
    void testCalculateMedian_OddCount() {
        // Odd number: should return middle value
        double[] grades = {70, 80, 85, 90, 100};
        assertEquals(85.0, StatisticsCalculator.calculateMedian(grades), 0.01,
                "Median of 70,80,85,90,100 should be 85");
    }

    @Test
    @DisplayName("Test median calculation with even number of values")
    void testCalculateMedian_EvenCount() {
        // Even number: should return average of two middle values
        double[] grades = {70, 80, 90, 100};
        assertEquals(85.0, StatisticsCalculator.calculateMedian(grades), 0.01,
                "Median of 70,80,90,100 should be 85 (average of 80 and 90)");
    }

    @Test
    @DisplayName("Test median with unsorted array")
    void testCalculateMedian_UnsortedArray() {
        // Should work with unsorted data
        double[] grades = {100, 70, 90, 80};
        assertEquals(85.0, StatisticsCalculator.calculateMedian(grades), 0.01,
                "Median should sort array first");
    }

    @Test
    @DisplayName("Test mode calculation")
    void testCalculateMode() {
        // Array with clear mode
        double[] grades = {85, 85, 85, 90, 95};
        assertEquals(85.0, StatisticsCalculator.calculateMode(grades), 0.1,
                "Mode should be 85 (appears 3 times)");
    }

    @Test
    @DisplayName("Test standard deviation calculation")
    void testCalculateStandardDeviation() {
        // Test with known standard deviation
        double[] grades = {80, 85, 90, 95, 100};
        // Standard deviation ≈ 7.07
        assertTrue(StatisticsCalculator.calculateStandardDeviation(grades) > 7.0,
                "Standard deviation should be approximately 7.07");
        assertTrue(StatisticsCalculator.calculateStandardDeviation(grades) < 7.2,
                "Standard deviation should be approximately 7.07");
    }

    @Test
    @DisplayName("Test standard deviation with identical values")
    void testCalculateStandardDeviation_IdenticalValues() {
        // All same values should have standard deviation of 0
        double[] grades = {85, 85, 85, 85};
        assertEquals(0.0, StatisticsCalculator.calculateStandardDeviation(grades), 0.01,
                "Standard deviation of identical values should be 0");
    }

    @Test
    @DisplayName("Test find minimum value")
    void testFindMin() {
        double[] grades = {95, 70, 85, 90, 75};
        assertEquals(70.0, StatisticsCalculator.findMin(grades), 0.01,
                "Minimum should be 70");
    }

    @Test
    @DisplayName("Test find maximum value")
    void testFindMax() {
        double[] grades = {95, 70, 85, 90, 75};
        assertEquals(95.0, StatisticsCalculator.findMax(grades), 0.01,
                "Maximum should be 95");
    }

    @Test
    @DisplayName("Test min/max with single value")
    void testFindMinMax_SingleValue() {
        double[] grades = {85.5};
        assertEquals(85.5, StatisticsCalculator.findMin(grades), 0.01);
        assertEquals(85.5, StatisticsCalculator.findMax(grades), 0.01);
    }
}