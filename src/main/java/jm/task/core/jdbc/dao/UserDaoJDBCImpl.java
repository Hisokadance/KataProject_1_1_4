package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final String CREATE_USERS_QUERY = "CREATE TABLE IF NOT EXISTS `users` " +
                                                     "(" + " id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                                                     "" + " " + " name VARCHAR(45), " +
                                                     "" + " lastName VARCHAR(45), " + " age TINYINT); ";
    private static final String DROP_USERS_QUERY = "DROP TABLE IF EXISTS users";
    private static final String SAVE_USERS_QUERY = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
    private static final String REMOVE_USERS_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String CLEAN_USERS_QUERY = "TRUNCATE TABLE users";

    private final Connection connection;

    public UserDaoJDBCImpl() {
        connection = Util.getConnection();
    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_USERS_QUERY);
            System.out.println("DBTable users has been created");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create users table " + e.getMessage());
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(DROP_USERS_QUERY);
            System.out.println("DBTable \"users\" has been dropped");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to drop users table " + e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USERS_QUERY)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User name: " + name + "; lastName: " + lastName + "; age: " + age + " has been saved");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user" + e.getMessage());
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_USERS_QUERY)) {
            preparedStatement.setLong(1, id);
            boolean removed = preparedStatement.executeUpdate() > 0;
            System.out.println("User id: " + id + " has been " + (removed ? "" : "not ") + "removed");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove user" + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User tempUser = new User(resultSet.getString("name"), resultSet.getString("lastName"), resultSet.getByte("age"));
                tempUser.setId(resultSet.getLong("id"));
                result.add(tempUser);
            }
            System.out.println("All users of the table \"users\" have been retrieved");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all users" + e.getMessage());
        }
        return result;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CLEAN_USERS_QUERY);
            System.out.println("Table \"users\" has been cleared");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clean users table" + e.getMessage());
        }
    }
}