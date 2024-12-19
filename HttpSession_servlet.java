login.html
  <!DOCTYPE html>
<html>
<head>
    <title>Login Page</title>
</head>
<body>
    <h2>Login</h2>
    <form action="LoginServlet" method="post">
        Username: <input type="text" name="username" required><br><br>
        Password: <input type="password" name="password" required><br><br>
        <input type="submit" value="Login">
    </form>
</body>
</html>


import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Simulated user data
        String validUsername = "admin";
        String validPassword = "password123";

        // Retrieve form data
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (validUsername.equals(username) && validPassword.equals(password)) {
            // Create or retrieve the session
            HttpSession session = request.getSession();
            session.setAttribute("username", username); // Store user info in session
            session.setAttribute("loginTime", new java.util.Date());

            out.println("<h3>Login Successful!</h3>");
            out.println("<a href='DashboardServlet'>Go to Dashboard</a>");
        } else {
            out.println("<h3>Invalid Credentials!</h3>");
            out.println("<a href='login.html'>Try Again</a>");
        }
    }
}


import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Retrieve the session
        HttpSession session = request.getSession(false); // false prevents creating a new session
        if (session != null) {
            String username = (String) session.getAttribute("username");
            java.util.Date loginTime = (java.util.Date) session.getAttribute("loginTime");

            out.println("<h2>Welcome, " + username + "!</h2>");
            out.println("<p>Login Time: " + loginTime + "</p>");
            out.println("<a href='LogoutServlet'>Logout</a>");
        } else {
            out.println("<h3>No active session found. Please <a href='login.html'>Login</a> again.</h3>");
        }
    }
}


import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Destroy the session
        }

        out.println("<h3>You have been logged out successfully.</h3>");
        out.println("<a href='login.html'>Login Again</a>");
    }
}


StudentApp/
├── src/
│   ├── controller/
│   │   ├── LoginServlet.java
│   │   ├── DashboardServlet.java
│   │   └── LogoutServlet.java
│   └── model/
│       └── User.java
├── WebContent/
│   ├── login.html
│   ├── WEB-INF/
│   │   └── web.xml
└── lib/
    └── mysql-connector-java-X.X.X.jar
