package com.samiksha.grades;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ReportFormatterTest {

    @Test
    void reportListsStudentsBestFirstWithStatistics() {
        List<Student> students = List.of(
                new Student("Low", Map.of("math", 50.0)),
                new Student("High", Map.of("math", 95.0)));
        String report = ReportFormatter.format(students);

        assertTrue(report.indexOf("High") < report.indexOf("Low"));
        assertTrue(report.contains("CLASS STATISTICS"));
        assertTrue(report.contains("Median   : 72.50"));
        assertTrue(report.contains("Highest  : 95.00 (High)"));
        assertTrue(report.contains("A=1"));
        assertTrue(report.contains("F=1"));
    }
}
