package service;

import model.Feed;

import java.util.ArrayList;
import java.util.List;

public class RssToStringConverterImpl implements RssToStringConverter{

    @Override
    public List<String> convert(List<Feed> feedList) {
        List<String> stringList = new ArrayList<>();
        for (Feed feed : feedList) {
            stringList.add(feed.getTitle());
            stringList.add(feed.getDescription());
            stringList.add(feed.getLink());
            stringList.add("\n");
        }
        return stringList;
    }
}
