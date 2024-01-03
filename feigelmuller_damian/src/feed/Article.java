package feed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import namedEntity.NamedEntity;
import namedEntity.StringProcesser;
import namedEntity.heuristic.Heuristic;

/*Esta clase modela el contenido de un articulo (ie, un item en el caso del rss feed) */

public class Article implements Serializable{
	private String title;
	private String text;
	private Date publicationDate;
	private String link;
	
	private List<NamedEntity> namedEntityList = new ArrayList<NamedEntity>();
	private List<String> candidates = new ArrayList<String>();
	
	public Article(String title, String text, Date publicationDate, String link) {
		super();
		this.title = title;
		this.text = text;
		this.publicationDate = publicationDate;
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	@Override
	public String toString() {
		return "Article [title=" + title + ", text=" + text + ", publicationDate=" + publicationDate + ", link=" + link
				+ "]";
	}
	
	public List<NamedEntity> getNamedEntityList() {
		return this.namedEntityList;
	}

	public NamedEntity getNamedEntity(String namedEntity){
		for (NamedEntity n: namedEntityList){
			if (n.getName().compareTo(namedEntity) == 0){				
				return n;
			}
		}
		return null;
	}
	
	public Article computeNamedEntities(Article art, Heuristic h){
		String text = this.getTitle() + " " +  this.getText();  
		StringProcesser sp = new StringProcesser(text);
		sp.processString();
		/* 
		String charsToRemove = ".,;:()'!?\n";
		for (char c : charsToRemove.toCharArray()) {
			text = text.replace(String.valueOf(c), "");
		}
		*/
		candidates.addAll(sp.filterCandidates(sp.getString()));
		for (String s: candidates) {
			if (h.isEntity(s)){
				NamedEntity ne = this.getNamedEntity(s);
				if (ne == null) {
					this.namedEntityList.add(new NamedEntity(s.trim(), h.getCategory(s),1));
				}else {
					ne.incFrequency();
				}
			}
		}
		return art;
	}

	public List<String> getCandidates(){
		return this.candidates;
	}

	public void printNamedEntities(){
		System.out.println(this.namedEntityList);
	}

	
	public void prettyPrint() {
		System.out.println("**********************************************************************************************");
		System.out.println("Title: " + this.getTitle());
		System.out.println("Publication Date: " + this.getPublicationDate());
		System.out.println("Link: " + this.getLink());
		System.out.println("Text: " + this.getText());
		System.out.println("**********************************************************************************************");
		
	}
	
	public static void main(String[] args) {
		  Article a = new Article("This Historically Black University Created Its Own Tech Intern Pipeline",
			  "A new program at Bowie State connects computing students directly with companies, bypassing an often harsh Silicon Valley vetting process",
			  new Date(),
			  "https://www.nytimes.com/2023/04/05/technology/bowie-hbcu-tech-intern-pipeline.html"
			  );
		 
		  a.prettyPrint();
	}
	
	
}



