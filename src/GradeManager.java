import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The GradeManager class manages a list of Student objects.
 * It provides core operations for adding, editing, removing, and searching
 * students, plus statistical calculations (average, highest, lowest,
 * median, standard deviation, grade distribution, pass/fail counts) and
 * CSV-based persistence so data survives between runs.
 */
public class GradeManager {
    private final ArrayList<Student> studentList;

    public GradeManager() {
        this.studentList = new ArrayList<>();
    }

    // ---------- Basic CRUD ----------

    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student object cannot be null.");
        }
        studentList.add(student);
    }

    /**
     * Removes a student by their unique ID.
     *
     * @param studentId the ID of the student to remove.
     * @return true if a student was removed, false if no match was found.
     */
    public boolean removeStudent(int studentId) {
        return studentList.removeIf(s -> s.getStudentId() == studentId);
    }

    /**
     * Finds a student by exact (case-insensitive) name match.
     *
     * @param name the name to search for.
     * @return the matching Student, or null if not found.
     */
    public Student findByName(String name) {
        for (Student s : studentList) {
            if (s.getStudentName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Finds a student by their unique ID.
     *
     * @param studentId the ID to search for.
     * @return the matching Student, or null if not found.
     */
    public Student findById(int studentId) {
        for (Student s : studentList) {
            if (s.getStudentId() == studentId) {
                return s;
            }
        }
        return null;
    }

    /**
     * Returns all students whose name contains the given (case-insensitive) substring.
     *
     * @param query the search text.
     * @return list of matching students (possibly empty, never null).
     */
    public List<Student> searchByName(String query) {
        List<Student> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (Student s : studentList) {
            if (s.getStudentName().toLowerCase().contains(lowerQuery)) {
                results.add(s);
            }
        }
        return results;
    }

    public ArrayList<Student> getStudents() {
        return studentList;
    }

    public boolean isEmpty() {
        return studentList.isEmpty();
    }

    public int size() {
        return studentList.size();
    }

    // ---------- Sorting ----------

    public enum SortKey { NAME, AVERAGE, ID }

    /**
     * Sorts the student list in place.
     *
     * @param key        the field to sort by.
     * @param descending true for descending order, false for ascending.
     */
    public void sortStudents(SortKey key, boolean descending) {
        Comparator<Student> comparator;
        switch (key) {
            case NAME:
                comparator = Comparator.comparing(s -> s.getStudentName().toLowerCase());
                break;
            case AVERAGE:
                comparator = Comparator.comparingDouble(Student::getStudentMarks);
                break;
            case ID:
            default:
                comparator = Comparator.comparingInt(Student::getStudentId);
                break;
        }
        if (descending) {
            comparator = comparator.reversed();
        }
        studentList.sort(comparator);
    }

    // ---------- Display ----------

    public void displayStudents() {
        if (studentList.isEmpty()) {
            System.out.println("\n[!] No student records found. Add students first.");
            return;
        }

        System.out.println("\n=========================================================");
        System.out.println("                     STUDENT RECORDS                    ");
        System.out.println("=========================================================");
        System.out.printf("| %-4s | %-18s | %-8s | %-6s | %-5s |\n",
                "ID", "Student Name", "Average", "Grade", "Pass?");
        System.out.println("---------------------------------------------------------");
        for (Student student : studentList) {
            System.out.printf("| %-4d | %-18s | %-8.2f | %-6c | %-5s |\n",
                    student.getStudentId(),
                    student.getStudentName(),
                    student.getStudentMarks(),
                    student.getGrade(),
                    student.isPassing() ? "Yes" : "No");
        }
        System.out.println("=========================================================\n");
    }

    /**
     * Displays a detailed per-subject breakdown for a single student.
     */
    public void displayStudentDetail(Student student) {
        System.out.println("\n----- Student Detail -----");
        System.out.println("ID   : " + student.getStudentId());
        System.out.println("Name : " + student.getStudentName());
        if (student.hasSubjects()) {
            System.out.println("Subjects:");
            for (Map.Entry<String, Double> entry : student.getSubjectMarks().entrySet()) {
                System.out.printf("  - %-15s : %.2f\n", entry.getKey(), entry.getValue());
            }
        } else {
            System.out.println("Subjects: (none recorded)");
        }
        System.out.printf("Average: %.2f | Grade: %c | %s\n",
                student.getStudentMarks(), student.getGrade(),
                student.isPassing() ? "PASS" : "FAIL");
        System.out.println("---------------------------\n");
    }

    // ---------- Statistics ----------

    public double calculateAverage() {
        if (studentList.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (Student student : studentList) {
            sum += student.getStudentMarks();
        }
        return sum / studentList.size();
    }

    public Student getHighestScore() {
        if (studentList.isEmpty()) {
            return null;
        }
        Student highest = studentList.get(0);
        for (Student student : studentList) {
            if (student.getStudentMarks() > highest.getStudentMarks()) {
                highest = student;
            }
        }
        return highest;
    }

    public Student getLowestScore() {
        if (studentList.isEmpty()) {
            return null;
        }
        Student lowest = studentList.get(0);
        for (Student student : studentList) {
            if (student.getStudentMarks() < lowest.getStudentMarks()) {
                lowest = student;
            }
        }
        return lowest;
    }

    /**
     * @return the median average marks across all students.
     */
    public double calculateMedian() {
        if (studentList.isEmpty()) {
            return 0.0;
        }
        List<Double> marks = new ArrayList<>();
        for (Student s : studentList) {
            marks.add(s.getStudentMarks());
        }
        marks.sort(Double::compareTo);
        int n = marks.size();
        if (n % 2 == 0) {
            return (marks.get(n / 2 - 1) + marks.get(n / 2)) / 2.0;
        } else {
            return marks.get(n / 2);
        }
    }

    /**
     * @return the population standard deviation of student averages.
     */
    public double calculateStandardDeviation() {
        if (studentList.isEmpty()) {
            return 0.0;
        }
        double mean = calculateAverage();
        double sumSquaredDiffs = 0;
        for (Student s : studentList) {
            double diff = s.getStudentMarks() - mean;
            sumSquaredDiffs += diff * diff;
        }
        return Math.sqrt(sumSquaredDiffs / studentList.size());
    }

    /**
     * @return a map of letter grade -> count of students with that grade.
     */
    public Map<Character, Integer> getGradeDistribution() {
        Map<Character, Integer> distribution = new LinkedHashMap<>();
        distribution.put('A', 0);
        distribution.put('B', 0);
        distribution.put('C', 0);
        distribution.put('D', 0);
        distribution.put('F', 0);
        for (Student s : studentList) {
            char grade = s.getGrade();
            distribution.put(grade, distribution.getOrDefault(grade, 0) + 1);
        }
        return distribution;
    }

    /**
     * @return the count of students passing vs failing, as a two-element array [passCount, failCount].
     */
    public int[] getPassFailCounts() {
        int pass = 0, fail = 0;
        for (Student s : studentList) {
            if (s.isPassing()) pass++;
            else fail++;
        }
        return new int[]{pass, fail};
    }

    /**
     * Displays a professional summary report of the student grades,
     * including median, standard deviation, grade distribution, and pass/fail split.
     */
    public void displaySummary() {
        if (studentList.isEmpty()) {
            System.out.println("\n[!] No data available to generate a summary report.");
            return;
        }

        int totalStudents = studentList.size();
        double averageMarks = calculateAverage();
        double median = calculateMedian();
        double stdDev = calculateStandardDeviation();
        Student highest = getHighestScore();
        Student lowest = getLowestScore();
        Map<Character, Integer> distribution = getGradeDistribution();
        int[] passFail = getPassFailCounts();

        System.out.println("\n===========================================");
        System.out.println("             ## SUMMARY REPORT             ");
        System.out.println("===========================================");
        System.out.printf("  Total Students      : %d\n", totalStudents);
        System.out.printf("  Average Marks       : %.2f\n", averageMarks);
        System.out.printf("  Median Marks        : %.2f\n", median);
        System.out.printf("  Standard Deviation  : %.2f\n", stdDev);
        System.out.println("-------------------------------------------");
        System.out.printf("  Highest Marks Record:\n");
        System.out.printf("    Name: %s\n", highest.getStudentName());
        System.out.printf("    Marks: %.2f\n", highest.getStudentMarks());
        System.out.println("-------------------------------------------");
        System.out.printf("  Lowest Marks Record:\n");
        System.out.printf("    Name: %s\n", lowest.getStudentName());
        System.out.printf("    Marks: %.2f\n", lowest.getStudentMarks());
        System.out.println("-------------------------------------------");
        System.out.printf("  Pass / Fail         : %d / %d\n", passFail[0], passFail[1]);
        System.out.println("  Grade Distribution  :");
        for (Map.Entry<Character, Integer> entry : distribution.entrySet()) {
            System.out.printf("    %c : %d\n", entry.getKey(), entry.getValue());
        }
        System.out.println("===========================================\n");
    }

    // ---------- Persistence (CSV) ----------

    /**
     * Saves all students to a CSV file. Format per line:
     * id,name,subject1:marks1;subject2:marks2;...
     *
     * @param filePath path to the output file.
     * @throws IOException if writing fails.
     */
    public void saveToFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Student s : studentList) {
                StringBuilder line = new StringBuilder();
                line.append(s.getStudentId()).append(',').append(s.getStudentName()).append(',');
                boolean first = true;
                for (Map.Entry<String, Double> entry : s.getSubjectMarks().entrySet()) {
                    if (!first) line.append(';');
                    line.append(entry.getKey()).append(':').append(entry.getValue());
                    first = false;
                }
                writer.write(line.toString());
                writer.write(System.lineSeparator());
            }
        }
    }

    /**
     * Loads students from a CSV file previously written by saveToFile().
     * Replaces the current in-memory student list.
     *
     * @param filePath path to the input file.
     * @throws IOException if reading fails.
     */
    public void loadFromFile(String filePath) throws IOException {
        List<Student> loaded = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", 3);
                if (parts.length < 2) continue;

                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                Student student = new Student(id, name);

                if (parts.length == 3 && !parts[2].trim().isEmpty()) {
                    String[] subjectEntries = parts[2].split(";");
                    for (String subjectEntry : subjectEntries) {
                        String[] kv = subjectEntry.split(":");
                        if (kv.length == 2) {
                            student.addSubjectMark(kv[0].trim(), Double.parseDouble(kv[1].trim()));
                        }
                    }
                }
                loaded.add(student);
            }
        }
        studentList.clear();
        studentList.addAll(loaded);
    }
}
