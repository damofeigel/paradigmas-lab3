package feed;

import httpRequest.httpRequester;
import namedEntity.heuristic.*;
import namedEntity.NameEntityComputer;
import parser.RedditParser;
import parser.RssParser;
import parser.SubscriptionParser;
import subscription.SingleSubscription;
import subscription.Subscription;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedReader {

    static private SubscriptionParser subscriptionParser;
    static private httpRequester req;
    static private RssParser rssParser;
    static private RedditParser redditParser;
    static private Subscription subscriptions;

    static private void setObjects(){
		// Instaciamos los objetos
		subscriptionParser = new SubscriptionParser();
		req = new httpRequester();
		rssParser = new RssParser();
		redditParser = new RedditParser();

		// Seteamos los parametros desde subscriptions.json
		subscriptionParser.setParams();

		// Instanciamos el objeto subscriptions
		subscriptions = subscriptionParser.getSubscription();
    } 

    static public void getFeeds(){
        setObjects();
	    // Iteramos por la lista de subscripsiones
        for(int i = 0 ; i < subscriptions.getSubListSize() ; i++){
            
            // Obtenemos la i-esima subscripcion
            SingleSubscription subscription = subscriptions.getSingleSubscription(i);

            // Iteramos sobre la lista de feeds
            for(int j = 0 ; j < subscription.getUlrParamsSize() ; j++){
                
                // Si el tipo de url es rss, parseamos con RssParser, 
                // si es de tipo reddit, parseamos con RedditParser
                if(subscription.getUrlType().equals("rss")){
                    
                    String xmlString = req.getFeedRss(subscription.getFeedToRequest(j)); 
                    Feed xmlFeed = rssParser.setFeed(xmlString);
                    System.out.println("\n---" + subscription.getUlrParams(j) + "---\n");
                    xmlFeed.prettyPrint();
                    
                } else if(subscription.getUrlType().equals("reddit")){
                    String redditString = req.getFeedReedit(subscription.getFeedToRequest(j));
                    Feed redditFeed = redditParser.setFeed(redditString);
                    redditFeed.prettyPrint();
                }
            }	
        }
    }

    static public void getNamedEntities(Heuristic heuristic){
        setObjects();
        List<Article> allArticles = new ArrayList<Article>();
        // Iteramos por la lista de subscripsiones
        for(int i = 0 ; i < subscriptions.getSubListSize() ; i++){
            
            // Obtenemos la i-esima subscripcion
            SingleSubscription subscription = subscriptions.getSingleSubscription(i);

            // Iteramos sobre la lista de feeds
            for(int j = 0 ; j < subscription.getUlrParamsSize() ; j++){
                
                // Si el tipo de url es rss, parseamos con RssParser, 
                // si es de tipo reddit, parseamos con RedditParser
                if(subscription.getUrlType().equals("rss")){
                    
                    String xmlString = req.getFeedRss(subscription.getFeedToRequest(j)); 
                    Feed xmlFeed = rssParser.setFeed(xmlString);
                    allArticles.addAll(xmlFeed.getArticleList());
                    
                } else if(subscription.getUrlType().equals("reddit")){
                    String redditString = req.getFeedReedit(subscription.getFeedToRequest(j));
                    Feed redditFeed = redditParser.setFeed(redditString);

                    allArticles.addAll(redditFeed.getArticleList());	

                }
            }	
        }

        // Splittear y convertir a list!
        NameEntityComputer.printNamedEntities(allArticles, heuristic);
    }

    public static void searchByKeyWords(List<String> keywords){
        setObjects();

        for(int i = 0 ; i < subscriptions.getSubListSize() ; i++){
            // Obtenemos la i-esima subscripcion
            SingleSubscription subscription = subscriptions.getSingleSubscription(i);

            // Iteramos sobre la lista de feeds
            for(int j = 0 ; j < subscription.getUlrParamsSize() ; j++){
                
                // Si el tipo de url es rss, parseamos con RssParser, 
                // si es de tipo reddit, parseamos con RedditParser
                if(subscription.getUrlType().equals("rss")){
                    
                    String xmlString = req.getFeedRss(subscription.getFeedToRequest(j)); 
                    Feed xmlFeed = rssParser.setFeed(xmlString);
                    printKeyWordAndArticle(xmlFeed, keywords);
                    
                } else if(subscription.getUrlType().equals("reddit")){
                    String redditString = req.getFeedReedit(subscription.getFeedToRequest(j));
                    Feed redditFeed = redditParser.setFeed(redditString);
                    printKeyWordAndArticle(redditFeed, keywords);
                }
            }	
        }
    }

    private static void printKeyWordAndArticle(Feed feed, List<String> keywords){

        SparkSession spark = SparkSession.builder()
                    .appName("")
                    .master("local[*]")
                    .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
        sc.setLogLevel("ERROR");

        JavaRDD<Article> articles = sc.parallelize(feed.getArticleList());

        articles.foreach( art -> {
            if (Arrays.asList(art.getText().split(" ")).containsAll(keywords)){
                art.prettyPrint();
            }   
        });
    }
}
