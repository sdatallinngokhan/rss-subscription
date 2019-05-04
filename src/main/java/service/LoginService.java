package service;

import model.User;
import model.UserRoleType;

public interface LoginService {

    String loginAndGetUserRoleType(String username, String password);
}
