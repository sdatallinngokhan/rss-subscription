package service;

import model.User;

public interface UserService {

    User getUser(String username);
    void saveUser(User user);
}
