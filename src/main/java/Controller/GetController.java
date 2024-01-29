package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level; // Add this import statement

import org.apache.struts2.convention.annotation.Namespace;

import com.opensymphony.xwork2.ActionSupport;

import Model.User;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

@Namespace("/api")

public class GetController extends ActionSupport{
        private User userBean;
        private static final Logger logger = LogManager.getLogger(GetController.class);
        private static Connection connection;
    private List<User> userList;


     public User getUserBean() {
        return userBean;
    }

    public void setUserBean(User user) {
        userBean = user;
    }

    public GetController() {
        super();
        userBean = new User(); // Initialize userBean
        initializeConnection1();
        // getAllUsers();
       
    }

    public void initializeConnection1() {
        Tracer tracer = GlobalTracer.get();
        Span span = tracer.buildSpan("initializeConnection1").start();
        String jdbcURL = "jdbc:postgresql://localhost:9001/kys";
        String dbUser = "postgres";
        String dbPassword = "redhat";

        try {
            logger.log(Level.INFO, "Connecting to the database...Get-------------");
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            if (connection != null && !connection.isClosed()) {
                logger.log(Level.INFO, "Database connection is ready.");
                
            }

        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.ERROR, "Error connecting to the database", e);
            span.log(e.getMessage());

        } finally {
            span.finish();
        }
    }

    public String getAllUsers() {
        Tracer tracer = GlobalTracer.get();
        Span span = tracer.buildSpan("getAllUsers")
            .withTag("operation", "getUsersFromDatabase")
            .start();
    
        try {

            logger.log(Level.INFO, "Connecting to the database...Get-------------");

            userList = getUsersFromDatabase(); // Set userList to be used in JSP
            span.log("Users retrieved successfully");
            return SUCCESS;
        } catch (Exception e) {
            span.setTag("error", true);
            span.log("Error retrieving users: " + e.getMessage());
            logger.log(Level.ERROR, "Error retrieving users", e);
            span.log(e.getMessage());

            return ERROR;
        } finally {
            span.finish();
        }
    }



private List<User> getUsersFromDatabase() {
    
    Tracer tracer = GlobalTracer.get();
    Span span = tracer.buildSpan("getUsersFromDatabase").start();
        List<User> userList = new ArrayList<>();

        try {
            String sql = "SELECT username, password FROM users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");

                        // Create User objects and add them to the list
                        User user = new User();
                        user.setUsername(username);
                        user.setPassword(password);

                        userList.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error retrieving users from the database", e);
            span.log(e.getMessage());

        }finally {
            span.finish();
        }

        return userList;
    }

    public List<User> getUserList() {
        return userList;
    }

}
