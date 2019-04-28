package service;

import model.SubscriptionType;
import model.User;
import model.UserRoleType;

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
    public void saveUser(User user) {
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
}
