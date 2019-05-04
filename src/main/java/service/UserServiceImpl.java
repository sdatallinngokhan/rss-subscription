package service;

import model.SubscriptionType;
import model.User;
import model.UserRoleType;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    FileService fileService = new FileServiceImpl();

    @Override
    public User getUser(String username) {
        List<String> userList = fileService.readFile("/Users/gokhanpolat/Developer/rss-subscription/src/main/resources/user_management/users.txt");

        User user = new User();
        for (String userString : userList) {
            String[] userInfo = userString.split(" ");
            if (username.equals(userInfo[0])) {
                user.setUsername(userInfo[0]);
                user.setPassword(userInfo[1]);
                user.setUserRoleType(UserRoleType.valueOf(userInfo[2]));
                user.setSubscriptionType(SubscriptionType.valueOf(userInfo[3]));
                break;
            }
        }
        return user;
    }

    @Override
    public User getUser(String username, String password) {
        List<User> userList = getUserList();

        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public void saveUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        char[] usernameArray = username.toCharArray();
        char[] passwordArray = password.toCharArray();

        for (char u : usernameArray) {
            if (u == ' ') {
                System.out.println("Username contains space. Please save username without space");
                return;
            }
        }

        for (char p : passwordArray) {
            if (p == ' ') {
                System.out.println("Password contains space. Please save password without space");
                return;
            }
        }

        String pathString = "/Users/gokhanpolat/Developer/rss-subscription/src/main/resources/user_management/users.txt";
        List<String> userList = fileService.readFile(pathString);

        for (String userString : userList) {
            String[] userInfo = userString.split(" ");
            if (user.getUsername().equals(userInfo[0])) {
                System.out.println("There is already a user with " + user.getUsername());
                return;
            }
        }

        String newUser = user.getUsername() + " " + user.getPassword() + " " + user.getUserRoleType().name() + " " + user.getSubscriptionType().name();

        userList.add("\n");
        userList.add(newUser);

        fileService.writeFile(pathString, userList);
    }

    @Override
    public List<User> getUserList() {
        String usersTextPath = "/Users/gokhanpolat/Developer/rss-subscription/src/main/resources/user_management/users.txt";
        List<String> userListString = fileService.readFile(usersTextPath);

        List<User> userList = new ArrayList<>();
        for (String userString : userListString) {
            User user = new User();
            String[] userStringArray = userString.split(" ");
            user.setUsername(userStringArray[0]);
            user.setPassword(userStringArray[1]);
            user.setUserRoleType(UserRoleType.valueOf(userStringArray[2]));
            user.setSubscriptionType(SubscriptionType.valueOf(userStringArray[3]));

            userList.add(user);
        }

        return userList;
    }
}
