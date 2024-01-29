// package Controller;
 
// import java.sql.*;
// import java.util.logging.Level;
// import java.util.logging.Logger;

// import Model.User;
 
// public class SQLController {
//         private static final Logger logger = Logger.getLogger(LoginController.class.getName());

//     private static Connection connection;
    
//     public void connect() throws ClassNotFoundException, SQLException {
//         String jdbcURL = "jdbc:postgresql://localhost:9001/kys";
//         String dbUser = "postgres";
//         String dbPassword = "redhat";
//         logger.log(Level.INFO, "quaery--------------------: {0}", dbPassword);

//         Class.forName("org.postgresql.Driver");
//         connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
//     }
    
    
//     public User checkLogin(String username, String password) throws SQLException,
//             ClassNotFoundException {
//                 String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
//              logger.log(Level.INFO, "quaery--------------------: {0}", sql);

//         PreparedStatement statement = connection.prepareStatement(sql);
//         statement.setString(1, username);
//         statement.setString(2, password);
 
//         ResultSet result = statement.executeQuery();
 
//         User user = null;
 
//         if (result.next()) {
//             user = new User();
//             user.setFullname(result.getString("full_name"));
//             user.setUsername(username);
//         }
 
//         connection.close();
 
//         return user;
//     }
// }