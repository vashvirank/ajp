UserRegistrationApp/
├── src/
│   ├── dao/
│   │   └── UserDAO.java
│   ├── model/
│   │   └── User.java
│   ├── servlet/
│   │   └── UserServlet.java
├── WebContent/
│   ├── index.jsp
│   ├── addUser.jsp
│   ├── updateUser.jsp
│   ├── listUsers.jsp
│   ├── WEB-INF/
│   │   └── web.xml


package model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}


package dao;

import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/UserDB";
    private String jdbcUsername = "root";
    private String jdbcPassword = "password";

    private static final String INSERT_USER_SQL = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
    private static final String SELECT_USER_BY_ID = "SELECT id, name, email, password FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String UPDATE_USER_SQL = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    // Insert User
    public void insertUser(User user) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_USER_SQL)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
        }
    }

    // Select User by ID
    public User selectUser(int id) throws SQLException {
        User user = null;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_USER_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
            }
        }
        return user;
    }

    // Select All Users
    public List<User> selectAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_USERS)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        }
        return users;
    }

    // Update User
    public void updateUser(User user) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_USER_SQL)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getId());
            ps.executeUpdate();
        }
    }

    // Delete User
    public void deleteUser(int id) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_USER_SQL)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}


package servlet;

import dao.UserDAO;
import model.User;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "create":
                    createUser(request, response);
                    break;
                case "update":
                    updateUser(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "list":
                    listUsers(request, response);
                    break;
                default:
                    response.sendRedirect("index.jsp");
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = new User();
        user.setName(request.getParameter("name"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        userDAO.insertUser(user);
        response.sendRedirect("user?action=list");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = new User();
        user.setId(Integer.parseInt(request.getParameter("id")));
        user.setName(request.getParameter("name"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        userDAO.updateUser(user);
        response.sendRedirect("user?action=list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deleteUser(id);
        response.sendRedirect("user?action=list");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        User user = userDAO.selectUser(id);
        request.setAttribute("user", user);
        RequestDispatcher dispatcher = request.getRequestDispatcher("updateUser.jsp");
        dispatcher.forward(request, response);
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("users", userDAO.selectAllUsers());
        RequestDispatcher dispatcher = request.getRequestDispatcher("listUsers.jsp");
        dispatcher.forward(request, response);
    }
}


<!DOCTYPE html>
<html>
<head>
    <title>Add User</title>
</head>
<body>
    <h1>Add New User</h1>
    <form action="user?action=create" method="post">
        <table>
            <tr>
                <td>Name:</td>
                <td><input type="text" name="name" required></td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><input type="email" name="email" required></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type="password" name="password" required></td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="Add User">
                    <a href="user?action=list">Back to List</a>
                </td>
            </tr>
        </table>
    </form>
</body>
</html>


<%@ page import="model.User" %>
<%
    User user = (User) request.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Update User</title>
</head>
<body>
    <h1>Update User</h1>
    <form action="user?action=update" method="post">
        <input type="hidden" name="id" value="<%= user.getId() %>">
        <table>
            <tr>
                <td>Name:</td>
                <td><input type="text" name="name" value="<%= user.getName() %>" required></td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><input type="email" name="email" value="<%= user.getEmail() %>" required></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type="password" name="password" value="<%= user.getPassword() %>" required></td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="Update User">
                    <a href="user?action=list">Back to List</a>
                </td>
            </tr>
        </table>
    </form>
</body>
</html>


<%@ page import="model.User" %>
<%@ page import="java.util.List" %>
<%
    List<User> userList = (List<User>) request.getAttribute("users");
%>
<!DOCTYPE html>
<html>
<head>
    <title>List of Users</title>
</head>
<body>
    <h1>List of Users</h1>
    <a href="addUser.jsp">Add New User</a>
    <table border="1" width="50%" cellpadding="5">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Actions</th>
        </tr>
        <%
            if (userList != null) {
                for (User user : userList) {
        %>
        <tr>
            <td><%= user.getId() %></td>
            <td><%= user.getName() %></td>
            <td><%= user.getEmail() %></td>
            <td>
                <a href="user?action=edit&id=<%= user.getId() %>">Edit</a> |
                <a href="user?action=delete&id=<%= user.getId() %>" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="4">No users found!</td>
        </tr>
        <%
            }
        %>
    </table>
</body>
</html>


<!DOCTYPE html>
<html>
<head>
    <title>User Registration App</title>
</head>
<body>
    <h1>Welcome to the User Registration Module</h1>
    <ul>
        <li><a href="addUser.jsp">Add User</a></li>
        <li><a href="user?action=list">View All Users</a></li>
    </ul>
</body>
</html>
