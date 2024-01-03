package handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;

import feed.Article;
import feed.Feed;
import httpRequest.HttpRequester;
import jsonFileReader.JsonFileReader;
import namedEntity.heuristic.Heuristic;
import parser.MyJsonParser;
import parser.RssParser;
import parser.SubscriptionParser;
import scala.Tuple2;
import subscription.SingleSubscription;
import subscription.Subscription;

/**
 * Esta clase se encarga de manejar peticiones del usuario
 */
public class FeedReaderHandler {

    static final List<Tuple2<String, String>> FEED_LIST = new ArrayList<Tuple2<String, String>>(Arrays.asList(
            new Tuple2<String, String>("Business", "New York Times"),
            new Tuple2<String, String>("Technology", "New York Times"),
            new Tuple2<String, String>("Marketing", "Reddit"),
            new Tuple2<String, String>("Sales", "Reddit"),
            new Tuple2<String, String>("Entrepreneur", "Reddit"),
            new Tuple2<String, String>("Startups", "Reddit")));

    /**
     * Metodo principal para el manejo de pedidos del usuario
     *
     * @param feedRDD    - RDD que contiene todos los feeds
     * @param articleRDD - RDD que contiene todos los articulos
     * @param heuristic  - Heuristica seleccionada
     */
    public static void handleQuery(JavaRDD<Feed> feedRDD, JavaRDD<Article> articleRDD, Heuristic heuristic) {

        boolean isRunning = true;
        Scanner scan = new Scanner(System.in);

        while (isRunning) {
            System.out.println("************* FeedReader version 1.0 *************");

            System.out.println("Bienvenido! Que accion desea realizar?\n" +
                    "f - Leer un feed especifico.\n" +
                    "s - Buscar ariculos por palabras clave.\n" +
                    "n - Mostrar numero de ocurrecias de entidades nombradas\n" +
                    "q - Salir del programa.\n");

            String input = scan.nextLine();

            switch (input) {
                case "f":

                    handleShowFeed(feedRDD, scan);

                    System.out.println("\nPresione ENTER para volver al menu principal\n");
                    scan.nextLine();

                    break;
                case "s":

                    handleKeyWordSearch(articleRDD, scan);

                    System.out.println("\nPresione ENTER para volver al menu principal\n");
                    scan.nextLine();

                    break;
                case "n":

                    handleNamedEntityCounting(articleRDD, scan, heuristic);

                    System.out.println("\nPresione ENTER para volver al menu principal\n");
                    scan.nextLine();

                    break;
                case "q":
                    handleQuit(scan);
                    isRunning = false;
                    break;
                default:
                    System.out.println("\nOpcion invalida, intente de nuevo.\n");
                    break;
            }
        }
    }

    private static void handleShowFeed(JavaRDD<Feed> feedRDD, Scanner scan) {

        System.out.println("Elija un feed para mostrar:");

        FEED_LIST.forEach(tuple -> {
            System.out.println(tuple._1 + " - " + tuple._2);
        });

        String chosenFeed = scan.nextLine();

        System.out.println("\nBuscando feed seleccionado...\n");

        List<String> feedNames = new ArrayList<String>();

        FEED_LIST.forEach(tuple -> {
            feedNames.add(tuple._1.toLowerCase());
        });

        if (!feedNames.contains(chosenFeed.toLowerCase())) {
            System.out.println("No existe feed llamado \"" + chosenFeed + "\" en nuestra base de datos");
            return;
        }

        feedRDD.foreach(feed -> {
            if (feed.getSiteName().trim().toLowerCase().equals(chosenFeed.trim().toLowerCase())) {
                feed.prettyPrint();
            }
        });
    }

    private static void handleKeyWordSearch(JavaRDD<Article> articleRDD, Scanner scan) {

        System.out.println("Escriba la palabra clave: \n");

        String keyword = scan.nextLine().toLowerCase();

        System.out.println("\n Buscando articulos...\n");

        final var filteredArticlesRDD = articleRDD
                .mapToPair(article -> new Tuple2<>(StringUtils.countMatches(article.getText(), keyword), article))
                .groupByKey()
                .filter(tuple -> tuple._1 != 0)
                .sortByKey(true);

        filteredArticlesRDD.foreach(tuple -> tuple._2.forEach(article -> article.prettyPrint()));
    }

    private static void handleNamedEntityCounting(JavaRDD<Article> articleRDD, Scanner scan, Heuristic heuristic) {

        final var namedEntitiesRDD = articleRDD.flatMap(article -> {
            return Article
                    .computeNamedEntities(article, heuristic)
                    .getListNamedEntities()
                    .iterator();
        });

        final var neNamesRDD = namedEntitiesRDD.map(entity -> {
            return entity.getName();
        });

        final var neCategoriesRDD = namedEntitiesRDD.map(entity -> {
            return entity.getCategory();
        });

        final var neNameCountsRDD = neNamesRDD
                .mapToPair(name -> new Tuple2<>(name, 1))
                .reduceByKey(Integer::sum);

        final var neCategoryCountsRDD = neCategoriesRDD
                .mapToPair(category -> new Tuple2<>(category, 1))
                .reduceByKey(Integer::sum);

        System.out.println("\nEntidades Nombradas encontradas:\n");

        neNameCountsRDD.foreach(tuple -> {
            System.out.println(tuple._1() + " :: " + tuple._2());
        });

        System.out.println("\nCategorias encontradas:\n");

        neCategoryCountsRDD.foreach(tuple -> {
            System.out.println(tuple._1() + " :: " + tuple._2());
        });

    }

    private static void handleQuit(Scanner scan) {
        System.out.println("\nSaliendo del programa...\n");
        scan.close();
    }

    /**
     * Este metodo se encarga de cargar las subscripciones de un archivo
     * en un objecto de la clase subscriptions
     * 
     * @param path - Path del archivo con las subscripciones
     * @return Objeto subscriptions con todas las subscripciones
     */
    public static Subscription getSubscriptions(String path) {

        JsonFileReader reader = new JsonFileReader();
        SubscriptionParser parser = new SubscriptionParser();

        try {
            Subscription subscriptions = parser
                    .setSubscription(reader.ReadJSONFile(path));

            return subscriptions;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Este metodo se encarga de obtener los feeds de las subscripciones
     * 
     * @param subscriptions - Objeto subscriptions con las subscripciones
     * @return Lista con todos los feeds
     */
    public static List<Feed> getFeedListFromSubscriptions(Subscription subscriptions) {

        HttpRequester requester = new HttpRequester();
        MyJsonParser jsonParser = new MyJsonParser();
        RssParser rssParser = new RssParser();
        List<Feed> feedList = new ArrayList<Feed>();

        try {
            for (SingleSubscription sub : subscriptions.getSubscriptionsList()) {

                for (int i = 0; i < sub.getUrlParams().size(); i++) {

                    if (sub.getUrlType().equals("nyt")) {
                        Feed feed = rssParser.parse(
                                requester.getFeed(sub.getFeedToRequest(i)),
                                sub.getUrlType());
                        feedList.add(feed);
                    } else if (sub.getUrlType().equals("reddit")) {
                        Feed feed = jsonParser.parse(
                                requester.getFeed(sub.getFeedToRequest(i)),
                                sub.getUrlType());
                        feedList.add(feed);
                    }
                }
            }
            return feedList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
