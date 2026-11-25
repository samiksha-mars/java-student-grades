package com.samiksha.grades;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** A student and their score (0-100) for each subject, in CSV column order. */
public record Student(String name, Map<String, Double> scores) {

    public Student {
        Objects.requireNonNull(name, "name");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Student name must not be blank");
        }
        if (scores == null || scores.isEmpty()) {
            throw new IllegalArgumentException("Student " + name + " has no scores");
        }
        scores.forEach((subject, score) -> {
            if (score == null || !(score >= 0 && score <= 100)) { // also rejects NaN
                throw new IllegalArgumentException(
                        "Score for " + name + " in " + subject + " must be between 0 and 100: " + score);
            }
        });
        scores = Collections.unmodifiableMap(new LinkedHashMap<>(scores));
    }

    public double average() {
        return scores.values().stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    public LetterGrade letterGrade() {
        return LetterGrade.forScore(average());
    }
}
