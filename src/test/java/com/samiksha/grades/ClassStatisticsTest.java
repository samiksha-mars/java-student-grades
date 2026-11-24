package com.samiksha.grades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ClassStatisticsTest {

    private static Student student(String name, double score) {
        return new Student(name, Map.of("subject", score));
    }

    @Test
    void computesMeanMedianHighestLowestForOddCount() {
        List<Student> students = List.of(student("A", 95), student("B", 70), student("C", 82));
        ClassStatistics stats = ClassStatistics.of(students);

        assertEquals(3, stats.count());
        assertEquals(82.333333, stats.mean(), 1e-5);
        assertEquals(82.0, stats.median(), 1e-9);
        assertEquals("A", stats.highest().name());
        assertEquals("B", stats.lowest().name());
    }

    @Test
    void medianOfEvenCountAveragesMiddlePair() {
        List<Student> students = List.of(student("A", 60), student("B", 90), student("C", 70), student("D", 80));
        assertEquals(75.0, ClassStatistics.of(students).median(), 1e-9);
    }

    @Test
    void distributionCountsEveryGrade() {
        List<Student> students = List.of(student("A", 95), student("B", 91), student("C", 55));
        Map<LetterGrade, Long> distribution = ClassStatistics.of(students).distribution();

        assertEquals(2L, distribution.get(LetterGrade.A));
        assertEquals(0L, distribution.get(LetterGrade.B));
        assertEquals(1L, distribution.get(LetterGrade.F));
    }

    @Test
    void emptyClassIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> ClassStatistics.of(List.of()));
    }
}
