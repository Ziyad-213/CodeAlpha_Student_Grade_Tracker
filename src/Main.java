import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class implementing a menu-driven console interface
 * for the Student Grade Tracker application.
 * Features robust input validation, multi-subject marks, editing/searching,
 * sorting, richer statistics, and CSV-based save/load persistence.
 *
 * Author: Mohd Ziyad (enhanced version)
 */
public class Main {
    private static final GradeManager gradeManager = new GradeManager();
    private static final String DEFAULT_FILE = "students.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("==============================================");
        System.out.println(" Welcome to CodeAlpha Student Grade Tracker! ");
        System.out.println("            (Enhanced Edition)               ");
        System.out.println("==============================================");

        while (running) {
            printMenu();
            int choice = readIntInput(scanner, "Enter your choice (1-13): ", 1, 13);

            switch (choice) {
                case 1: handleAddStudent(scanner); break;
                case 2: gradeManager.displayStudents(); break;
                case 3: handleCalculateAverage(); break;
                case 4: handleShowHighest(); break;
                case 5: handleShowLowest(); break;
                case 6: gradeManager.displaySummary(); break;
                case 7: handleEditStudent(scanner); break;
                case 8: handleDeleteStudent(scanner); break;
                case 9: handleSearchStudent(scanner); break;
                case 10: handleSortStudents(scanner); break;
                case 11: handleSaveToFile(scanner); break;
                case 12: handleLoadFromFile(scanner); break;
                case 13:
                    System.out.println("\nThank you for using Student Grade Tracker. Exiting application...");
                    running = false;
                    break;
                default:
                    System.out.println("[!] Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("===== STUDENT GRADE TRACKER =====");
        System.out.println(" 1. Add Student");
        System.out.println(" 2. View All Students");
        System.out.println(" 3. Calculate Average Marks");
        System.out.println(" 4. Show Highest Marks");
        System.out.println(" 5. Show Lowest Marks");
        System.out.println(" 6. Display Summary Report");
        System.out.println(" 7. Edit Student");
        System.out.println(" 8. Delete Student");
        System.out.println(" 9. Search Students by Name");
        System.out.println("10. Sort Students");
        System.out.println("11. Save Records to File");
        System.out.println("12. Load Records from File");
        System.out.println("13. Exit");
        System.out.println("---------------------------------");
    }

    // ---------- Add ----------

    private static void handleAddStudent(Scanner scanner) {
        System.out.println("\n--- Add Student Details ---");
        String name = readNonEmptyLine(scanner, "Enter Student Name: ");

        Student student;
        try {
            student = new Student(name);
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Failed to add student: " + e.getMessage());
            return;
        }

        boolean addingSubjects = true;
        while (addingSubjects) {
            String subject = readNonEmptyLine(scanner, "Enter Subject Name: ");
            double marks = readDoubleInput(scanner, "Enter Marks for " + subject + " (0.0 - 100.0): ", 0.0, 100.0);
            try {
                student.addSubjectMark(subject, marks);
            } catch (IllegalArgumentException e) {
                System.out.println("[!] " + e.getMessage());
                continue;
            }
            System.out.print("Add another subject for this student? (y/n): ");
            String more = scanner.nextLine().trim().toLowerCase();
            addingSubjects = more.equals("y") || more.equals("yes");
        }

        gradeManager.addStudent(student);
        System.out.println("\n[OK] Student Added Successfully!");
        gradeManager.displayStudentDetail(student);
    }

    // ---------- Stats shortcuts ----------

    private static void handleCalculateAverage() {
        if (gradeManager.isEmpty()) {
            System.out.println("\n[!] No records found. Add students first.");
            return;
        }
        double avg = gradeManager.calculateAverage();
        System.out.printf("\nClass Average Marks: %.2f\n\n", avg);
    }

    private static void handleShowHighest() {
        Student highest = gradeManager.getHighestScore();
        if (highest == null) {
            System.out.println("\n[!] No records found. Add students first.");
            return;
        }
        System.out.println("\nHighest Score:");
        gradeManager.displayStudentDetail(highest);
    }

    private static void handleShowLowest() {
        Student lowest = gradeManager.getLowestScore();
        if (lowest == null) {
            System.out.println("\n[!] No records found. Add students first.");
            return;
        }
        System.out.println("\nLowest Score:");
        gradeManager.displayStudentDetail(lowest);
    }

    // ---------- Edit ----------

    private static void handleEditStudent(Scanner scanner) {
        if (gradeManager.isEmpty()) {
            System.out.println("\n[!] No records found. Add students first.");
            return;
        }
        gradeManager.displayStudents();
        int id = readIntInput(scanner, "Enter the ID of the student to edit: ", 0, Integer.MAX_VALUE);
        Student student = gradeManager.findById(id);
        if (student == null) {
            System.out.println("[!] No student found with ID " + id + ".");
            return;
        }

        System.out.println("\n--- Editing " + student.getStudentName() + " ---");
        System.out.println("1. Rename student");
        System.out.println("2. Update/add a subject mark");
        System.out.println("3. Remove a subject");
        int action = readIntInput(scanner, "Choose an action (1-3): ", 1, 3);

        switch (action) {
            case 1:
                String newName = readNonEmptyLine(scanner, "Enter new name: ");
                try {
                    student.setStudentName(newName);
                    System.out.println("[OK] Name updated.");
                } catch (IllegalArgumentException e) {
                    System.out.println("[!] " + e.getMessage());
                }
                break;
            case 2:
                String subject = readNonEmptyLine(scanner, "Enter subject name: ");
                double marks = readDoubleInput(scanner, "Enter marks (0.0 - 100.0): ", 0.0, 100.0);
                try {
                    student.addSubjectMark(subject, marks);
                    System.out.println("[OK] Subject mark updated.");
                } catch (IllegalArgumentException e) {
                    System.out.println("[!] " + e.getMessage());
                }
                break;
            case 3:
                String toRemove = readNonEmptyLine(scanner, "Enter subject name to remove: ");
                if (student.removeSubject(toRemove)) {
                    System.out.println("[OK] Subject removed.");
                } else {
                    System.out.println("[!] Subject not found for this student.");
                }
                break;
        }
        gradeManager.displayStudentDetail(student);
    }

    // ---------- Delete ----------

    private static void handleDeleteStudent(Scanner scanner) {
        if (gradeManager.isEmpty()) {
            System.out.println("\n[!] No records found. Add students first.");
            return;
        }
        gradeManager.displayStudents();
        int id = readIntInput(scanner, "Enter the ID of the student to delete: ", 0, Integer.MAX_VALUE);
        boolean removed = gradeManager.removeStudent(id);
        if (removed) {
            System.out.println("[OK] Student with ID " + id + " deleted.\n");
        } else {
            System.out.println("[!] No student found with ID " + id + ".\n");
        }
    }

    // ---------- Search ----------

    private static void handleSearchStudent(Scanner scanner) {
        if (gradeManager.isEmpty()) {
            System.out.println("\n[!] No records found. Add students first.");
            return;
        }
        String query = readNonEmptyLine(scanner, "Enter a name (or partial name) to search: ");
        List<Student> results = gradeManager.searchByName(query);
        if (results.isEmpty()) {
            System.out.println("[!] No students matched \"" + query + "\".\n");
            return;
        }
        System.out.println("\nFound " + results.size() + " match(es):");
        for (Student s : results) {
            gradeManager.displayStudentDetail(s);
        }
    }

    // ---------- Sort ----------

    private static void handleSortStudents(Scanner scanner) {
        if (gradeManager.isEmpty()) {
            System.out.println("\n[!] No records found. Add students first.");
            return;
        }
        System.out.println("Sort by: 1. Name  2. Average Marks  3. ID");
        int keyChoice = readIntInput(scanner, "Choose (1-3): ", 1, 3);
        System.out.println("Order: 1. Ascending  2. Descending");
        int orderChoice = readIntInput(scanner, "Choose (1-2): ", 1, 2);

        GradeManager.SortKey key = keyChoice == 1 ? GradeManager.SortKey.NAME
                : keyChoice == 2 ? GradeManager.SortKey.AVERAGE
                : GradeManager.SortKey.ID;
        boolean descending = orderChoice == 2;

        gradeManager.sortStudents(key, descending);
        System.out.println("[OK] Students sorted.");
        gradeManager.displayStudents();
    }

    // ---------- File I/O ----------

    private static void handleSaveToFile(Scanner scanner) {
        if (gradeManager.isEmpty()) {
            System.out.println("\n[!] No records to save. Add students first.");
            return;
        }
        String path = readOptionalLine(scanner,
                "Enter file path to save (leave blank for '" + DEFAULT_FILE + "'): ", DEFAULT_FILE);
        try {
            gradeManager.saveToFile(path);
            System.out.println("[OK] Saved " + gradeManager.size() + " record(s) to " + path + "\n");
        } catch (IOException e) {
            System.out.println("[!] Failed to save file: " + e.getMessage() + "\n");
        }
    }

    private static void handleLoadFromFile(Scanner scanner) {
        String path = readOptionalLine(scanner,
                "Enter file path to load (leave blank for '" + DEFAULT_FILE + "'): ", DEFAULT_FILE);
        try {
            gradeManager.loadFromFile(path);
            System.out.println("[OK] Loaded " + gradeManager.size() + " record(s) from " + path + "\n");
        } catch (IOException e) {
            System.out.println("[!] Failed to load file: " + e.getMessage() + "\n");
        }
    }

    // ---------- Input helpers ----------

    private static String readNonEmptyLine(Scanner scanner, String prompt) {
        String value = "";
        while (value.isEmpty()) {
            System.out.print(prompt);
            value = scanner.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("[!] Error: This field cannot be empty. Please try again.");
            }
        }
        return value;
    }

    private static String readOptionalLine(Scanner scanner, String prompt, String defaultValue) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();
        return value.isEmpty() ? defaultValue : value;
    }

    private static int readIntInput(Scanner scanner, String prompt, int min, int max) {
        int val = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                val = Integer.parseInt(raw);
                if (val < min || val > max) {
                    System.out.printf("[!] Please enter an integer between %d and %d.\n", min, max);
                } else {
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid input type. Please enter a valid number.");
            }
        }
        return val;
    }

    private static double readDoubleInput(Scanner scanner, String prompt, double min, double max) {
        double val = -1.0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                val = Double.parseDouble(raw);
                if (val < min || val > max) {
                    System.out.printf("[!] Please enter a value between %.2f and %.2f.\n", min, max);
                } else {
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid numeric format. Please enter a valid decimal number (e.g. 85.5).");
            }
        }
        return val;
    }
}
