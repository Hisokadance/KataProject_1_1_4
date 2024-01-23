package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBCImpl implements UserDao {

    Connection connection;
    Util util;

    public UserDaoJDBCImpl() {
        util = new Util();
        connection = util.getConnection();
    }

    public void createUsersTable() {
        String create = "CREATE TABLE IF NOT EXISTS `users` (" +
                        " id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                        " name VARCHAR(45), " +
                        " lastName VARCHAR(45), " +
                        " age TINYINT); ";
        try (Statement statement = connection.createStatement()) {
            statement.execute(create);
            System.out.println("DBTable users has been created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        String drop = "DROP TABLE IF EXISTS users";
        try (Statement statement = connection.createStatement()) {
            statement.execute(drop);
            System.out.println("DBTable \"users\" has been dropped");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String save = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(save)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User name:" + name + "; lastName:" + lastName + "; age:" + age + " has been saved");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        String remove = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(remove)) {
            preparedStatement.setLong(1, id);
            System.out.println((preparedStatement.executeUpdate() > 0) ? "User id: " + id + " has been removed"
                    : "User id: " + id + " has not been removed. Wrong id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        String getAll = "SELECT * FROM users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User tempUser = new User(resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age"));
                tempUser.setId(resultSet.getLong("id"));
                result.add(tempUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("All users of the table \"users\" have been get");
        return result;
    }

    public void cleanUsersTable() {
        String clean = "TRUNCATE TABLE users";
        try (Statement statement = connection.createStatement()) {
            statement.execute(clean);
            System.out.println("Table \"users\" has been cleared");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}