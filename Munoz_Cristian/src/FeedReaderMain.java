import feed.FeedReader;

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

		if (args.length == 0) {
			FeedReader.getFeeds();
		} else if (args.length == 1){
			FeedReader.getNamedEntities();
		}
	}
}
