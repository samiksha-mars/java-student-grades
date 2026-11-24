package com.samiksha.grades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class LetterGradeTest {

    @ParameterizedTest
    @CsvSource({
        "100, A", "90, A", "89.99, B", "80, B", "79.5, C",
        "70, C", "69.9, D", "60, D", "59.99, F", "0, F"
    })
    void mapsScoresToLetters(double score, LetterGrade expected) {
        assertEquals(expected, LetterGrade.forScore(score));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, 100.1, Double.NaN})
    void rejectsOutOfRangeScores(double score) {
        assertThrows(IllegalArgumentException.class, () -> LetterGrade.forScore(score));
    }
}
