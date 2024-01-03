package namedEntity.heuristic;

import java.io.Serializable;
import java.util.Map;

public abstract class Heuristic implements Serializable {

	private static Map<String, String> categoryMap = Map.ofEntries(
			Map.entry("Microsoft", "Organization"), Map.entry("Apple", "Organization"),
			Map.entry("Google", "Organization"), Map.entry("Musk", "Surname"),
			Map.entry("Biden", "Surname"), Map.entry("Trump", "Surname"),
			Map.entry("Messi", "Surname"), Map.entry("Pichai", "Surname"),
			Map.entry("Tucker", "Surname"), Map.entry("Carlson", "Name"),
			Map.entry("Federer", "Surname"), Map.entry("USA", "Country"),
			Map.entry("Russia", "Country"), Map.entry("Taiwan", "Country"),
			Map.entry("Taipei", "City"), Map.entry("233 Nashe Avenue", "Adress"),
			Map.entry("Mr.", "Title"), Map.entry("Elon", "Name"));

	private static Map<String, String> themeMap = Map.ofEntries(
			Map.entry("Microsoft", "other"), Map.entry("Apple", "other"),
			Map.entry("Google", "other"), Map.entry("Musk", "other"),
			Map.entry("Biden", "international"), Map.entry("Trump", "international"),
			Map.entry("Messi", "football"), Map.entry("Pichai", "other"),
			Map.entry("Tucker", "national"), Map.entry("Carlson", "national"),
			Map.entry("Federer", "Tennis"), Map.entry("USA", "international"),
			Map.entry("Russia", "international"), Map.entry("Taiwan", "international"),
			Map.entry("Taipei", "national"), Map.entry("Mr.", "other"),
			Map.entry("Elon", "other"));

	public String getCategory(String entity) {
		return categoryMap.get(entity);
	}

	public String getTheme(String entity) {
		return themeMap.get(entity);
	}

	public String mapEntity(String candidate) {

		String category = getCategory(candidate);
		String theme = getTheme(candidate);

		if (category == null || theme == null) {
			return null;
		}

		return category;
	}

	public abstract boolean isEntity(String word);

}
