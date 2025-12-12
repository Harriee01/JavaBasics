public class StatisticsCalculator {//this class calculates statistical measures for grade analysis
    // it also follows the SOLID principles (Single Responsibility Principle)

    // this calculates the mean (average) of an array of numbers
    public static double calculateMean(double[] values) {
        if (values.length == 0) return 0.0;
        double sum = 0;
        //loops through all values and sum them
        for (double value : values) {
            sum += value;
        }
        //returns average (sum divided by count)
        return sum / values.length;
    }

    //this calculates the median (middle value) of an array
    public static double calculateMedian(double[] values) {
        if (values.length == 0) return 0.0;

        // the array is first sorted  (it is copied to avoid changing the original array)
        double[] sorted = values.clone();
        java.util.Arrays.sort(sorted);

        // if odd ,it  returns the middle one
        if (sorted.length % 2 == 1) {
            return sorted[sorted.length / 2];
        } else {
            // if even,it returns the average of the two middle values
            int mid1 = sorted.length / 2 - 1;
            int mid2 = sorted.length / 2;
            return (sorted[mid1] + sorted[mid2]) / 2.0;
        }
    }

    // calculates the mode (most frequent value)
    public static double calculateMode(double[] values) {
        if (values.length == 0) return 0.0;

        // uses simple frequency counting approach
        double mode = values[0];
        int maxCount = 0;

        // each value is counted as many times as it appears
        for (int i = 0; i < values.length; i++) {
            int count = 0;
            for (int j = 0; j < values.length; j++) {
                // rounds up to 1 decimal place for comparison
                if (Math.round(values[i] * 10) == Math.round(values[j] * 10)) {
                    count++;
                }
            }
            // if this value appears more than current mode, update mode
            if (count > maxCount) {
                maxCount = count;
                mode = values[i];
            }
        }
        return mode;
    }

    // calculates standard deviation (measure of spread)
    public static double calculateStandardDeviation(double[] values) {
        if (values.length == 0) return 0.0;

        // calculates mean
        double mean = calculateMean(values);

        // calculates the sum of squared differences from mean
        double sumSquaredDiff = 0;
        for (double value : values) {
            double diff = value - mean;  // Difference from mean
            sumSquaredDiff += diff * diff;  // Square it and add to sum
        }

        // calculates variance (average of squared differences)
        double variance = sumSquaredDiff / values.length;

        // standard deviation is square root of variance
        return Math.sqrt(variance);
    }

    // finds minimum value in array
    public static double findMin(double[] values) {
        if (values.length == 0) return 0.0;
        double min = values[0];
        for (double value : values) {
            if (value < min) min = value;
        }
        return min;
    }

    // finds maximum value in array
    public static double findMax(double[] values) {
        if (values.length == 0) return 0.0;
        double max = values[0];
        for (double value : values) {
            if (value > max) max = value;
        }
        return max;
    }




}
