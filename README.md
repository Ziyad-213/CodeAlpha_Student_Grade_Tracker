<div align="center">

# 📊 Student Grade Tracker

### A console-based Java application to manage student records, marks, and grade statistics.

![Java](https://img.shields.io/badge/Java-8%2B-orange?logo=openjdk&logoColor=white)
![Status](https://img.shields.io/badge/status-active-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)
![Internship](https://img.shields.io/badge/CodeAlpha-Internship%20Project-purple)

Built by **Mohd Ziyad** as part of the **CodeAlpha Internship Program**

</div>

---

## ✨ Features

- ➕ Add students with marks across multiple subjects
- 📋 View all students in a clean, tabular format
- 📈 Class average, highest, and lowest scores
- 📊 Full summary report — median, standard deviation, pass/fail split, grade distribution
- ✏️ Edit student names and subject marks
- 🗑️ Delete student records
- 🔍 Search students by full or partial name
- ↕️ Sort by name, average, or ID (ascending/descending)
- 💾 Save and load records to/from a CSV file
- ✅ Full input validation on every prompt

---

## 🗂️ Project Structure

```
CodeAlpha_Student_Grade_Tracker/
└── src/
    ├── Student.java        # Student model — name, marks, grade, pass/fail
    ├── GradeManager.java   # Collection management, statistics, CSV I/O
    └── Main.java           # Console menu / application entry point
```

---

## 🚀 Getting Started

### Prerequisites

- JDK 8 or later installed and on your `PATH`

### Compile & Run

```bash
git clone https://github.com/<your-username>/CodeAlpha_Student_Grade_Tracker.git
cd CodeAlpha_Student_Grade_Tracker/src

javac *.java
java Main
```

> Always compile all three files together and launch with `java Main` —
> that's the only class with a `main` method.

---

## 🎮 Usage

```
===== STUDENT GRADE TRACKER =====
 1. Add Student
 2. View All Students
 3. Calculate Average Marks
 4. Show Highest Marks
 5. Show Lowest Marks
 6. Display Summary Report
 7. Edit Student
 8. Delete Student
 9. Search Students by Name
10. Sort Students
11. Save Records to File
12. Load Records from File
13. Exit
---------------------------------
```

1. Add a student and their subject marks.
2. Explore stats with **View All Students** or **Summary Report**.
3. Edit, delete, search, or sort as needed.
4. Save before exiting — reload anytime with **Load Records from File**.

---

## 🎓 Grading Scale

| Average | Grade |
|---------|-------|
| 90–100  | A     |
| 75–89   | B     |
| 60–74   | C     |
| 40–59   | D     |
| < 40    | F     |

Passing threshold: **40.0**

---

## 💾 CSV Format

```
id,name,subject1:marks1;subject2:marks2;...
```

Example:

```
1,Alice,Math:92.0;Science:88.0
2,Bob,Math:55.0;English:60.0
```

---

## 🛣️ Roadmap

- [ ] GUI version (JavaFX / Swing)
- [ ] Database storage (SQLite / MySQL)
- [ ] PDF export for summary reports
- [ ] Weighted/credit-based grading
- [ ] Unit tests

---

## 🤝 Acknowledgment

Developed as part of the **[CodeAlpha](https://www.codealpha.tech/)**
internship program.

## 👤 Author

**Mohd Ziyad**

---

<div align="center">

If you found this project useful, consider giving it a ⭐!

</div>
