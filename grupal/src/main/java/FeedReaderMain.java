import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import handler.FeedReaderHandler;
import namedEntity.heuristic.Heuristic;
import namedEntity.heuristic.QuickHeuristic;
import subscription.Subscription;

public class FeedReaderMain {

	static final String PATH_TO_SUBSCRIPTIONS = "src/main/resources/subscriptions.json";

	static private final SparkConf SPARK_CONF = new SparkConf()
			.setAppName("FeedReader")
			.setMaster("local[*]");

	public static void main(String[] args) {

		try (final var sparkContext = new JavaSparkContext(SPARK_CONF)) {

			sparkContext.setLogLevel("ERROR");

			Heuristic heuristic = new QuickHeuristic();
			Subscription subscriptions = FeedReaderHandler.getSubscriptions(PATH_TO_SUBSCRIPTIONS);

			System.out.println("\nObteniendo feeds...\n");

			final var feedRDD = sparkContext.parallelize(FeedReaderHandler.getFeedListFromSubscriptions(subscriptions));

			final var articleRDD = feedRDD.flatMap(list -> list.getArticleList().iterator());

			FeedReaderHandler.handleQuery(feedRDD, articleRDD, heuristic);

		}
	}
}
