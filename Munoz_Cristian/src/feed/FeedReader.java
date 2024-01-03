package feed;

import httpRequest.httpRequester;
import parser.RedditParser;
import parser.RssParser;
import parser.SubscriptionParser;
import subscription.SingleSubscription;
import subscription.Subscription;
import namedEntity.NamedEntity;
import namedEntity.heuristic.*;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import scala.Tuple2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class FeedReader implements Serializable {

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
                    
                    String xmlString = req.getFeed(subscription.getFeedToRequest(j)); 
                    Feed xmlFeed = rssParser.setFeed(xmlString);
                    System.out.println("\n---" + subscription.getUlrParams(j) + "---\n");
                    xmlFeed.prettyPrint();
                    
                } else if(subscription.getUrlType().equals("reddit")){
                    String redditString = req.getFeed(subscription.getFeedToRequest(j));
                    Feed redditFeed = redditParser.setFeed(redditString);
                    redditFeed.prettyPrint();
                }
            }	
        }
    }

    static public void getNamedEntities(){
        setObjects();
        QuickHeuristic heuristic = new QuickHeuristic();
        List<Article> data = new ArrayList<Article>();

        // Iteramos por la lista de subscripsiones
        for(int i = 0 ; i < subscriptions.getSubListSize() ; i++){
            
            // Obtenemos la i-esima subscripcion
            SingleSubscription subscription = subscriptions.getSingleSubscription(i);

            // Iteramos sobre la lista de feeds
            for(int j = 0 ; j < subscription.getUlrParamsSize() ; j++){

                // Si el tipo de url es rss, parseamos con RssParser, 
                // si es de tipo reddit, parseamos con RedditParser
                if(subscription.getUrlType().equals("rss")){
                    
                    String xmlString = req.getFeed(subscription.getFeedToRequest(j)); 
                    Feed xmlFeed = rssParser.setFeed(xmlString);

                    // Agregamos el los articulos a una lista
                    data.addAll(xmlFeed.getArticleList());
                    
                } else if(subscription.getUrlType().equals("reddit")){
                    String redditString = req.getFeed(subscription.getFeedToRequest(j));
                    Feed redditFeed = redditParser.setFeed(redditString);
                    
                    data.addAll(redditFeed.getArticleList());
                }
            }
        }

        // Creamos un SparkConf object y seteamos (local[4] para que use 4 cores)
        SparkConf conf = new SparkConf()
        .setAppName("Name Entity Count")
        .setMaster("local[4]");

        // Creamos un JavaSparkContext object
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Para que nada mas salgan Error messages de Spark
        sc.setLogLevel("Error");

        // Creamos un RDD de articulos
        JavaRDD<Article> rddArt = sc.parallelize(data);

        // Creamos un RDD  de articulos con los namedEntities computados
        JavaRDD<Article> rddArtCompute = rddArt
                            .map(art -> Article.computeNamedEntities(art, heuristic));
        
        // Creamos un RDD con todos los NamedEntities
        JavaRDD<NamedEntity> rddNamesEntities = rddArtCompute
                                .flatMap(art -> art.getListNamedEntities().iterator());

        // Creamos un RDD con todos los nombres de los NamedEntities
        JavaRDD<String> rddNames = rddNamesEntities.map(a -> a.getName());

        // Creamos un RDD con todas las categorias de los NamedEntities
        JavaRDD<String> rddCategories = rddNamesEntities.map(a -> a.getCategory());

        // Creamos un RDD con tuplas de Nombre y un contador
        JavaPairRDD<String, Integer> rddNamesCounts = rddNames
                                        .mapToPair(n -> new Tuple2<>(n, 1))
                                        .reduceByKey((count1, count2) -> count1 + count2);

        // Creamos un RDD con tuplas de Categoria y un contador
        JavaPairRDD<String, Integer> rddCateogryCounts = rddCategories
                                        .mapToPair(c -> new Tuple2<>(c, 1))
                                        .reduceByKey((count1, count2) -> count1 + count2);

        // Imprimimos las ocurrencias de cada nameEntity y de cada categoria
        rddNamesCounts.foreach(t -> System.out.println(t));
        rddCateogryCounts.foreach(t -> System.out.println(t));
                              
        sc.stop();
        sc.close();
    }
}
