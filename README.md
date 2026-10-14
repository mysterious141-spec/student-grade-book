# Arrays in Real-World Applications: Student Grade Book

**Live app:** https://mysterious141-spec.github.io/student-grade-book/

The project has two versions that use the same array design: a Java console program (`GradeBook.java`) and a web app (`index.html`) deployed with GitHub Pages.

## Assignment Statement

> Describe real-world applications that require arrays as a data structure, explain why arrays are necessary, and choose one application to develop.

---

## 1. Real-World Applications That Require Arrays

An array is a fixed-size, ordered collection of items of the same type, stored side by side in memory and accessed by an index. Many everyday systems depend on this structure.

| Application | How arrays are used |
|---|---|
| **Student records and grade books** | Names and a table of scores (students x subjects) stored in arrays |
| **Banking** | A customer's list of transactions or monthly balances |
| **Image processing** | A photo is a 2D array of pixels (rows x columns), each holding a colour value |
| **Seat booking** | Cinema, bus, train or flight seats modelled as a grid of rows and columns |
| **Inventory and point of sale** | Item prices and stock levels held in parallel arrays |
| **Sensor and weather data** | Hourly temperature readings stored in sequence for analysis |
| **Games** | Chess, draughts, tic-tac-toe and Sudoku boards are 2D arrays |
| **Spreadsheets and tables** | Rows and columns of data are 2D arrays |

---

## 2. Why Arrays Are Necessary

1. **Fast direct access.** Any element is reached immediately by its index (`scores[2][1]`) without searching through the others.
2. **Grouping related data.** One array replaces dozens of separate variables such as `maths1`, `maths2`, `maths3`.
3. **Easy processing with loops.** A single `for` loop can read, update or total thousands of items in a few lines of code.
4. **Efficient memory use.** Elements are stored next to each other in memory, which makes reading and processing fast.
5. **Natural fit for tables and grids.** Two-dimensional arrays represent rows and columns directly, such as score sheets, seats and images.
6. **Foundation for other structures.** Lists, stacks, queues and matrices are built on arrays.

---

## 3. Chosen Application: Student Grade Book

### Problem
A teacher needs a simple system to store the scores of students in several subjects, work out each student's average and grade, update a score, see the class statistics for each subject, and rank the students.

### Why arrays suit this problem
- A score sheet is a table: one row per student and one column per subject, which is exactly a **2D array** (`scores[student][subject]`).
- The class size and the number of subjects are known, which fits the fixed size of an array.
- A student's average is a loop along a **row**; a subject's average, highest and lowest marks are a loop down a **column**.
- Any mark is reached instantly, e.g. `scores[2][1]` is the third student's English mark.

### Data design
| Array | Type | Purpose |
|---|---|---|
| `names[MAX_STUDENTS]` | `String` | Name of each student |
| `scores[MAX_STUDENTS][4]` | `double` | Score of each student in each subject |
| `SUBJECTS[4]` | `String` | Subject names: Maths, English, Science, ICT |

`names[i]` and `scores[i][j]` use the same student index `i`. A counter called `count` tracks how many students are stored.

### Features
1. View all students with scores, average and grade
2. Add a student (rejects empty names, duplicates and scores outside 0-100)
3. Update a score
4. Subject statistics: average, highest and lowest per subject
5. Class ranking from highest to lowest average
6. Search for a student by name
7. Exit

Grades: A (80 and above), B (70-79), C (60-69), D (50-59), F (below 50).

### How the program works
1. The menu repeats in a `do-while` loop until the user chooses Exit.
2. `addStudent()` stores the name and copies the marks into row `count` of the 2D array, then increases `count`.
3. `average(i)` loops along row `i` and divides the total by the number of subjects.
4. `subjectStats()` loops down each column to find the average, highest and lowest mark of every subject.
5. `ranking()` sorts a list of student positions by average (bubble sort) and prints them in order.
6. `readIndex()` converts the student number typed by the user into an array index and rejects invalid input.

---

## 4. The Code (`GradeBook.java`)

```java
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
```

### How to run
Requires Java (JDK 8 or later).

```
javac GradeBook.java
java GradeBook
```

---

## 5. Sample Output

```
===== STUDENT GRADE BOOK =====
1. View all students
...
Choose: 1

No.  Name            Maths  English  Science      ICT  Average  Grade
---------------------------------------------------------------
1    Ama                78       85       72       90     81.3      A
2    Kofi               64       58       70       66     64.5      C
3    Esi                92       88       95       97     93.0      A
4    Yaw                45       52       48       60     51.3      D
```

Subject statistics:

```
Subject      Average   Highest    Lowest
-----------------------------------------
Maths           69.8        92        45
English         70.8        88        52
Science         71.3        95        48
ICT             78.3        97        60
```

---

## 6. Conclusion

Arrays are essential wherever data comes in an ordered, fixed-size collection or a table. In the grade book, a 2D array holds the whole score sheet, gives instant access to any mark, and lets simple loops work along rows (one student) or down columns (one subject). The same idea powers inventory systems, image processing, game boards and many other real-world applications.
