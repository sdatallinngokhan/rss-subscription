package service;

import model.Feed;

import java.util.List;

public interface RssToStringConverter {

    List<String> convert(List<Feed> feedList);
}
