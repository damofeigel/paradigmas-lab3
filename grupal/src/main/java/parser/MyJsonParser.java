package parser;

import java.time.Instant;
import java.util.Date;

import feed.Article;
import feed.Feed;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyJsonParser extends GeneralParser {

    private JsonNode node;

    public Feed parse(String jsonString, String type) throws IOException, JsonParseException {

        ObjectMapper obj = new ObjectMapper();

        JsonFactory factory = obj.getFactory();

        JsonParser parser = factory.createParser(jsonString);

        this.node = obj.readTree(parser);

        if (type.equals("reddit")) {
            return parseRedditFeed();
        } else {
            return null;
        }
    }

    private Feed parseRedditFeed() {

        JsonNode root = this.node.get("data").get("children");

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
