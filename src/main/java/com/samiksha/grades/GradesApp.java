package com.samiksha.grades;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Entry point. With no arguments it reads the bundled {@code students.csv};
 * otherwise it reads the CSV file given as the first argument.
 */
public final class GradesApp {

    private GradesApp() {
    }

    public static void main(String[] args) {
        try {
            List<Student> students = args.length > 0
                    ? CsvStudentLoader.load(Path.of(args[0]))
                    : CsvStudentLoader.loadBundled();
            if (students.isEmpty()) {
                System.err.println("No students found in input.");
                System.exit(1);
            }
            System.out.print(ReportFormatter.format(students));
        } catch (CsvFormatException | UncheckedIOException | IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
