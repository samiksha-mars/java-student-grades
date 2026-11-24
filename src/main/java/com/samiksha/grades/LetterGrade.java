package com.samiksha.grades;

/** Standard 90/80/70/60 letter-grade scale. */
public enum LetterGrade {
    A(90),
    B(80),
    C(70),
    D(60),
    F(0);

    private final double minimum;

    LetterGrade(double minimum) {
        this.minimum = minimum;
    }

    public double minimum() {
        return minimum;
    }

    public static LetterGrade forScore(double score) {
        if (Double.isNaN(score) || score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100: " + score);
        }
        for (LetterGrade grade : values()) {
            if (score >= grade.minimum) {
                return grade;
            }
        }
        return F;
    }
}
