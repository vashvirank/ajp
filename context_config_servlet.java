WebApp/
├── src/
│   ├── MyServlet.java
│   └── AppHitsServlet.java
├── WebContent/
│   ├── index.html
│   └── WEB-INF/
│       └── web.xml


<web-app>
    <!-- Context Param (Application-level init parameter) -->
    <context-param>
        <param-name>supportEmail</param-name>
        <param-value>support@mywebapp.com</param-value>
    </context-param>

    <!-- Servlet for Displaying Email Address -->
    <servlet>
        <servlet-name>EmailServlet</servlet-name>
        <servlet-class>MyServlet</servlet-class>
        <init-param>
            <param-name>adminEmail</param-name>
            <param-value>admin@mywebapp.com</param-value>
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>EmailServlet</servlet-name>
        <url-pattern>/email</url-pattern>
    </servlet-mapping>

    <!-- Servlet for Tracking Hits -->
    <servlet>
        <servlet-name>HitsServlet</servlet-name>
        <servlet-class>AppHitsServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>HitsServlet</servlet-name>
        <url-pattern>/hits</url-pattern>
    </servlet-mapping>
</web-app>


import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

public class MyServlet extends HttpServlet {
    private String adminEmail;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Retrieve servlet init parameter
        adminEmail = config.getInitParameter("adminEmail");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve context (application) init parameter
        ServletContext context = getServletContext();
        String supportEmail = context.getInitParameter("supportEmail");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h2>Email Addresses</h2>");
        out.println("<p>Admin Email (Servlet Init Param): " + adminEmail + "</p>");
        out.println("<p>Support Email (Context Init Param): " + supportEmail + "</p>");
        out.println("</body></html>");
    }
}


import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

public class AppHitsServlet extends HttpServlet {
    private int servletHits = 0; // Counter for this servlet

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Increment the servlet-specific counter
        servletHits++;

        // Get application-wide counter from context
        ServletContext context = getServletContext();
        Integer appHits = (Integer) context.getAttribute("appHits");

        if (appHits == null) {
            appHits = 1;
        } else {
            appHits++;
        }
        // Update application-wide counter in context
        context.setAttribute("appHits", appHits);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h2>Hit Counters</h2>");
        out.println("<p>Servlet Hits (This Servlet): " + servletHits + "</p>");
        out.println("<p>Application-wide Hits: " + appHits + "</p>");
        out.println("</body></html>");
    }
}


index.html
<!DOCTYPE html>
<html>
<head>
    <title>Web Application</title>
</head>
<body>
    <h2>Welcome to the Web Application</h2>
    <ul>
        <li><a href="email">View Email Addresses</a></li>
        <li><a href="hits">View Hit Counters</a></li>
    </ul>
</body>
</html>
