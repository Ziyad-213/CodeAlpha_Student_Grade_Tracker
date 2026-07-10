# CodeAlpha Student Grade Tracker (Enhanced Edition)

A console-based Java application for managing student records and grades.
Built as part of the **CodeAlpha Internship Program**, this project demonstrates
core Object-Oriented Programming (OOP) principles, input validation, statistical
analysis, and file-based data persistence.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Class Descriptions](#class-descriptions)
- [Grading Scale](#grading-scale)
- [How to Compile and Run](#how-to-compile-and-run)
- [How to Use](#how-to-use)
- [CSV File Format](#csv-file-format)
- [Sample Menu](#sample-menu)
- [Possible Future Enhancements](#possible-future-enhancements)
- [Author](#author)
- [Acknowledgment](#acknowledgment)

---

## Overview

The Student Grade Tracker lets you add students, record their marks across
multiple subjects, and instantly view statistics such as class average,
median, standard deviation, and grade distribution. It also supports
searching, editing, deleting, sorting, and saving/loading records so data
isn't lost between runs.

The project started as a simple single-subject grade tracker and was
enhanced into a more complete, menu-driven grade management system.

---

## Features

- **Add Students** — record a student's name and marks across as many
  subjects as needed in one go.
- **View All Students** — clean, tabular view of every student with their
  average, letter grade, and pass/fail status.
- **Calculate Average Marks** — instantly see the class-wide average.
- **Show Highest / Lowest Marks** — quickly identify top and bottom performers.
- **Summary Report** — a full statistical report including:
  - Total students
  - Class average
  - Median
  - Standard deviation
  - Highest and lowest records
  - Pass/fail counts
  - Grade distribution (A/B/C/D/F breakdown)
- **Edit Student** — rename a student, update/add a subject mark, or remove
  a subject.
- **Delete Student** — remove a student record by ID.
- **Search Students** — find students by full or partial name match
  (case-insensitive).
- **Sort Students** — sort by name, average marks, or ID, in ascending or
  descending order.
- **Save / Load Records** — persist all student data to a CSV file and
  reload it in a future session, with IDs preserved.
- **Robust Input Validation** — every prompt validates its input (empty
  names, out-of-range marks, non-numeric entries, etc.) and re-prompts
  until valid data is entered.

---

## Project Structure

```
CodeAlpha_Student_Grade_Tracker/
└── src/
    ├── Student.java        # Represents a single student and their marks
    ├── GradeManager.java   # Manages the collection of students, statistics, and file I/O
    └── Main.java           # Console UI / menu-driven entry point
```

---

## Class Descriptions

### `Student.java`
Represents an individual student.

- Stores a unique, auto-generated **student ID**, a **name**, and a map of
  **subject → marks**.
- Computes derived values on demand: total marks, average marks, letter
  grade, and pass/fail status.
- Validates input at the source — a student cannot be created with a blank
  name, and marks must fall between `0.0` and `100.0`.

### `GradeManager.java`
Manages the full collection of `Student` objects.

- CRUD operations: add, find (by ID or name), search (partial name match),
  and remove students.
- Statistics: average, highest, lowest, median, standard deviation, grade
  distribution, and pass/fail counts.
- Sorting by name, average, or ID.
- CSV persistence: `saveToFile()` and `loadFromFile()`.
- Formatted console output for both a full student table and a single
  student's detailed breakdown.

### `Main.java`
The console entry point and user interface.

- Presents a numbered menu and routes user choices to the appropriate
  `GradeManager` operations.
- Handles all input collection and validation (integers, decimals,
  non-empty strings) with clear error messages and re-prompting.

---

## Grading Scale

Letter grades are derived from a student's **average** marks across all
recorded subjects:

| Average Marks | Letter Grade |
|---------------|--------------|
| 90 – 100       | A            |
| 75 – 89        | B            |
| 60 – 74        | C            |
| 40 – 59        | D            |
| Below 40       | F            |

A student is considered **passing** if their average is `40.0` or higher.
Both the grading bands and the passing threshold are defined as constants
in `Student.java` and can be adjusted there if needed.

---

## How to Compile and Run

Requires a Java Development Kit (JDK 8 or later).

```bash
# Navigate to the source folder
cd CodeAlpha_Student_Grade_Tracker/src

# Compile all files
javac *.java

# Run the application
java Main
```

> **Note:** Always compile all three `.java` files together (or run
> `javac *.java`) and start the program with `java Main` — `Main` is the
> only class with a `main` method.

---

## How to Use

1. Launch the program with `java Main`.
2. Choose **1. Add Student** to create a new record — enter the student's
   name, then add one or more subjects and marks.
3. Use **2. View All Students** at any time to see the full class roster.
4. Use **6. Display Summary Report** for a complete statistical overview.
5. Use **7 / 8 / 9 / 10** to edit, delete, search, or sort records.
6. Use **11. Save Records to File** before exiting to keep your data, and
   **12. Load Records from File** the next time you run the program to
   pick up where you left off.
7. Choose **13. Exit** to close the application.

---

## CSV File Format

When you save records, they're written to a plain-text CSV file (default:
`students.csv`) with one line per student:

```
id,name,subject1:marks1;subject2:marks2;...
```

**Example:**

```
1,Alice,Math:92.0;Science:88.0
2,Bob,Math:55.0;English:60.0
3,Charlie,Math:30.0
```

This format is loaded back exactly as written, including each student's
original ID, so saved data can be safely reused across sessions.

---

## Possible Future Enhancements

- Graphical User Interface (JavaFX or Swing)
- Database-backed storage (SQLite / MySQL) instead of CSV
- Export summary reports to PDF
- Class/section grouping for larger student cohorts
- Weighted subject grading (e.g., credit hours)
- Unit tests for `Student` and `GradeManager`

---

## Author

**Mohd Ziyad**

---

## Acknowledgment

This project was developed as part of the **CodeAlpha Internship Program**,
which provided the opportunity to design and build a complete, real-world
console application from the ground up.
