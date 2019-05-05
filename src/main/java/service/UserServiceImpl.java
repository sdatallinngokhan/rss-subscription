package service;

import model.SubscriptionType;
import model.User;
import model.UserRoleType;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final FileService fileService = new FileServiceImpl();
    private final String USERS_TXT_PATH = "/Users/gokhanpolat/Developer/rss-subscription/src/main/resources/user_management/users.txt";

    @Override
    public User getUser(String username) {
        List<String> userList = fileService.readFile(USERS_TXT_PATH);

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
            if (userStringArray.length < 4) {
                continue;
            }
            user.setUsername(userStringArray[0]);
            user.setPassword(userStringArray[1]);
            user.setUserRoleType(UserRoleType.valueOf(userStringArray[2]));
            user.setSubscriptionType(SubscriptionType.valueOf(userStringArray[3]));

            userList.add(user);
        }

        return userList;
    }

    @Override
    public String deleteUser(User loggedInUser, String usernameToDelete) {
        List<User> userList = getUserList();
        User userToDelete = getUser(usernameToDelete);

        List<User> refreshedUserList = new ArrayList<>();
        for (User user : userList) {
//          if (userToDelete.getUsername().equals(user.getUsername())) { // might get NullPointerException
            if (user.getUsername().equals(userToDelete.getUsername())) {
                if (userToDelete.getUsername().equals(loggedInUser.getUsername())) {
                    return "You can not delete yourself!!";
                }

                if (!userToDelete.getUserRoleType().equals(UserRoleType.CLIENT) && loggedInUser.getUserRoleType().equals(UserRoleType.MODERATOR)) {
                    return "You are not permitted for this operation!!";
                }

                continue;
            }

            refreshedUserList.add(user);
        }

        if (userList.size() == refreshedUserList.size()) {
            return usernameToDelete + " is not exist!!";
        }

        List<String> refreshedUserListString = new ArrayList<>();
        for (User refreshedUser : refreshedUserList) {
            String refreshedUserString = refreshedUser.getUsername() + " " + refreshedUser.getPassword() + " " + refreshedUser.getUserRoleType().name() + " " + refreshedUser.getSubscriptionType().name();
            refreshedUserListString.add(refreshedUserString);
        }

        fileService.writeFile(USERS_TXT_PATH, refreshedUserListString);

        return "You successfully deleted " + usernameToDelete;
    }

    @Override
    public String updateUser(User loggedInUser, User updatedUserInfo) {
        User userToUpdate = getUser(updatedUserInfo.getUsername());
        List<User> userList = getUserList();

        List<User> refreshedUserList = new ArrayList<>();
        for (User user : userList) {
            if (user.getUsername().equals(updatedUserInfo.getUsername())) {
                if (user.getUserRoleType().equals(UserRoleType.ADMIN) && loggedInUser.getUserRoleType().equals(UserRoleType.MODERATOR)) {
                    return "You are not allowed for this operation!!";
                }

                userToUpdate.setUsername(user.getUsername());
                userToUpdate.setPassword(updatedUserInfo.getPassword());
                userToUpdate.setSubscriptionType(updatedUserInfo.getSubscriptionType());

                if (loggedInUser.getUserRoleType().equals(UserRoleType.MODERATOR)) {
                    userToUpdate.setUserRoleType(user.getUserRoleType());
                } else {
                    userToUpdate.setUserRoleType(updatedUserInfo.getUserRoleType());
                }

                refreshedUserList.add(userToUpdate);
            } else {
                refreshedUserList.add(user);
            }
        }

        List<String> refreshedUserListString = new ArrayList<>();
        for (User refreshedUser : refreshedUserList) {
            String refreshedUserString = refreshedUser.getUsername() + " " + refreshedUser.getPassword() + " " + refreshedUser.getUserRoleType().name() + " " + refreshedUser.getSubscriptionType().name();
            refreshedUserListString.add(refreshedUserString);
        }

        fileService.writeFile(USERS_TXT_PATH, refreshedUserListString);

        return updatedUserInfo.getUsername() + " was updated successfully with new info";
    }
}
