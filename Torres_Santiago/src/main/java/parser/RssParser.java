package parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jdom2.Document;
import org.jdom2.Element;

import feed.Article;
import feed.Feed;

public class RssParser extends GeneralParser {

    public Feed parseNYTFeed(Document doc) throws ParseException {

        Element root = doc.getRootElement().getChild("channel");

        Feed feedNYT = new Feed(root.getChildText("title").substring(6));

        List<Element> itemList = root.getChildren("item");

        for (Element item : itemList) {

            String title = item.getChildText("title");

            String text = item.getChildText("description");

            Date date = formatDate(item.getChildText("pubDate"));

            String link = item.getChildText("link");

            Article article = new Article(title, text, date, link);
            feedNYT.addArticle(article);
        }

        return feedNYT;
    }

    private static Date formatDate(String dateString) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);

        Date date = formatter.parse(dateString.substring(0, 24));

        return date;
    }
}
