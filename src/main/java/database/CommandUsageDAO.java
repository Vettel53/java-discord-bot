package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommandUsageDAO {

    private Connection connection;

     /**
     * Constructor for CommandUsageDAO.
     *
     * @param connection The database connection object.
     */
     public CommandUsageDAO(Connection connection) {
        this.connection = connection;
     }

     /**
      * Increments the usage count of a specific command in the database.
      * If the command does not exist, it will be inserted with a usage count of 1.
      * If the command already exists (ON DUPLICATE), the usage count will be updated by 1.
      *
      * @param commandName The name of the command to increment the usage count for.
      */
     public void incrementCommandUsage(String commandName) {
         String sqlStatement = "INSERT INTO commands (command_name, command_uses) VALUES (?, 1) " +
                 "ON DUPLICATE KEY UPDATE command_uses = command_uses + 1";
     
         try (PreparedStatement pstmt = connection.prepareStatement(sqlStatement)) {
             pstmt.setString(1, commandName);
             pstmt.executeUpdate();
         } catch (SQLException e) {
             System.err.println("Error updating command usage: " + e.getMessage());
             e.printStackTrace();
         }
     }

}
