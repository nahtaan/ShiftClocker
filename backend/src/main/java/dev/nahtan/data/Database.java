package dev.nahtan.data;

import dev.nahtan.Config;
import dev.nahtan.Main;
import org.tinylog.Logger;

import java.sql.*;

@SuppressWarnings("SqlNoDataSourceInspection")
public class Database {
    private Connection connection;

    public boolean connect() {
        Config config = Main.getConfig();
        String connection_url = String.format("jdbc:mysql://%s:%s@%s:%s/%s", config.getDb_username(), config.getDb_password(), config.getDb_host(), config.getDb_port(), config.getDb_name());
        try {
            connection = DriverManager.getConnection(connection_url);
        }catch (SQLException e) {
            Logger.error("Could not connect to database. {}", e.getMessage());
            return false;
        }
        return createTables();
    }

    private boolean createTables() {
        try{
            String SQL = """
CREATE TABLE IF NOT EXISTS users (
    id int PRIMARY KEY AUTO_INCREMENT,
    email varchar(255) NOT NULL UNIQUE,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    salt varchar(255) NOT NULL
)
""";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.execute();
            SQL = """
CREATE TABLE IF NOT EXISTS shifts (
    id int PRIMARY KEY AUTO_INCREMENT,
    user_id int NOT NULL,
    start_time bigint NOT NULL,
    end_time bigint NOT NULL,
    duration bigint NOT NULL,
    rate float,
    location varchar(255) NOT NULL,
    payment_received boolean NOT NULL
)
""";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.execute();
            preparedStatement.close();
        }catch (SQLException e){
            Logger.error("Could not create tables in database. {}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * This method will add a new user to the database.
     * @param email The user's email.
     * @param username The user's username.
     * @param password The user's password HASH. Make sure this value has been hashed prior to being passed to this method.
     * @param salt The user's password SALT.
     * @return True - The user was successfully added. False - Something went wrong whilst creating the user.
     */
    public boolean createNewUser(String email, String username, String password, String salt) {
        try{
            String SQL = """
                    INSERT INTO users (email, username, password, salt) VALUES (?, ?, ?, ?)
                    """;
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, email);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setString(4, salt);
            statement.execute();
            statement.close();
            return true;
        }catch (SQLException e){
            Logger.error("Could not add user to table. {}", e.getMessage());
            return false;
        }
    }

    /**
     * Checks if an email is already being used by another user.
     * @param email The email to check for.
     * @return True - The email is in use. False - The email is not in use.
     */
    public boolean emailInUse(String email) {
        try {
            String SQL = "SELECT * FROM users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            Logger.error("Could not check if email already in use. {}", e.getMessage());
            // return true anyways
            return true;
        }
    }

    /**
     * Updates the email of a user using their old email as an identifier.
     * @param oldEmail The user's previous email.
     * @param newEmail The user's new email.
     * @return True - The change was successful. False - The change was not successful.
     */
    public boolean updateUserEmail(String oldEmail, String newEmail) {
        try {
            String SQL = "UPDATE users SET email = ? WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, newEmail);
            statement.setString(2, oldEmail);
            statement.executeUpdate();
            return true;
        }catch (SQLException e){
            Logger.error("Could not update user email. {}", e.getMessage());
            return false;
        }
    }


}
