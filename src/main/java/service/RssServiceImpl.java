package service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import model.Feed;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RssServiceImpl implements RssService {

    @Override
    public SyndFeed readRss(String urlString) {
        try {
            URL url = new URL(urlString);
            XmlReader xmlReader = new XmlReader(url);
            SyndFeed syndFeed = new SyndFeedInput().build(xmlReader);

            return syndFeed;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Feed> getFeedList(SyndFeed syndFeed) {
        List<Feed> feedList = new ArrayList<>();
        for (SyndEntry syndEntry : syndFeed.getEntries()) {
            Feed feed = new Feed();
            feed.setTitle(syndEntry.getTitle());
            feed.setDescription(syndEntry.getDescription().getValue());
            feed.setLink(syndEntry.getLink());

            feedList.add(feed);
        }

        return feedList;
    }
}
