package model;

public class Student {
    private int rollNo;
    private String name;
    private String course;
    private String email;

    // Constructor
    public Student() {
    }

    // Getters and Setters
    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


<%@ page import="model.Student" %>
<%
    // Create a new Student object
    Student student = new Student();
    
    // Set properties of the Student object (could be from form data or hardcoded)
    student.setRollNo(101);
    student.setName("John Doe");
    student.setCourse("Computer Science");
    student.setEmail("johndoe@example.com");
    
    // Set the Student object as an attribute to the request
    request.setAttribute("student", student);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Student Details</title>
</head>
<body>
    <h1>Student Information</h1>
    
    <h3>Details of the Student:</h3>
    <table border="1">
        <tr>
            <th>Roll No</th>
            <td><%= student.getRollNo() %></td>
        </tr>
        <tr>
            <th>Name</th>
            <td><%= student.getName() %></td>
        </tr>
        <tr>
            <th>Course</th>
            <td><%= student.getCourse() %></td>
        </tr>
        <tr>
            <th>Email</th>
            <td><%= student.getEmail() %></td>
        </tr>
    </table>

    <h3>Using JSTL (JSP Standard Tag Library):</h3>
    <table border="1">
        <tr>
            <th>Roll No</th>
            <td>${student.rollNo}</td>
        </tr>
        <tr>
            <th>Name</th>
            <td>${student.name}</td>
        </tr>
        <tr>
            <th>Course</th>
            <td>${student.course}</td>
        </tr>
        <tr>
            <th>Email</th>
            <td>${student.email}</td>
        </tr>
    </table>
</body>
</html>


