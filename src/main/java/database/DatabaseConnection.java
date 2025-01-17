package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection {
    private static String dbUsername;
    private static String dbPassword;
    private static String dbSchemaUrl;
    //private static String dbEnabled;
    private static Properties properties = new Properties();

    public static void loadDatabaseProperties() {
        try (FileInputStream input = new FileInputStream("src/main/java/database/db.properties")) {
            // Load db.properties file
            properties.load(input);

            // Access individual properties in db.properties file
            //dbEnabled = properties.getProperty("db.enabled");
            dbUsername = properties.getProperty("db.username");
            dbPassword = properties.getProperty("db.password");
            dbSchemaUrl = properties.getProperty("db.schemaUrl");

        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unknown error...");
            e.printStackTrace();
        }
        // Create database table named "commands"
        createCommandsTable();
    }


    /**
     * Establishes a connection to the specific database SCHEMA using the properties loaded from the 'db.properties' file.
     * This method connects to a particular SCHEMA within the database, which allows operations on its tables/data
     *
     * @return A Connection object representing the connection to the database SCHEMA.
     *         If any error occurs during the connection process, the function will print the stack trace and return null.
     * @throws SQLException If a database access error occurs or the url is null
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbSchemaUrl, dbUsername, dbPassword);
    }

//    public static String isDatabaseEnabled() {
//        return dbEnabled;
//    }

    /**
     * This function attempts to create a table named "commands" in the database.
     * If the table already exists, the function will not create a new one.
     * The table has two columns: <b>"command_name" (VARCHAR(255) and PRIMARY KEY)</b> and <b>"command_uses" (INT with a default value of 0).</b>
     *
     * @return void
     * @throws SQLException If a database access error occurs or the url is null
     */
    public static void createCommandsTable() {
        int result = -1;
    
        try (Connection connection = getConnection()) {
            Statement stmt = connection.createStatement();
            // CREATE TABLE IF NOT EXISTS employees returns 0 for success (whether created or already exists)
            result = stmt.executeUpdate("CREATE TABLE IF NOT EXISTS commands (" +
                    "command_name VARCHAR(255) PRIMARY KEY," +
                    "command_uses INT DEFAULT 0" +
                    ");");
    
            if (result == -1) {
                System.err.println("Unexpected result, please run the program again...");
            }
            else {
                System.out.println("Table creation attempted! If it did not exist, it has been created!");
            }
    
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }
}
