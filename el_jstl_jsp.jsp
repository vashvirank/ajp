pom.xml
<dependency>
    <groupId>javax.servlet.jsp.jstl</groupId>
    <artifactId>jstl</artifactId>
    <version>1.2.5</version>
</dependency>

      
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Student" %>
<%
    // Create a new Student object and set its properties
    Student student = new Student();
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
    
    <h3>Details of the Student (Using Scriptlet to Set the Data):</h3>
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

    <h3>Details of the Student (Using JSTL `<c:out>`):</h3>
    <table border="1">
        <tr>
            <th>Roll No</th>
            <td><c:out value="${student.rollNo}" /></td>
        </tr>
        <tr>
            <th>Name</th>
            <td><c:out value="${student.name}" /></td>
        </tr>
        <tr>
            <th>Course</th>
            <td><c:out value="${student.course}" /></td>
        </tr>
        <tr>
            <th>Email</th>
            <td><c:out value="${student.email}" /></td>
        </tr>
    </table>
</body>
</html>




