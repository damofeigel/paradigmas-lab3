package namedEntity;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import feed.Article;

import namedEntity.heuristic.Heuristic;

import scala.Tuple2;

import java.util.List;

// IDEA, pasar lista de documentos, hacer un RDD de ellos, ver que onda, Por ahora lo hago sin filtrar candidatos


public class NameEntityComputer {

	public NameEntityComputer() {

	}

    public static void printNamedEntities(List<Article> allArticles, Heuristic heuristic) {
        // Creamos una sesion de spark
        SparkSession spark = SparkSession.builder()
                .appName("")
                .master("local[*]")
                .getOrCreate();

        // Creamos un contexto de Spark
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
		sc.setLogLevel("ERROR");

		JavaRDD<Article> articleRDD = sc.parallelize(allArticles);
        
		JavaRDD<NamedEntity> namedEntitiesRDD = articleRDD.flatMap(article -> {
            return article
                .computeNamedEntities(article, heuristic)
                .getNamedEntityList()
                .iterator();
        });		

		namedEntitiesRDD.foreach(
			ne -> {System.out.println(ne.toString());}
		);

		JavaRDD<String> names = namedEntitiesRDD
			.mapToPair(ne -> new Tuple2<>(ne.getName().trim(), 1))
			.reduceByKey((i1, i2) -> i1 + i2)
			.map(pair -> "(" + pair._1 + ", " + pair._2 + ")");


		System.out.println("Entidades nombradas: ");
		names.foreach(
			name -> {System.out.println(name);}
		);
		
		System.out.println("Categorias: ");
		JavaRDD<String> categories = namedEntitiesRDD
			.mapToPair(ne -> {
				if (ne.getCategory() != null){
					return new Tuple2<>(ne.getCategory(), 1);
				} else return new Tuple2<>(null, 1);
				})
			.reduceByKey((i1, i2) -> i1 + i2)
			.map(pair -> "(" + pair._1 + ", " + pair._2 + ")");

		categories.foreach(
			category -> {System.out.println(category);}
		);
 
        spark.stop();
        sc.close();
    }

}