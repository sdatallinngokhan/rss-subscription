import com.rometools.rome.feed.synd.SyndFeed;
import model.Feed;
import model.SubscriptionType;
import model.User;
import model.UserRoleType;
import service.*;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Application {

    private static Scanner scanner = new Scanner(System.in);
    private static FileService fileService = new FileServiceImpl();

    public static final String RSS_URL = "http://feeds.bbci.co.uk/news/world/rss.xml";
    public static final String SESSION_TXT_PATH = "/Users/gokhanpolat/Developer/rss-subscription/src/main/resources/user_management/session.txt";

    public static void main(String[] args) {
        System.out.println("////// WELCOME TO RSS FEED ///////");
        System.out.println();
        System.out.println();

        UserService userService = new UserServiceImpl();
        Application application = new Application();
        RssService rssService = new RssServiceImpl();
        RssToStringConverter rssToStringConverter = new RssToStringConverterImpl();

        User loggedInUser = application.getCurrentUser(userService);
        if (loggedInUser == null) {
            System.out.println("You are not registered yet!!");
            return;
        }

        System.out.println("You successfully logged in as " + loggedInUser.getUserRoleType().name());
        System.out.println();
        String option = application.showOptionsAndGetOption(loggedInUser.getUserRoleType().name());

        if (option.equals("wrong option")) {
            System.out.println("You choice wrong option");
            return;
        }

        SyndFeed syndFeed = rssService.readRss(RSS_URL);
        List<Feed> feedList = rssService.getFeedList(syndFeed);
        if (option.equals("1")) {
            String writingPath = "/Users/gokhanpolat/Developer/rss-subscription/src/main/resources/rss_feeds/" + loggedInUser.getUsername() + ".txt";
            if (loggedInUser.getSubscriptionType().name().equals("DEMO")) {
                List<Feed> limitedFeedList = feedList.stream().limit(10).collect(Collectors.toList());
                List<String> limitedFeedListString = rssToStringConverter.convert(limitedFeedList);
                fileService.writeFile(writingPath, limitedFeedListString);
                System.out.println("You are a DEMO user. You can see 10 Rss Feed in your file");
            } else {
                List<String> feedListString = rssToStringConverter.convert(feedList);
                fileService.writeFile(writingPath, feedListString);
                System.out.println("Your Rss Feed file has been created");
            }
        } else if (option.equals("2")) {
            System.out.println();
            System.out.println("//// ADDING USER ////");
            System.out.println();

            User userCandidate = new User();
            System.out.print("Username : ");
            userCandidate.setUsername(scanner.nextLine());
            System.out.print("Password : ");
            userCandidate.setPassword(scanner.nextLine());

            System.out.println("Current subscription options : " + Arrays.asList(SubscriptionType.values()));
            System.out.print("Enter subscription type : ");
            userCandidate.setSubscriptionType(SubscriptionType.valueOf(scanner.nextLine()));

            if (loggedInUser.getUserRoleType().equals(UserRoleType.MODERATOR)) {
                userCandidate.setUserRoleType(UserRoleType.CLIENT);
            } else {
                System.out.println("Current role types : " + Arrays.asList(UserRoleType.values()));
                System.out.print("Enter role type : ");
                userCandidate.setUserRoleType(UserRoleType.valueOf(scanner.nextLine()));
            }

            userService.saveUser(userCandidate);
        } else if (option.equals("3")) {
            System.out.println();
            System.out.println("/// UPDATING USER ///");
            System.out.println();

            System.out.print("Which user(username) you want to update? : ");
            String usernameToUpdate = scanner.nextLine();

            User updatedUserInfo = new User();
            updatedUserInfo.setUsername(usernameToUpdate);
            System.out.print("New password?: ");
            updatedUserInfo.setPassword(scanner.nextLine());

            if (loggedInUser.getUserRoleType().equals(UserRoleType.ADMIN)) {
                System.out.print("New role type?: ");
                updatedUserInfo.setUserRoleType(UserRoleType.valueOf(scanner.nextLine()));
            }

            System.out.print("New subscription type?: ");
            updatedUserInfo.setSubscriptionType(SubscriptionType.valueOf(scanner.nextLine()));

            String message = userService.updateUser(loggedInUser, updatedUserInfo);

            System.out.println(message);
        } else if (option.equals("4")) {
            System.out.println();
            System.out.println("/// DELETING USER ///");
            System.out.println();

            System.out.print("Which username you want to delete? : ");
            String usernameToDelete = scanner.nextLine();
            String message = userService.deleteUser(loggedInUser, usernameToDelete);
            System.out.println(message);
        }
    }

    private User getCurrentUser(UserService userService) {
        List<String> sessionList = fileService.readFile(SESSION_TXT_PATH);
        String session = sessionList.get(0);
        String[] sessionArray = session.split(" ");
        String username = sessionArray[0];
        long lastLoginMillis = Long.parseLong(sessionArray[1]);

        User loggedInUser = new User();
        if (System.currentTimeMillis() - lastLoginMillis < 180000) {
            loggedInUser = userService.getUser(username);
        } else {
            System.out.print("Your username : ");
            username = scanner.nextLine();

            System.out.print("Your password : ");
            String password = scanner.nextLine();

            loggedInUser = userService.getUser(username, password);
        }

        if (loggedInUser != null){
            List<String> updatedSession = Arrays.asList(username + " " + System.currentTimeMillis());
            fileService.writeFile(SESSION_TXT_PATH, updatedSession);
        }

        return loggedInUser;
    }

    private String showOptionsAndGetOption(String roleType) {
        String option = "";
        if (roleType.equals(UserRoleType.ADMIN.name()) || roleType.equals(UserRoleType.MODERATOR.name())) {
            System.out.println("1. Get Feed List");
            System.out.println("2. Add User");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. Logout");
            System.out.print("Your option? : ");
            option = scanner.nextLine();
        } else {
            System.out.println("1. Get Feed List");
            System.out.println("5. Logout");
            System.out.print("Your option? : ");
            option = scanner.nextLine();
        }

        if (roleType.equals(UserRoleType.CLIENT.name()) && option.equals("1")) {
            return option;
        } else if (!roleType.equals(UserRoleType.CLIENT.name()) && (option.equals("1") || option.equals("2") || option.equals("3") || option.equals("4"))) {
            return option;
        }

        return "wrong option";
    }
}
