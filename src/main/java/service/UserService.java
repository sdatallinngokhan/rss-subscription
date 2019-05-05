package service;

import model.User;
import model.UserRoleType;

import java.util.List;

public interface UserService {

    User getUser(String username);
    User getUser(String username, String password);
    void saveUser(User user);
    List<User> getUserList();

    String deleteUser(User loggedInUser, String usernameToDelete);

    String updateUser(User loggedInUser, String usernameToUpdate, User updatedUserInfo);
}
