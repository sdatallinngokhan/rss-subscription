package service;

import model.User;
import model.UserRoleType;

import java.util.List;

public class LoginServiceImpl implements LoginService {

    @Override
    public String loginAndGetUserRoleType(String username, String password) {
        UserService userService = new UserServiceImpl();

        List<User> userList = userService.getUserList();

        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                UserRoleType userRoleType = user.getUserRoleType();
                return userRoleType.name();
            }
        }

        return "You are not registered yet!!";
    }
}
