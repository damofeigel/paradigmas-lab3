package parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import java.io.StringReader;
import java.io.IOException;

import javax.xml.XMLConstants;

import feed.Article;
import feed.Feed;

import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class RssParser extends GeneralParser {

    private Document doc;

    public Feed parse(String xmlString, String type) throws IOException, JDOMException, ParseException {
        SAXBuilder sax = new SAXBuilder();

        sax.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        sax.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        this.doc = sax.build(new StringReader(xmlString));

        if (type.equals("nyt")) {
            return parseNYTFeed();
        } else {
            return null;
        }
    }

    private Feed parseNYTFeed() throws ParseException {

        Element root = this.doc.getRootElement().getChild("channel");

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
