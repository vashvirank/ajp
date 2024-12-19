SharedStudentApp/
├── src/
│   ├── model/
│   │   └── Student.java
│   ├── servlets/
│   │   ├── FirstServlet.java
│   │   └── SecondServlet.java
├── WebContent/
│   ├── index.html
│   └── WEB-INF/
│       └── web.xml


package model;

public class Student {
    private int[] rollNumbers;

    // Constructor to initialize roll numbers
    public Student() {
        rollNumbers = new int[]{101, 102, 103, 104, 105}; // Default roll numbers
    }

    public int[] getRollNumbers() {
        return rollNumbers;
    }
}


package servlets;

import model.Student;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/first")
public class FirstServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve Student object from context
        ServletContext context = getServletContext();
        Student student = (Student) context.getAttribute("student");

        // Display roll numbers
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h2>First Servlet: Displaying Roll Numbers</h2>");
        out.println("<ul>");
        for (int rollNo : student.getRollNumbers()) {
            out.println("<li>Roll Number: " + rollNo + "</li>");
        }
        out.println("</ul>");
        out.println("<a href='second'>Go to Second Servlet</a>");
        out.println("</body></html>");
    }
}


package servlets;

import model.Student;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/second")
public class SecondServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve Student object from context
        ServletContext context = getServletContext();
        Student student = (Student) context.getAttribute("student");

        // Display roll numbers
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h2>Second Servlet: Displaying Roll Numbers</h2>");
        out.println("<ul>");
        for (int rollNo : student.getRollNumbers()) {
            out.println("<li>Roll Number: " + rollNo + "</li>");
        }
        out.println("</ul>");
        out.println("<a href='first'>Go to First Servlet</a>");
        out.println("</body></html>");
    }
}


<web-app>
    <!-- Context Listener to Initialize Shared Student Object -->
    <listener>
        <listener-class>servlets.StudentContextListener</listener-class>
    </listener>

    <!-- First Servlet -->
    <servlet>
        <servlet-name>FirstServlet</servlet-name>
        <servlet-class>servlets.FirstServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>FirstServlet</servlet-name>
        <url-pattern>/first</url-pattern>
    </servlet-mapping>

    <!-- Second Servlet -->
    <servlet>
        <servlet-name>SecondServlet</servlet-name>
        <servlet-class>servlets.SecondServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>SecondServlet</servlet-name>
        <url-pattern>/second</url-pattern>
    </servlet-mapping>
</web-app>


package servlets;

import model.Student;
import javax.servlet.*;

public class StudentContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        // Initialize the Student object
        Student student = new Student();
        
        // Store in ServletContext
        ServletContext context = event.getServletContext();
        context.setAttribute("student", student);
        System.out.println("Shared Student object initialized and stored in context.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Context destroyed. Cleaning up...");
    }
}


<!DOCTYPE html>
<html>
<head>
    <title>Shared Student App</title>
</head>
<body>
    <h1>Welcome to the Shared Student Web Application</h1>
    <ul>
        <li><a href="first">Go to First Servlet</a></li>
        <li><a href="second">Go to Second Servlet</a></li>
    </ul>
</body>
</html>
