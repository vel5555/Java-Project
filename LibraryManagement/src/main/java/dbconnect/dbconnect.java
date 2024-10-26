package dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbconnect {
    public static Connection connect() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:/home/velavan/LibraryManagement/database/library.db"); 
            // Update the path to where your `library.db` is stored
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // Handle exceptions
        }
        return connection;
    }
}
