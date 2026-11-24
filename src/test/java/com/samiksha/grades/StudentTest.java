package com.samiksha.grades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.Test;

class StudentTest {

    @Test
    void averageIsMeanOfScores() {
        Student s = new Student("Alice", Map.of("math", 90.0, "science", 80.0, "english", 70.0));
        assertEquals(80.0, s.average(), 1e-9);
        assertEquals(LetterGrade.B, s.letterGrade());
    }

    @Test
    void rejectsScoresOutsideRange() {
        assertThrows(IllegalArgumentException.class, () -> new Student("Bob", Map.of("math", 101.0)));
        assertThrows(IllegalArgumentException.class, () -> new Student("Bob", Map.of("math", -1.0)));
    }

    @Test
    void rejectsBlankNameAndEmptyScores() {
        assertThrows(IllegalArgumentException.class, () -> new Student(" ", Map.of("math", 50.0)));
        assertThrows(IllegalArgumentException.class, () -> new Student("Carl", Map.of()));
    }
}
