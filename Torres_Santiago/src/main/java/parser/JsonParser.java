package parser;

import java.time.Instant;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;

import feed.Article;
import feed.Feed;

public class JsonParser extends GeneralParser {

    public Feed parseRedditFeed(JsonNode node) {

        JsonNode root = node.get("data").get("children");

        String feedName = root
                .get(0)
                .get("data")
                .get("subreddit")
                .textValue();

        Feed redditFeed = new Feed(feedName);

        for (JsonNode item : root) {

            String title = item.get("data").get("title").textValue();

            String text = item.get("data").get("selftext").textValue();

            Instant instant = Instant.ofEpochSecond(item.get("data").get("created_utc").intValue());
            Date date = Date.from(instant);

            String link = item.get("data").get("url").textValue();

            Article article = new Article(title, text, date, link);
            redditFeed.addArticle(article);
        }

        return redditFeed;
    }
}
