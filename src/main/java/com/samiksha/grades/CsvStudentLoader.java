package com.samiksha.grades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parses a CSV whose header is {@code name,<subject1>,<subject2>,...} and whose rows hold
 * one student each. Blank lines and lines starting with {@code #} are ignored.
 */
public final class CsvStudentLoader {

    public static final String BUNDLED_RESOURCE = "/students.csv";

    private CsvStudentLoader() {
    }

    public static List<Student> loadBundled() {
        InputStream stream = CsvStudentLoader.class.getResourceAsStream(BUNDLED_RESOURCE);
        if (stream == null) {
            throw new IllegalStateException("Bundled resource " + BUNDLED_RESOURCE + " not found on classpath");
        }
        return load(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    public static List<Student> load(Path path) {
        try {
            return load(Files.newBufferedReader(path, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + path, e);
        }
    }

    public static List<Student> load(Reader source) {
        try (BufferedReader reader = new BufferedReader(source)) {
            String[] header = null;
            List<Student> students = new ArrayList<>();
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String trimmed = line.strip();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                String[] cells = trimmed.split(",", -1);
                if (header == null) {
                    header = cells;
                    if (header.length < 2) {
                        throw new CsvFormatException(lineNumber, "header needs a name column and at least one subject");
                    }
                    validateSubjects(header, lineNumber);
                    continue;
                }
                students.add(parseRow(header, cells, lineNumber));
            }
            if (header == null) {
                throw new CsvFormatException(0, "file is empty");
            }
            return students;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void validateSubjects(String[] header, int lineNumber) {
        Set<String> seen = new HashSet<>();
        for (int i = 1; i < header.length; i++) {
            String subject = header[i].strip();
            if (subject.isEmpty()) {
                throw new CsvFormatException(lineNumber, "subject name in column " + (i + 1) + " is blank");
            }
            if (!seen.add(subject)) {
                throw new CsvFormatException(lineNumber, "duplicate subject column '" + subject + "'");
            }
        }
    }

    private static Student parseRow(String[] header, String[] cells, int lineNumber) {
        if (cells.length != header.length) {
            throw new CsvFormatException(lineNumber,
                    "expected " + header.length + " columns but found " + cells.length);
        }
        Map<String, Double> scores = new LinkedHashMap<>();
        for (int i = 1; i < header.length; i++) {
            String raw = cells[i].strip();
            try {
                scores.put(header[i].strip(), Double.parseDouble(raw));
            } catch (NumberFormatException e) {
                throw new CsvFormatException(lineNumber, "'" + raw + "' is not a number");
            }
        }
        try {
            return new Student(cells[0].strip(), scores);
        } catch (IllegalArgumentException e) {
            throw new CsvFormatException(lineNumber, e.getMessage());
        }
    }
}
