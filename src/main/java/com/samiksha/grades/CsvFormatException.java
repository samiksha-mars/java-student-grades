package com.samiksha.grades;

/** Raised when the input CSV is malformed; carries the offending line number. */
public class CsvFormatException extends RuntimeException {

    private final int lineNumber;

    public CsvFormatException(int lineNumber, String message) {
        super("Line " + lineNumber + ": " + message);
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
