import com.rometools.rome.feed.synd.SyndFeed;
import model.Feed;
import model.User;
import model.UserRoleType;
import service.*;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Application {

    private static Scanner scanner = new Scanner(System.in);

    public static final String RSS_URL = "http://feeds.bbci.co.uk/news/world/rss.xml";

    public static void main(String[] args) {
        System.out.println("////// WELCOME TO RSS FEED ///////");
        System.out.println();
        System.out.println();

        UserService userService = new UserServiceImpl();
        Application application = new Application();
        RssService rssService = new RssServiceImpl();
        RssToStringConverter rssToStringConverter = new RssToStringConverterImpl();
        FileService fileService = new FileServiceImpl();

        System.out.print("Your username : ");
        String username = scanner.nextLine();
        System.out.print("Your password : ");
        String password = scanner.nextLine();

        User user = userService.getUser(username, password);

        if (user == null) {
            System.out.println("You are not registered yet!!");
            return;
        }

        System.out.println("You successfully logged in as " + user);
        System.out.println();
        String option = application.showOptionsAndGetOption(user.getUserRoleType().name());

        if (option.equals("wrong option")) {
            System.out.println("You choice wrong option");
            return;
        }

        SyndFeed syndFeed = rssService.readRss(RSS_URL);
        List<Feed> feedList = rssService.getFeedList(syndFeed);
        if (option.equals("1")) {
            String writingPath = "/Users/gokhanpolat/Developer/rss-subscription/src/main/resources/rss_feeds/" + user.getUsername() + ".txt";
            if (user.getSubscriptionType().name().equals("DEMO")) {
                List<Feed> limitedFeedList = feedList.stream().limit(10).collect(Collectors.toList());
                List<String> limitedFeedListString = rssToStringConverter.convert(limitedFeedList);
                fileService.writeFile(writingPath, limitedFeedListString);
                System.out.println("You are a DEMO user. You can see 10 Rss Feed in your file");
            } else {
                List<String> feedListString = rssToStringConverter.convert(feedList);
                fileService.writeFile(writingPath, feedListString);
                System.out.println("Your Rss Feed file has been created");
            }
        }
    }

    private String showOptionsAndGetOption(String roleType) {
        String option = "";
        if (roleType.equals(UserRoleType.ADMIN.name()) || roleType.equals(UserRoleType.MODERATOR.name())) {
            System.out.println("1. Get Feed List");
            System.out.println("2. Add User");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.print("Your option? : ");
            option = scanner.nextLine();
        } else {
            System.out.println("1. Get Feed List");
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
