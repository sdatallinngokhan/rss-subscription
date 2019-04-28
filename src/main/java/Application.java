import com.rometools.rome.feed.synd.SyndFeed;
import model.Feed;
import service.*;

import java.util.List;

public class Application {

    public static final String RSS_URL = "http://feeds.bbci.co.uk/news/world/rss.xml";

    public static void main(String[] args) {
        RssService rssService = new RssServiceImpl();
        SyndFeed syndFeed = rssService.readRss(RSS_URL);
        if (syndFeed == null) {
            System.out.println("No Rss");
            return;
        }
        List<Feed> feedList = rssService.getFeedList(syndFeed);

        RssToStringConverter rssToStringConverter = new RssToStringConverterImpl();
        List<String> stringFeedList = rssToStringConverter.convert(feedList);

        FileService fileService = new FileServiceImpl();
        String pathString = "/Users/gokhanpolat/Developer/advancedCoding-tll5/rssTest.txt";
        fileService.writeFile(pathString, stringFeedList);
    }
}
