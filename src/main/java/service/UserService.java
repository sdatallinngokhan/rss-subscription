package service;

import model.User;
import model.UserRoleType;

import java.util.List;

public interface UserService {

    User getUser(String username);
    User getUser(String username, String password);
    void saveUser(User user);
    List<User> getUserList();
}
