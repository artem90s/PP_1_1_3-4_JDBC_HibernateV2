package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
    }
    Util util=new Util();

    public void createUsersTable() {
        try {
            Connection connection = util.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS User(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(64), lastName VARCHAR(64), age INT)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void dropUsersTable() {
        try {
            Connection connection = util.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS User");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void saveUser(String name, String lastName, byte age) {
        Connection connection = util.getConnection();
        PreparedStatement preparedStatement = null;
        String saveUserSql = "INSERT INTO User (name, lastName, age) VALUES (?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(saveUserSql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,lastName);
            preparedStatement.setByte(3,age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @Override
    public void removeUserById(long id) {
        Connection connection = util.getConnection();
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
        String sql="SELECT id, name, lastName, age FROM User";
        Connection connection = util.getConnection();
        Statement statement=null;
        try {
            statement=connection.createStatement();
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
        }finally {
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Statement statement= null;
        try {
            Connection connection = util.getConnection();
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
