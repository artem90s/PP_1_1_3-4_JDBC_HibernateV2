package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Артем", "Щербаков", (byte) 31);
        userService.saveUser("Артур", "Петров", (byte) 20);
        userService.saveUser("Ксения", "Иванова", (byte) 27);
        userService.saveUser("Мария", "Магдалина", (byte) 25);
        userService.getAllUsers();
        userService.removeUserById(2);
        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
