# Java Student Grades

Reads a CSV of students and their subject scores, computes each student's average and
letter grade, and prints a formatted report with class statistics. Plain Java, no
external dependencies at runtime.

## Features

- Loads the bundled `students.csv` or any CSV you pass on the command line
- Any number of subject columns (taken from the CSV header)
- Per-student average and letter grade (A >= 90, B >= 80, C >= 70, D >= 60, otherwise F)
- Class statistics: mean, median, highest and lowest average, and grade distribution
- Report sorted from highest to lowest average
- Clear errors with line numbers for malformed CSV (bad numbers, wrong column count,
  scores outside 0-100, blank or duplicate subject columns)

## Tech Stack

- Java 21 (compiled with `--release 21`, runs on 21+)
- Maven (via the bundled wrapper)
- JUnit 5

## Usage

Compile, then run the main class from `target/classes` (the bundled CSV is copied there
during the build):

```bash
./mvnw -q compile
java -cp target/classes com.samiksha.grades.GradesApp
```

To grade your own file, pass its path:

```bash
java -cp target/classes com.samiksha.grades.GradesApp path/to/class.csv
```

### CSV format

The first non-comment line is the header: a name column followed by one column per subject.
Blank lines and lines starting with `#` are ignored.

```csv
name,math,science,english,history
Aarav Sharma,92,88,79,85
Priya Patel,78,85,91,88
```

The bundled sample lives at `src/main/resources/students.csv`.

### Example output

```text
STUDENT GRADE REPORT
---------------------------------------------------------------------
Student             math   science   english   history  Average Grade
---------------------------------------------------------------------
Ananya Iyer         98.0      95.0      93.0      97.0    95.75     A
Sneha Kulkarni      95.0      90.0      97.0      92.0    93.50     A
Isha Reddy          89.0      92.0      86.0      81.0    87.00     B
...
Vikram Singh        55.0      61.0      67.0      49.0    58.00     F
---------------------------------------------------------------------
CLASS STATISTICS
  Students : 10
  Mean     : 79.15
  Median   : 85.38
  Highest  : 95.75 (Ananya Iyer)
  Lowest   : 58.00 (Vikram Singh)
  Grades   : A=2  B=4  C=1  D=2  F=1
```

## Project Layout

```text
src/main/java/com/samiksha/grades/
  GradesApp.java          main class
  CsvStudentLoader.java   CSV parsing and validation
  Student.java            name + scores, average, letter grade
  LetterGrade.java        grade scale
  ClassStatistics.java    mean, median, highest, lowest, distribution
  ReportFormatter.java    text report
src/main/resources/students.csv   sample data
```

## Build & Test

```bash
./mvnw test          # compile and run the JUnit 5 suite
./mvnw -q package    # build target/java-student-grades-0.0.1-SNAPSHOT.jar
```
