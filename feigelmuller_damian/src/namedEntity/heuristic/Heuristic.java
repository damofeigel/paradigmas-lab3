package namedEntity.heuristic;

import namedEntity.*;

import java.util.Map;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Heuristic implements Serializable{
	
		private static Map<String, String> categoryMap = Map.ofEntries(
			Map.entry("Microsofts", "organization"), Map.entry("Apple", "organization"),
			Map.entry("Google", "organization"), Map.entry("Musk", "surname"),
			Map.entry("Biden", "surname"), Map.entry("Trump", "surname"),
			Map.entry("Messi", "surname"), Map.entry("Pichai", "surname"),
			Map.entry("Tucker", "surname"), Map.entry("Carlson", "name"),
			Map.entry("Federer", "surname"), Map.entry("USA", "country"),
			Map.entry("Russia", "country"), Map.entry("Taiwan", "country"),
			Map.entry("Taipei", "city"), Map.entry("233 Nashe Avenue", "adress"),
			Map.entry("Mr.", "title"), Map.entry("Elon", "name"),
			Map.entry("Discord", "technology"),Map.entry("Ms. Pattiz", "name")
		);

		private static Map<String, String> themeMap = Map.ofEntries(
			Map.entry("Microsoft", "other"), Map.entry("Apple", "other"),
			Map.entry("Google", "other"), Map.entry("Musk", "other"),
			Map.entry("Biden", "international"), Map.entry("Trump", "international"),
			Map.entry("Messi", "football"), Map.entry("Pichai", "other"),
			Map.entry("Tucker", "national"), Map.entry("Carlson", "national"),
			Map.entry("Federer", "Tennis"), Map.entry("USA", "international"),
			Map.entry("Russia", "international"), Map.entry("Taiwan", "international"),
			Map.entry("Taipei", "national"), Map.entry("Mr.", "other"),
			Map.entry("Elon", "other"),
			Map.entry("Emma Tucker ", "foobar")
			);
	
	public String getCategory(String entity){
		return categoryMap.get(entity);
	}

	public String getTheme(String entity){
		return themeMap.get(entity);
	}

	public List<NamedEntity> getNamedEntitiesList(Heuristic h, List<String> candidates) {
		
		List<NamedEntity> namedEntities = new ArrayList<>();
		List<String> findWords = new ArrayList<String>();
		NamedEntityList namedEntityList = new NamedEntityList();

		for (int i=0; i < candidates.size(); i++){
			String str = candidates.get(i).trim();

			for(String word : str.split(" ")) {
				word = word.trim();

				if (h.isEntity(word)){

					if (findWords.contains(word)) {
						for (NamedEntity n : namedEntities){
					
							if (n.getName().compareTo(word) == 0){
								n.incFrequency();
							}
						} 

					} else {

						String category = getCategory(word);
						String theme = getTheme(word);

						if(category == null || theme == null) {
							continue;
						}
	
						switch(category){
							case "country":
								switch(theme){
									case "international":
										namedEntities.add(namedEntityList.new 
											CountryInternational(word, category, 1)); 
										findWords.add(word);
										break;
									default:
										namedEntities.add(namedEntityList.new 
											CountryCultureOthers(word, category, 1));
										findWords.add(word);
								}
								break;
							case "city":
								switch(theme){
									case "national": 
										namedEntities.add(namedEntityList.new 
											CityNational(word, category, 1)); 
										findWords.add(word);
										break;
									default:
										namedEntities.add(namedEntityList.new 
											CityCultureOthers(word, category, 1)); 
										findWords.add(word);
								}
								break;
							case "address":
								namedEntities.add(namedEntityList.new 
									AddressOthers(word, category, 1)); 
								findWords.add(word);
								break;
							
							case "surname":
								switch(theme){
									case "national":
										namedEntities.add(namedEntityList.new 
											SurnameNational(word, category, 1)); 
										findWords.add(word);
										break;
									case "international":
										namedEntities.add(namedEntityList.new 
											SurnameInternational(word, category, 1)); 
										findWords.add(word);
										break;
									case "football":
										namedEntities.add(namedEntityList.new 
											SurnameFootball(word, category, 1)); 
										findWords.add(word);
										break;
									case "basketball":
										namedEntities.add(namedEntityList.new 
											SurnameBasketball(word, category, 1)); 
										findWords.add(word);
										break;
									case "tennis":
										namedEntities.add(namedEntityList.new 
											SurnameTennis(word, category, 1)); 
										findWords.add(word);
										break;
								}
								break;
							case "name":
								switch(theme){
									case "national":
										namedEntities.add(namedEntityList.new 
											NameNational(word, category, 1)); 
										findWords.add(word);
										break;
									case "international":
										namedEntities.add(namedEntityList.new 
											NameInternational(word, category, 1)); 
										findWords.add(word);
										break;
									case "football":
										namedEntities.add(namedEntityList.new 
											NameFootball(word, category, 1)); 
										findWords.add(word);
										break;
									case "basketball":
										namedEntities.add(namedEntityList.new 
											NameBasketball(word, category, 1)); 
										findWords.add(word);
										break;
									case "tennis":
										namedEntities.add(namedEntityList.new 
											NameTennis(word, category, 1)); 
										findWords.add(word);
										break;
								}
								break;
							case "title":
								switch(theme){
									case "national":
										namedEntities.add(namedEntityList.new 
											TitleNational(word, category, 1)); 
										findWords.add(word);
										break;
									case "international":
										namedEntities.add(namedEntityList.new 
											TitleInternational(word, category, 1)); 
										findWords.add(word);
										break;
									default:
										namedEntities.add(namedEntityList.new 
											TitleOthers(word, category, 1)); 
										findWords.add(word);
								}
								break;
							case "organization":
								switch(theme){
									case "national":
										namedEntities.add(namedEntityList.new 
											OrganizationNational(word, category, 1)); 
										findWords.add(word);
										break;
									case "international":
										namedEntities.add(namedEntityList.new 
											OrganizationInternational(word, category, 1)); 
										findWords.add(word);
										break;
									default:
										namedEntities.add(namedEntityList.new 
											OrganizationOthers(word, category, 1)); 
										findWords.add(word);
								}
								break;
							case "product":
								namedEntities.add(namedEntityList.new 
									ProductOthers(word, category, 1)); 
								findWords.add(word);
								break;
							case "date":
								namedEntities.add(namedEntityList.new 
									DateOther(word, category, 1)); 
								findWords.add(word);
								break;
							default:
								namedEntities.add(namedEntityList.new 
									CategoryOther(word, category, 1)); 
								findWords.add(word);
						};
					}
				}
			}
		}

		return namedEntities; 
	}
	
	public abstract boolean isEntity(String word);
		
}
