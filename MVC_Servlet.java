StudentApp/
├── src/
│   ├── controller/
│   │   ├── AddStudentServlet.java
│   │   ├── EditStudentServlet.java
│   │   ├── DeleteStudentServlet.java
│   │   ├── SearchStudentServlet.java
│   │   └── ViewStudentsServlet.java
│   ├── model/
│   │   ├── Student.java
│   │   └── StudentDAO.java
├── WebContent/
│   ├── addStudent.html
│   ├── searchStudent.html
│   ├── viewStudents.jsp
│   ├── updateStudent.jsp
│   └── WEB-INF/
│       └── web.xml
└── lib/
    └── mysql-connector-java-X.X.X.jar


package model;

public class Student {
    private int enrolmentNo;
    private String name;
    private int age;
    private String course;

    // Constructor
    public Student(int enrolmentNo, String name, int age, String course) {
        this.enrolmentNo = enrolmentNo;
        this.name = name;
        this.age = age;
        this.course = course;
    }

    // Getters and Setters
    public int getEnrolmentNo() { return enrolmentNo; }
    public void setEnrolmentNo(int enrolmentNo) { this.enrolmentNo = enrolmentNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
}


package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/student_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    // Add Student
    public void addStudent(Student student) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        String query = "INSERT INTO students (enrolment_no, name, age, course) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, student.getEnrolmentNo());
        ps.setString(2, student.getName());
        ps.setInt(3, student.getAge());
        ps.setString(4, student.getCourse());
        ps.executeUpdate();
        conn.close();
    }

    // Update Student
    public void updateStudent(Student student) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        String query = "UPDATE students SET name=?, age=?, course=? WHERE enrolment_no=?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, student.getName());
        ps.setInt(2, student.getAge());
        ps.setString(3, student.getCourse());
        ps.setInt(4, student.getEnrolmentNo());
        ps.executeUpdate();
        conn.close();
    }

    // Delete Student
    public void deleteStudent(int enrolmentNo) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        String query = "DELETE FROM students WHERE enrolment_no=?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, enrolmentNo);
        ps.executeUpdate();
        conn.close();
    }

    // Find Student by Enrollment No
    public Student findStudent(int enrolmentNo) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        String query = "SELECT * FROM students WHERE enrolment_no=?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, enrolmentNo);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Student student = new Student(
                rs.getInt("enrolment_no"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("course")
            );
            conn.close();
            return student;
        }
        conn.close();
        return null;
    }

    // View All Students
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        String query = "SELECT * FROM students";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            students.add(new Student(
                rs.getInt("enrolment_no"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("course")
            ));
        }
        conn.close();
        return students;
    }
}


@WebServlet("/AddStudent")
public class AddStudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int enrolmentNo = Integer.parseInt(request.getParameter("enrolment_no"));
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String course = request.getParameter("course");

        StudentDAO dao = new StudentDAO();
        try {
            dao.addStudent(new Student(enrolmentNo, name, age, course));
            response.sendRedirect("viewStudents.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

addstudent.html
<form action="AddStudent" method="post">
    Enrolment No: <input type="text" name="enrolment_no"><br>
    Name: <input type="text" name="name"><br>
    Age: <input type="number" name="age"><br>
    Course: <input type="text" name="course"><br>
    <input type="submit" value="Add Student">
</form>

  
viewstudent.jsp
<%@ page import="model.StudentDAO, model.Student" %>
<%
    StudentDAO dao = new StudentDAO();
    List<Student> students = dao.getAllStudents();
%>
<table border="1">
    <tr>
        <th>Enrolment No</th><th>Name</th><th>Age</th><th>Course</th>
    </tr>
    <% for(Student s : students) { %>
    <tr>
        <td><%= s.getEnrolmentNo() %></td>
        <td><%= s.getName() %></td>
        <td><%= s.getAge() %></td>
        <td><%= s.getCourse() %></td>
    </tr>
    <% } %>
</table>

