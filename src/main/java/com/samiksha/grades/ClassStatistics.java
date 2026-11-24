package com.samiksha.grades;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Summary statistics over the per-student averages of a class. */
public record ClassStatistics(
        int count,
        double mean,
        double median,
        Student highest,
        Student lowest,
        Map<LetterGrade, Long> distribution) {

    public static ClassStatistics of(List<Student> students) {
        if (students == null || students.isEmpty()) {
            throw new IllegalArgumentException("Cannot compute statistics for an empty class");
        }
        double[] averages = students.stream().mapToDouble(Student::average).sorted().toArray();
        double mean = Arrays.stream(averages).average().orElseThrow();
        Comparator<Student> byAverage = Comparator.comparingDouble(Student::average);
        Student highest = students.stream().max(byAverage).orElseThrow();
        Student lowest = students.stream().min(byAverage).orElseThrow();

        Map<LetterGrade, Long> distribution = new EnumMap<>(LetterGrade.class);
        for (LetterGrade grade : LetterGrade.values()) {
            distribution.put(grade, 0L);
        }
        students.forEach(s -> distribution.merge(s.letterGrade(), 1L, Long::sum));

        return new ClassStatistics(students.size(), mean, median(averages), highest, lowest, distribution);
    }

    static double median(double[] sorted) {
        int n = sorted.length;
        return n % 2 == 1 ? sorted[n / 2] : (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0;
    }
}
