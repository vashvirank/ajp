StudentManagementHibernateApp/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── dao/
│   │   │   │   └── StudentDAO.java
│   │   │   ├── model/
│   │   │   │   └── Student.java
│   │   │   ├── util/
│   │   │   │   └── HibernateUtil.java
│   │   │   └── main/
│   │   │       └── Main.java
│   │
│   ├── resources/
│   │   └── hibernate.cfg.xml
│
├── lib/
│   ├── hibernate-core-5.6.10.Final.jar
│   ├── mysql-connector-java-8.0.28.jar
│   └── c3p0-0.9.5.5.jar  (Optional for connection pooling)
│
├── target/ (generated by Maven after building the project)

├── pom.xml (if using Maven)



pom.xml
<dependencies>
    <!-- Hibernate Core -->
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>5.6.10.Final</version>
    </dependency>

    <!-- MySQL JDBC Driver -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.28</version>
    </dependency>

    <!-- JSTL for web-based applications (optional) -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>4.0.1</version>
        <scope>provided</scope>
    </dependency>
</dependencies>



CREATE DATABASE StudentDB;

USE StudentDB;

CREATE TABLE Student (
    enrollment_no VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100),
    sem INT,
    div CHAR(1),
    dept VARCHAR(50),
    sgpa FLOAT,
    cgpa FLOAT
);


hibernate.cfg.xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN" "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <!-- JDBC Database connection settings -->
    <session-factory>
        <!-- JDBC Database connection settings -->
        <property name="hibernate.dialect">org.hibernate.dialect.MySQL8Dialect</property>
        <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/StudentDB</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">password</property>

        <!-- JDBC connection pool settings -->
        <property name="hibernate.c3p0.min_size">5</property>
        <property name="hibernate.c3p0.max_size">20</property>
        <property name="hibernate.c3p0.timeout">300</property>
        <property name="hibernate.c3p0.max_statements">50</property>
        <property name="hibernate.c3p0.idle_test_period">3000</property>

        <!-- Specify dialect -->
        <property name="hibernate.dialect">org.hibernate.dialect.MySQL8Dialect</property>

        <!-- Enable Hibernate's automatic session context management -->
        <property name="hibernate.current_session_context_class">thread</property>

        <!-- Echo all executed SQL to stdout -->
        <property name="hibernate.show_sql">true</property>

        <!-- Drop and re-create the database schema on startup -->
        <property name="hibernate.hbm2ddl.auto">update</property>

        <!-- Enable Hibernate's query language -->
        <property name="hibernate.enable_lazy_load_no_trans">true</property>
    </session-factory>
</hibernate-configuration>



package model;

import javax.persistence.*;

@Entity
@Table(name = "Student")
public class Student {
    @Id
    @Column(name = "enrollment_no")
    private String enrollmentNo;

    @Column(name = "name")
    private String name;

    @Column(name = "sem")
    private int sem;

    @Column(name = "div")
    private String div;

    @Column(name = "dept")
    private String dept;

    @Column(name = "sgpa")
    private float sgpa;

    @Column(name = "cgpa")
    private float cgpa;

    // Getters and Setters
    public String getEnrollmentNo() {
        return enrollmentNo;
    }

    public void setEnrollmentNo(String enrollmentNo) {
        this.enrollmentNo = enrollmentNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSem() {
        return sem;
    }

    public void setSem(int sem) {
        this.sem = sem;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public float getSgpa() {
        return sgpa;
    }

    public void setSgpa(float sgpa) {
        this.sgpa = sgpa;
    }

    public float getCgpa() {
        return cgpa;
    }

    public void setCgpa(float cgpa) {
        this.cgpa = cgpa;
    }
}


package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().addAnnotatedClass(model.Student.class).buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}


package dao;

import model.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

public class StudentDAO {

    // Create a new student record
    public void addStudent(Student student) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(student);
        transaction.commit();
        session.close();
    }

    // Retrieve a student by enrollment number
    public Student getStudent(String enrollmentNo) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Student student = session.get(Student.class, enrollmentNo);
        session.close();
        return student;
    }

    // Retrieve all students
    public List<Student> getAllStudents() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<Student> query = session.createQuery("from Student", Student.class);
        List<Student> students = query.list();
        session.close();
        return students;
    }

    // Update student record
    public void updateStudent(Student student) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(student);
        transaction.commit();
        session.close();
    }

    // Delete a student record
    public void deleteStudent(String enrollmentNo) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Student student = session.get(Student.class, enrollmentNo);
        if (student != null) {
            session.delete(student);
        }
        transaction.commit();
        session.close();
    }
}


package main;

import dao.StudentDAO;
import model.Student;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize DAO
        StudentDAO studentDAO = new StudentDAO();

        // Create a new student object
        Student student = new Student();
        student.setEnrollmentNo("12345");
        student.setName("John Doe");
        student.setSem(5);
        student.setDiv("A");
        student.setDept("Computer Science");
        student.setSgpa(8.5f);
        student.setCgpa(8.3f);

        // Add student to database
        studentDAO.addStudent(student);

        // Retrieve and display student
        Student retrievedStudent = studentDAO.getStudent("12345");
        System.out.println("Student Name: " + retrievedStudent.getName());

        // Retrieve and display all students
        List<Student> students = studentDAO.getAllStudents();
        for (Student s : students) {
            System.out.println(s.getName() + " - " + s.getDept());
        }

        // Update student record
        retrievedStudent.setName("John Smith");
        studentDAO.updateStudent(retrievedStudent);

        // Delete student
        studentDAO.deleteStudent("12345");
    }
}


