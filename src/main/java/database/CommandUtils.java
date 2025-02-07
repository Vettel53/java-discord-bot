package database;

import java.sql.SQLException;

public class CommandUtils {

    /**
     * Increments the usage count of a specific command in the database.
     *
     * @param commandName The name of the command for which to increment the usage count.
     *
     * @throws SQLException If an error occurs while connecting to the database or executing the update operation.
     */
    public static void incrementCommandUsage(String commandName) {
        try {
            CommandUsageDAO dao = new CommandUsageDAO(DatabaseConnection.getConnection());
            dao.incrementCommandUsage(commandName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Fetches the usage count of a specific command from the database.
     *
     * @param commandName The name of the command for which to retrieve the usage count.
     * @return The usage count of the specified command. If an error occurs during the database operation,
     *         this method will return 0.
     */
    public static int fetchCommandUsageCount(String commandName) {
        int usageCount = 0;
        try {
            CommandUsageDAO dao = new CommandUsageDAO(DatabaseConnection.getConnection());
            usageCount = dao.retrieveCommandUsage(commandName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usageCount; // Return the command usage count (0 if error occurs)
    }

}
