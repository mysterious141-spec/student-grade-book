import java.util.Scanner;

/**
 * Student Grade Book
 * Uses a 1D array for student names and a 2D array (students x subjects)
 * for scores. scores[i][j] is the mark of student i in subject j.
 */
public class GradeBook {

    static final int MAX_STUDENTS = 10;
    static final String[] SUBJECTS = {"Maths", "English", "Science", "ICT"};

    static String[] names = new String[MAX_STUDENTS];
    static double[][] scores = new double[MAX_STUDENTS][SUBJECTS.length];
    static int count = 0;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Starting data
        addStudent("Ama", new double[]{78, 85, 72, 90});
        addStudent("Kofi", new double[]{64, 58, 70, 66});
        addStudent("Esi", new double[]{92, 88, 95, 97});
        addStudent("Yaw", new double[]{45, 52, 48, 60});

        int choice;
        do {
            System.out.println("\n===== STUDENT GRADE BOOK =====");
            System.out.println("1. View all students");
            System.out.println("2. Add a student");
            System.out.println("3. Update a score");
            System.out.println("4. Subject statistics");
            System.out.println("5. Class ranking");
            System.out.println("6. Search for a student");
            System.out.println("7. Exit");
            System.out.print("Choose: ");

            try {
                choice = Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                choice = 0;
            }

            switch (choice) {
                case 1: viewAll(); break;
                case 2: addNew(in); break;
                case 3: updateScore(in); break;
                case 4: subjectStats(); break;
                case 5: ranking(); break;
                case 6: search(in); break;
                case 7: System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid choice. Enter 1-7.");
            }
        } while (choice != 7);
    }

    static void addStudent(String name, double[] marks) {
        names[count] = name;
        for (int j = 0; j < SUBJECTS.length; j++) {
            scores[count][j] = marks[j];
        }
        count++;
    }

    // Average of one student's row
    static double average(int i) {
        double total = 0;
        for (int j = 0; j < SUBJECTS.length; j++) {
            total += scores[i][j];
        }
        return total / SUBJECTS.length;
    }

    static char grade(double avg) {
        if (avg >= 80) return 'A';
        if (avg >= 70) return 'B';
        if (avg >= 60) return 'C';
        if (avg >= 50) return 'D';
        return 'F';
    }

    static void viewAll() {
        if (count == 0) {
            System.out.println("No students yet.");
            return;
        }
        System.out.printf("%n%-4s %-12s", "No.", "Name");
        for (String s : SUBJECTS) System.out.printf("%9s", s);
        System.out.printf("%9s %6s%n", "Average", "Grade");
        System.out.println("---------------------------------------------------------------");
        for (int i = 0; i < count; i++) {
            System.out.printf("%-4d %-12s", i + 1, names[i]);
            for (int j = 0; j < SUBJECTS.length; j++) {
                System.out.printf("%9.0f", scores[i][j]);
            }
            double avg = average(i);
            System.out.printf("%9.1f %6c%n", avg, grade(avg));
        }
    }

    static void addNew(Scanner in) {
        if (count == MAX_STUDENTS) {
            System.out.println("Class is full (" + MAX_STUDENTS + " students).");
            return;
        }
        System.out.print("Student name: ");
        String name = in.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        if (indexOf(name) != -1) {
            System.out.println("That student already exists.");
            return;
        }
        double[] marks = new double[SUBJECTS.length];
        try {
            for (int j = 0; j < SUBJECTS.length; j++) {
                System.out.print(SUBJECTS[j] + " score (0-100): ");
                marks[j] = Double.parseDouble(in.nextLine().trim());
                if (marks[j] < 0 || marks[j] > 100) {
                    System.out.println("Scores must be between 0 and 100. Student not added.");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Student not added.");
            return;
        }
        addStudent(name, marks);
        System.out.println(name + " added.");
    }

    static void updateScore(Scanner in) {
        viewAll();
        System.out.print("Enter student number: ");
        int i = readIndex(in.nextLine());
        if (i == -1) {
            System.out.println("Invalid student number.");
            return;
        }
        for (int j = 0; j < SUBJECTS.length; j++) {
            System.out.println((j + 1) + ". " + SUBJECTS[j]);
        }
        System.out.print("Choose subject: ");
        int j;
        try {
            j = Integer.parseInt(in.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            j = -1;
        }
        if (j < 0 || j >= SUBJECTS.length) {
            System.out.println("Invalid subject.");
            return;
        }
        System.out.print("New score (0-100): ");
        try {
            double mark = Double.parseDouble(in.nextLine().trim());
            if (mark < 0 || mark > 100) {
                System.out.println("Score must be between 0 and 100.");
                return;
            }
            scores[i][j] = mark;
            System.out.printf("%s's %s score is now %.0f.%n", names[i], SUBJECTS[j], mark);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    // Goes down each column of the 2D array
    static void subjectStats() {
        if (count == 0) {
            System.out.println("No students yet.");
            return;
        }
        System.out.printf("%n%-10s %9s %9s %9s%n", "Subject", "Average", "Highest", "Lowest");
        System.out.println("-----------------------------------------");
        for (int j = 0; j < SUBJECTS.length; j++) {
            double total = 0;
            double high = scores[0][j];
            double low = scores[0][j];
            for (int i = 0; i < count; i++) {
                total += scores[i][j];
                if (scores[i][j] > high) high = scores[i][j];
                if (scores[i][j] < low) low = scores[i][j];
            }
            System.out.printf("%-10s %9.1f %9.0f %9.0f%n", SUBJECTS[j], total / count, high, low);
        }
    }

    // Sorts a list of positions by average, highest first
    static void ranking() {
        if (count == 0) {
            System.out.println("No students yet.");
            return;
        }
        int[] order = new int[count];
        for (int i = 0; i < count; i++) order[i] = i;
        for (int a = 0; a < count - 1; a++) {
            for (int b = 0; b < count - 1 - a; b++) {
                if (average(order[b]) < average(order[b + 1])) {
                    int temp = order[b];
                    order[b] = order[b + 1];
                    order[b + 1] = temp;
                }
            }
        }
        System.out.println("\nClass ranking:");
        for (int r = 0; r < count; r++) {
            int i = order[r];
            System.out.printf("%d. %-12s %6.1f  (%c)%n", r + 1, names[i], average(i), grade(average(i)));
        }
    }

    static void search(Scanner in) {
        System.out.print("Search for: ");
        String term = in.nextLine().trim().toLowerCase();
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (names[i].toLowerCase().contains(term)) {
                System.out.printf("%s | average %.1f | grade %c%n", names[i], average(i), grade(average(i)));
                found = true;
            }
        }
        if (!found) System.out.println("No matching students.");
    }

    static int indexOf(String name) {
        for (int i = 0; i < count; i++) {
            if (names[i].equalsIgnoreCase(name)) return i;
        }
        return -1;
    }

    // Converts the typed student number (1-based) into an array index, or -1
    static int readIndex(String input) {
        try {
            int n = Integer.parseInt(input.trim());
            if (n >= 1 && n <= count) return n - 1;
        } catch (NumberFormatException e) {
            // fall through
        }
        return -1;
    }
}