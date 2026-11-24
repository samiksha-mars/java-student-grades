package com.samiksha.grades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.Test;

class CsvStudentLoaderTest {

    @Test
    void parsesHeaderAndRows() {
        String csv = """
                name,math,science
                # comment lines are skipped
                Alice, 90, 80

                Bob,70,60
                """;
        List<Student> students = CsvStudentLoader.load(new StringReader(csv));

        assertEquals(2, students.size());
        assertEquals("Alice", students.get(0).name());
        assertEquals(List.of("math", "science"), List.copyOf(students.get(0).scores().keySet()));
        assertEquals(65.0, students.get(1).average(), 1e-9);
    }

    @Test
    void reportsLineNumberForBadNumber() {
        String csv = "name,math\nAlice,90\nBob,abc\n";
        CsvFormatException e = assertThrows(CsvFormatException.class,
                () -> CsvStudentLoader.load(new StringReader(csv)));
        assertEquals(3, e.getLineNumber());
        assertTrue(e.getMessage().contains("abc"));
    }

    @Test
    void rejectsWrongColumnCount() {
        String csv = "name,math,science\nAlice,90\n";
        assertThrows(CsvFormatException.class, () -> CsvStudentLoader.load(new StringReader(csv)));
    }

    @Test
    void rejectsOutOfRangeScore() {
        String csv = "name,math\nAlice,150\n";
        assertThrows(CsvFormatException.class, () -> CsvStudentLoader.load(new StringReader(csv)));
    }

    @Test
    void rejectsEmptyInput() {
        assertThrows(CsvFormatException.class, () -> CsvStudentLoader.load(new StringReader("\n\n")));
    }

    @Test
    void bundledCsvLoads() {
        List<Student> students = CsvStudentLoader.loadBundled();
        assertFalse(students.isEmpty());
        assertEquals(4, students.get(0).scores().size());
    }
}
