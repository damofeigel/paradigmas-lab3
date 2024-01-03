package parser;

import org.w3c.dom.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import feed.Article;
import feed.Feed;

/* Esta clase implementa el parser de feed de tipo rss (xml)
 * https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm 
 * */

public class RssParser extends GeneralParser {

    private Document XMLDocument;

    protected void parse(String xmlString){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    
            DocumentBuilder builder = dbf.newDocumentBuilder();
    
            this.XMLDocument = builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public Feed setFeed(String xmlString){
        try{
            parse(xmlString);
            Document doc = this.XMLDocument;

            NodeList titleList = doc.getElementsByTagName("title");
            NodeList textList = doc.getElementsByTagName("media:description");
            NodeList DateList = doc.getElementsByTagName("pubDate");
            NodeList linkList = doc.getElementsByTagName("link");

            String feedName = titleList
                    .item(0)
                    .getTextContent()
                    .replace("NYT > ", "");
            Feed feed = new Feed(feedName);
            
            for(int i=0; i < textList.getLength(); i++){
                Node titleNode = titleList.item(i+2);
                String title = titleNode.getTextContent();
                
                Node textNode = textList.item(i);
                String text = textNode.getTextContent();

                Node dateNode = DateList.item(i+1);
                Date publicationDate = parseDate(dateNode.getTextContent());

                Node linkNode = linkList.item(i+2);
                String link = linkNode.getTextContent();
                
                Article article = new Article(title, text, publicationDate, link);
                feed.addArticle(article);
            }

            return feed;
        } catch(DOMException e){
            throw new RuntimeException(e);
        }
    }

    private Date parseDate (String dateString){

        String formattedDate = dateString
                    .substring(5, 25)
                    .replace(" ", "/");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy/HH:mm:ss");   
        
        try {
            Date date = sdf.parse(formattedDate);
            return date;
        } catch (ParseException e) {
            System.out.println(e);
            return null;
        }
    }
}
