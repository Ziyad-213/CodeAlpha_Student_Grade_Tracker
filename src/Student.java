import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Student class encapsulates student information including a unique ID,
 * name, and marks across one or more subjects.
 * It demonstrates standard Object-Oriented Programming (OOP) principles:
 * Encapsulation (private fields, public getters/setters) and robust validation.
 *
 * Enhancements over the original version:
 *  - Supports multiple subjects per student (not just a single mark)
 *  - Auto-computes average, total, and a letter grade (A/B/C/D/F)
 *  - Tracks a unique, auto-generated Student ID
 *  - Tracks pass/fail status based on a configurable passing threshold
 *
 * Author: Mohd Ziyad (enhanced version)
 */
public class Student {

    /** Marks below this value (out of 100, per subject average) are considered failing. */
    public static final double PASS_THRESHOLD = 40.0;

    private static int nextId = 1;

    private final int studentId;
    private String studentName;
    private final Map<String, Double> subjectMarks;

    /**
     * Constructor to initialize a Student object with a name only.
     * Subjects/marks can be added afterward via addSubjectMark().
     *
     * @param studentName The name of the student.
     * @throws IllegalArgumentException if the name is null or blank.
     */
    public Student(String studentName) {
        this.studentId = nextId++;
        setStudentName(studentName);
        this.subjectMarks = new LinkedHashMap<>();
    }

    /**
     * Convenience constructor for a student with a single subject/mark,
     * preserved for backward compatibility with simpler use cases.
     *
     * @param studentName  The name of the student.
     * @param subject      The subject name.
     * @param marks        The marks for that subject (0.0 - 100.0).
     */
    public Student(String studentName, String subject, double marks) {
        this(studentName);
        addSubjectMark(subject, marks);
    }

    /**
     * Package-private constructor used when restoring a student from saved
     * data, so the original ID is preserved instead of a new one being assigned.
     *
     * @param studentId    the explicit ID to restore.
     * @param studentName  the student's name.
     */
    Student(int studentId, String studentName) {
        setStudentName(studentName);
        this.studentId = studentId;
        this.subjectMarks = new LinkedHashMap<>();
        if (studentId >= nextId) {
            nextId = studentId + 1;
        }
    }

    // ---------- ID ----------

    public int getStudentId() {
        return studentId;
    }

    /**
     * Resets the internal ID counter. Intended for use when reloading
     * student records from a file so IDs don't collide or restart oddly.
     *
     * @param nextAvailableId the next ID value to hand out.
     */
    public static void setNextId(int nextAvailableId) {
        if (nextAvailableId > nextId) {
            nextId = nextAvailableId;
        }
    }

    // ---------- Name ----------

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        if (studentName == null || studentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty or null.");
        }
        this.studentName = studentName.trim();
    }

    // ---------- Subjects & Marks ----------

    /**
     * Adds or updates the marks for a given subject.
     *
     * @param subject the subject name (e.g. "Math").
     * @param marks   the marks obtained (0.0 - 100.0).
     * @throws IllegalArgumentException if subject is blank or marks are out of range.
     */
    public void addSubjectMark(String subject, double marks) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be empty or null.");
        }
        if (marks < 0.0 || marks > 100.0) {
            throw new IllegalArgumentException("Marks must be between 0.0 and 100.0 (inclusive).");
        }
        subjectMarks.put(subject.trim(), marks);
    }

    /**
     * Removes a subject's marks entry.
     *
     * @param subject the subject to remove.
     * @return true if a subject was removed.
     */
    public boolean removeSubject(String subject) {
        return subjectMarks.remove(subject) != null;
    }

    public Map<String, Double> getSubjectMarks() {
        return subjectMarks;
    }

    public boolean hasSubjects() {
        return !subjectMarks.isEmpty();
    }

    // ---------- Derived statistics ----------

    /**
     * @return the total marks summed across all subjects.
     */
    public double getTotalMarks() {
        double total = 0;
        for (double m : subjectMarks.values()) {
            total += m;
        }
        return total;
    }

    /**
     * @return the average mark across all subjects, or 0.0 if no subjects recorded.
     */
    public double getStudentMarks() {
        if (subjectMarks.isEmpty()) {
            return 0.0;
        }
        return getTotalMarks() / subjectMarks.size();
    }

    /**
     * Computes a letter grade from the student's average marks.
     * A: 90-100, B: 75-89, C: 60-74, D: 40-59, F: below 40.
     *
     * @return the letter grade.
     */
    public char getGrade() {
        double avg = getStudentMarks();
        if (avg >= 90) return 'A';
        if (avg >= 75) return 'B';
        if (avg >= 60) return 'C';
        if (avg >= 40) return 'D';
        return 'F';
    }

    /**
     * @return true if the student's average is at/above the passing threshold.
     */
    public boolean isPassing() {
        return getStudentMarks() >= PASS_THRESHOLD;
    }

    /**
     * Returns a formatted String representation of the Student object.
     */
    @Override
    public String toString() {
        return String.format("Student[ID: %d, Name: %s, Average: %.2f, Grade: %c]",
                studentId, studentName, getStudentMarks(), getGrade());
    }
}
