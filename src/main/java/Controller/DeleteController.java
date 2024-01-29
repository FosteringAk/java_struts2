package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;

import Model.User;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import jaeger.CustomSpanned;

@CustomSpanned
public class DeleteController extends ActionSupport {

    private User userBean;
    private static final Logger logger = LogManager.getLogger(DeleteController.class);
    private static Connection connection;
    private List<User> userList;

    public User getUserBean() {
        return userBean;
    }

    public void setUserBean(User user) {
        userBean = user;
    }

    public DeleteController() {
        super();
        userBean = new User(); // Initialize userBean
        initializeConnection3();

    }

    public void initializeConnection3() {
        // Tracer tracer = GlobalTracer.get();
        // Span span = tracer.buildSpan("initializeConnection3").start();
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
           // span.log(e.getMessage());

        } finally {
           // span.finish();
        }
    }

    public String deleteUser() throws SQLException {

        logger.log(Level.INFO, "method call.......................");

        // Tracer tracer = GlobalTracer.get();
        // Span span = tracer.buildSpan("deleteUser").start();

        try {

            logger.log(Level.INFO, "Connecting to the database...EDITTTTT-------------");

            HttpServletRequest request = ServletActionContext.getRequest();
            logger.log(Level.INFO, "Request Method: " + request.getMethod());
            logger.log(Level.INFO, "Request Headers:");
            Collections.list(request.getHeaderNames())
                    .forEach(headerName -> logger.log(Level.INFO, headerName + ": " + request.getHeader(headerName)));

            // Ensure that the request content type is JSON
            String contentType = request.getContentType();
            if (contentType != null && contentType.startsWith("application/json")) {
                // Get JSON data and set it to userBean
                setUserFromJson(request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual));

                // Retrieve the new username and password from the userBean
                String newUsername = userBean.getUsername();
                // String newPassword = userBean.getPassword();

                // Update the user's information in the database
                deleteUserFromDatabase(newUsername);

                return SUCCESS;
            } else {
                logger.log(Level.WARN, "Invalid content type. Expected application/json.");
                // Return appropriate error response to the client
                return ERROR;
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, "Error editing user", e);
           // span.log(e.getMessage());

            // Handle the exception appropriately
            return ERROR;
        } finally {
            //span.finish();
        }
    }
 private void setUserFromJson(String json) {
    // Tracer tracer = GlobalTracer.get();
    // Span span = tracer.buildSpan("setUserFromJson").start();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Parse the JSON string to a User object
            User user = objectMapper.readValue(json, User.class);
            // Set the username and password from the parsed User object
            userBean.setUsername(user.getUsername());
            // userBean.setPassword(user.getPassword());
        } catch (IOException e) {
            // Handle JSON parsing exception
            logger.log(Level.ERROR, "Error parsing JSON: " + e.getMessage(), e);
           // span.log(e.getMessage());

        }finally {
            //span.finish();
        }
    }
    private void deleteUserFromDatabase(String username) {
        // Tracer tracer = GlobalTracer.get();
        // Span span = tracer.buildSpan("deleteUserFromDatabase").start();

        try {
            String sql = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    logger.log(Level.INFO, "User deleted successfully.");
                } else {
                    logger.log(Level.WARN, "User not found or not deleted.");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error deleting user from the database", e);
           // span.log(e.getMessage());

        } finally {
            //span.finish();
        }
    }

}
