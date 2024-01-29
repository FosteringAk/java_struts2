package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level; // Add this import statement


import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;

import Model.User;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

@Namespace("/api")
public class LoginController extends ActionSupport {

    private static final long serialVersionUID = 1L;
    private User userBean;
   // private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    private static final Logger logger = LogManager.getLogger(LoginController.class);
    private static Connection connection;
        private static final Tracer tracer = GlobalTracer.get();

    public User getUserBean() {
        return userBean;
    }

    public void setUserBean(User user) {
        userBean = user;
    }

    public LoginController() {
        super();
        userBean = new User();
        initializeConnection();
        createUser();
    }

    public void initializeConnection() {
        Tracer tracer = GlobalTracer.get();
        Span span = tracer.buildSpan("initializeConnection").start();
        String jdbcURL = "jdbc:postgresql://localhost:9001/kys";
        String dbUser = "postgres";
        String dbPassword = "redhat";

        try {
            logger.log(Level.INFO, "Connecting to the database...");
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            if (connection != null && !connection.isClosed()) {
                logger.log(Level.INFO, "Database connection is ready.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.ERROR, "Error connecting to the database", e);
        } finally {
            span.finish();
        }
    }

    // @Action(value = "createUser", results = {
    // @Result(name = "success", type = "json"),
    // @Result(name = "input", type = "json"),
    // @Result(name = "error", location = "/error.jsp")})
    public String createUser() {
        logger.log(Level.INFO, "Creating method call--------------------->");

        Tracer tracer = GlobalTracer.get();
        Span span = tracer.buildSpan("createUser").start();

        try {
            // Access HttpServletRequest to get JSON data
            HttpServletRequest request = ServletActionContext.getRequest();

            // Log request method and headers
            logger.log(Level.INFO, "Request Method: " + request.getMethod());
            logger.log(Level.INFO, "Request Headers:");
            Collections.list(request.getHeaderNames())
                    .forEach(headerName -> logger.log(Level.INFO, headerName + ": " + request.getHeader(headerName)));

            // Ensure that the request content type is JSON
            String contentType = request.getContentType();
            if (contentType != null && contentType.startsWith("application/json")) {
                // Get JSON data and log it
                String json = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
                logger.log(Level.INFO, "Request Body: " + json);

                setUserFromJson(json);

                saveUserToDatabase();

                return SUCCESS; // Indicate successful processing
            } else {
                logger.log(Level.WARN, "Invalid content type. Expected application/json.");

                // Add an action error to indicate the invalid content type
                addActionError("Invalid content type. Expected application/json.");

                return ERROR; // Indicate failure due to invalid content type
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, "Error reading request body", e);
            return ERROR; // Indicate failure due to IO exception
        } finally {
            span.finish();
        }
    }

    private void setUserFromJson(String json) {
        Tracer tracer = GlobalTracer.get();
        Span span = tracer.buildSpan("setUserFromJson").start();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Parse the JSON string to a User object
            User user = objectMapper.readValue(json, User.class);

            // Check if both username and password are present in the JSON
            if (user.getUsername() != null && !user.getUsername().isEmpty() &&
                    user.getPassword() != null && !user.getPassword().isEmpty()) {
                // Set the username and password from the parsed User object
                userBean.setUsername(user.getUsername());
                userBean.setPassword(user.getPassword());

                validate();
            } else {
                addActionError("Username or password is missing in the JSON data.");
                logger.log(Level.WARN, "Username or password is missing in the JSON data.");

            }
        } catch (IOException e) {
            // Handle JSON parsing exception
            logger.log(Level.ERROR, "Error parsing JSON: " + e.getMessage(), e);
        } finally {
            span.finish();
        }
    }

    private void saveUserToDatabase() {
        Tracer tracer = GlobalTracer.get();
        Span span = tracer.buildSpan("saveUserToDatabase").start();
        try {
            logger.log(Level.INFO, "Save  method call--------------------->");

            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, userBean.getUsername());
                preparedStatement.setString(2, userBean.getPassword());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    logger.log(Level.INFO, "User data inserted successfully.");
                } else {
                    logger.log(Level.WARN, "User data not inserted.");
                }

            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error saving user data to the database", e);
            e.printStackTrace(); // Log the stack trace
            span.log(e.getMessage());
        } finally {
            span.finish();
        }
    }

    public void validate() {
        if (userBean == null) {
            addActionError("Invalid user data.");
            return;
        }

        if (userBean.getUsername() == null || userBean.getUsername().trim().isEmpty()) {
            addFieldError("userBean.username", "Username is required.");
        }

        if (userBean.getPassword() == null || userBean.getPassword().trim().isEmpty()) {
            addFieldError("userBean.password", "Password is required.");
        }
    }
}
