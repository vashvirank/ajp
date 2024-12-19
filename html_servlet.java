<!DOCTYPE html>
<html>
<head>
    <title>Student Form</title>
</head>
<body>
    <h2>Student Form</h2>
    <form action="DisplayServlet" method="post">
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" required><br><br>

        <label for="age">Age:</label>
        <input type="number" id="age" name="age" required><br><br>

        <label for="course">Course:</label>
        <input type="text" id="course" name="course" required><br><br>

        <label for="enrollment">Enrollment No:</label>
        <input type="text" id="enrollment" name="enrollment" required><br><br>

        <input type="submit" value="Submit">
    </form>
</body>
</html>

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/DisplayServlet")
public class DisplayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Set content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Retrieve form data using request.getParameter()
        String name = request.getParameter("name");
        String age = request.getParameter("age");
        String course = request.getParameter("course");
        String enrollmentNo = request.getParameter("enrollment");

        // Display form data in response
        out.println("<html><head><title>Student Data</title></head><body>");
        out.println("<h2>Submitted Student Information</h2>");
        out.println("<table border='1' cellpadding='10'>");
        out.println("<tr><th>Field</th><th>Value</th></tr>");
        out.println("<tr><td>Name</td><td>" + name + "</td></tr>");
        out.println("<tr><td>Age</td><td>" + age + "</td></tr>");
        out.println("<tr><td>Course</td><td>" + course + "</td></tr>");
        out.println("<tr><td>Enrollment No</td><td>" + enrollmentNo + "</td></tr>");
        out.println("</table>");
        out.println("</body></html>");

        out.close();
    }
}

MyWebApp/
├── WebContent/
│   ├── form.html
│   ├── WEB-INF/
│   │   └── web.xml
└── src/
    └── DisplayServlet.java
