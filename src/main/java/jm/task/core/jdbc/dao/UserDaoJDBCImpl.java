package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
    }
    private static Connection connection=Util.getConnection();

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS User(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(64), lastName VARCHAR(64), age INT)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement();){
            statement.executeUpdate("DROP TABLE IF EXISTS User");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void saveUser(String name, String lastName, byte age) {
        String saveUserSql = "INSERT INTO User (name, lastName, age) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(saveUserSql)){
            connection.setAutoCommit(false);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,lastName);
            preparedStatement.setByte(3,age);
            preparedStatement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        Connection connection = Util.getConnection();
        PreparedStatement preparedStatement=null;
        String sql = "DELETE FROM user WHERE id=?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<User> getAllUsers() {
        List<User>userList=new ArrayList<>();
        String sql="SELECT * FROM User";
        try (Connection connection = Util.getConnection();
             Statement statement=connection.createStatement()){
            ResultSet resultSet=statement.executeQuery(sql);
            while (resultSet.next()){
                User user=new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                userList.add(user);
            }
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Statement statement= null;
        try {
            Connection connection = Util.getConnection();
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            statement.executeUpdate("DELETE FROM user");
            statement.executeUpdate("ALTER TABLE user AUTO_INCREMENT=0");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
