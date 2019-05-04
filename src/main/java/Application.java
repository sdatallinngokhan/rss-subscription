import model.UserRoleType;
import service.LoginService;
import service.LoginServiceImpl;

import java.util.Scanner;

public class Application {

    private static Scanner scanner = new Scanner(System.in);

    public static final String RSS_URL = "http://feeds.bbci.co.uk/news/world/rss.xml";

    public static void main(String[] args) {
        System.out.println("////// WELCOME TO RSS FEED ///////");
        System.out.println();
        System.out.println();

        LoginService loginService = new LoginServiceImpl();
        Application application = new Application();

        System.out.print("Your username : ");
        String username = scanner.nextLine();
        System.out.print("Your password : ");
        String password = scanner.nextLine();

        String message = loginService.loginAndGetUserRoleType(username, password);

        if (message.equals("You are not registered yet!!")) {
            System.out.println(message);
            return;
        }

        System.out.println("You successfully logged in as " + message);
        System.out.println();
        String option = application.showOptionsAndGetOption(message);

        if (option.equals("wrong option")) {
            System.out.println("You choice wrong option");
            return;
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
