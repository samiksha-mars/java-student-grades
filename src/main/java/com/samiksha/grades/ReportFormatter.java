package com.samiksha.grades;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/** Renders students and class statistics as a fixed-width text report. */
public final class ReportFormatter {

    private ReportFormatter() {
    }

    public static String format(List<Student> students) {
        ClassStatistics stats = ClassStatistics.of(students);
        List<String> subjects = new ArrayList<>(students.get(0).scores().keySet());
        int nameWidth = Math.max(7, students.stream().mapToInt(s -> s.name().length()).max().orElse(7));

        StringBuilder sb = new StringBuilder();
        StringBuilder header = new StringBuilder(String.format("%-" + nameWidth + "s", "Student"));
        for (String subject : subjects) {
            header.append(String.format(" %9s", abbreviate(subject)));
        }
        header.append(String.format(" %8s %5s", "Average", "Grade"));
        String rule = "-".repeat(header.length());

        sb.append("STUDENT GRADE REPORT").append('\n');
        sb.append(rule).append('\n').append(header).append('\n').append(rule).append('\n');

        students.stream()
                .sorted(Comparator.comparingDouble(Student::average).reversed())
                .forEach(student -> {
                    sb.append(String.format("%-" + nameWidth + "s", student.name()));
                    for (String subject : subjects) {
                        Double score = student.scores().get(subject);
                        sb.append(score == null ? String.format(" %9s", "-") : String.format(" %9.1f", score));
                    }
                    sb.append(String.format(" %8.2f %5s", student.average(), student.letterGrade()));
                    sb.append('\n');
                });

        sb.append(rule).append('\n');
        sb.append("CLASS STATISTICS").append('\n');
        sb.append(String.format("  Students : %d%n", stats.count()));
        sb.append(String.format("  Mean     : %.2f%n", stats.mean()));
        sb.append(String.format("  Median   : %.2f%n", stats.median()));
        sb.append(String.format("  Highest  : %.2f (%s)%n", stats.highest().average(), stats.highest().name()));
        sb.append(String.format("  Lowest   : %.2f (%s)%n", stats.lowest().average(), stats.lowest().name()));
        sb.append("  Grades   : ");
        List<String> parts = new ArrayList<>();
        for (Map.Entry<LetterGrade, Long> entry : stats.distribution().entrySet()) {
            parts.add(entry.getKey() + "=" + entry.getValue());
        }
        sb.append(String.join("  ", parts)).append('\n');
        return sb.toString();
    }

    private static String abbreviate(String subject) {
        return subject.length() <= 9 ? subject : subject.substring(0, 9);
    }
}
