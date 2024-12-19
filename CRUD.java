// Write a JDBC desktop program to perform following CRUD and Search operation. Create
// appropriate table in database to store objects of Student class.
// 1. Add Student. (Create)
// 2. View Students. (Display all students)
// 3. Edit Student. (Update)
// 4. Delete Student. (Delete)
// 5. Search Student (Find student based on enrolment No)

import java.sql.*;
import java.util.Scanner;

public class StudentCRUD {
    // JDBC Database URL, Username, and Password
    static final String URL = "jdbc:mysql://localhost:3306/student_db";
    static final String USER = "root";
    static final String PASSWORD = "";

    // Main menu
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Register JDBC Driver
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            while (true) {
                System.out.println("\n--- Student Management System ---");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Edit Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Search Student");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addStudent(connection, scanner);
                        break;
                    case 2:
                        viewStudents(connection);
                        break;
                    case 3:
                        editStudent(connection, scanner);
                        break;
                    case 4:
                        deleteStudent(connection, scanner);
                        break;
                    case 5:
                        searchStudent(connection, scanner);
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        connection.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    // 1. Add Student
    private static void addStudent(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Enrolment No: ");
            int enrolmentNo = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Course: ");
            String course = scanner.nextLine();

            String query = "INSERT INTO students (enrolment_no, name, age, course) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, enrolmentNo);
            ps.setString(2, name);
            ps.setInt(3, age);
            ps.setString(4, course);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Student added successfully!");
            } else {
                System.out.println("Failed to add student.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. View Students
    private static void viewStudents(Connection connection) {
        try {
            String query = "SELECT * FROM students";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            System.out.println("\n--- Student List ---");
            System.out.printf("%-15s%-20s%-10s%-20s\n", "Enrolment No", "Name", "Age", "Course");
            System.out.println("----------------------------------------------------------");

            while (rs.next()) {
                int enrolmentNo = rs.getInt("enrolment_no");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String course = rs.getString("course");

                System.out.printf("%-15d%-20s%-10d%-20s\n", enrolmentNo, name, age, course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. Edit Student
    private static void editStudent(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Enrolment No to edit: ");
            int enrolmentNo = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter New Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter New Age: ");
            int age = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter New Course: ");
            String course = scanner.nextLine();

            String query = "UPDATE students SET name = ?, age = ?, course = ? WHERE enrolment_no = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, course);
            ps.setInt(4, enrolmentNo);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. Delete Student
    private static void deleteStudent(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Enrolment No to delete: ");
            int enrolmentNo = scanner.nextInt();

            String query = "DELETE FROM students WHERE enrolment_no = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, enrolmentNo);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 5. Search Student
    private static void searchStudent(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Enrolment No to search: ");
            int enrolmentNo = scanner.nextInt();

            String query = "SELECT * FROM students WHERE enrolment_no = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, enrolmentNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\n--- Student Details ---");
                System.out.println("Enrolment No: " + rs.getInt("enrolment_no"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Course: " + rs.getString("course"));
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
// In the Project Explorer, right-click on the src folder of your project.
// Select New > Package and enter a name, e.g., com.student.crud.
// Right-click on the newly created package and select New > Class.
// Enter the class name as StudentCRUD.
// Check the public static void main(String[] args) option to generate the main method.
// Click Finish.
