import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import feed.FeedReader;
import namedEntity.heuristic.QuickHeuristic;

public class FeedReaderMain {

	private static void printHelp(){
		System.out.println("Please, call this program in correct way: FeedReader [-ne]");
	}
	
	public static void main(String[] args) {
		System.out.println("************* FeedReader version 1.0 *************");
		if (args.length >= 2) {
			printHelp();
			return;
		} 	

		// Declarar la heuristica deseada
		QuickHeuristic qh = new QuickHeuristic();

		if (args.length == 0) {
			FeedReader.getNamedEntities(qh);
			//FeedReader.getFeeds();
		} else if (args.length == 1 && args[0].equals("-ne")){
			FeedReader.getNamedEntities(qh);	
		} else if (args.length == 1 && args[0].equals("-se")){
			List<String> keywords = new ArrayList<String>();
			try (Scanner scanner = new Scanner(System.in)) {
				System.out.println("Ingrese las palabras clave separadas por espacios: ");
				String readString = scanner.nextLine();
				keywords = Arrays.asList(readString.split(" "));
			}
			System.out.println(keywords);

			FeedReader.searchByKeyWords(keywords);

		}
	}
}
