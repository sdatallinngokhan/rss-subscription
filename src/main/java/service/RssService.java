package service;

import com.rometools.rome.feed.synd.SyndFeed;
import model.Feed;

import java.util.List;

public interface RssService {

    SyndFeed readRss(String urlString);
    List<Feed> getFeedList(SyndFeed syndFeed);
}
