import service.LoginService;
import service.LoginServiceImpl;

import java.util.Scanner;

public class Application {

    public static final String RSS_URL = "http://feeds.bbci.co.uk/news/world/rss.xml";

    public static void main(String[] args) {
        System.out.println("////// WELCOME TO RSS FEED ///////");
        System.out.println();
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        LoginService loginService = new LoginServiceImpl();

        System.out.print("Your username : ");
        String username = scanner.nextLine();
        System.out.print("Your password : ");
        String password = scanner.nextLine();

        String message = loginService.loginAndGetUserRoleType(username, password);

        if (message.equals("You are not registered yet!!")) {
            System.out.println(message);
        } else {
            System.out.println("You are successfully logged in as " + message);
        }
    }
}
