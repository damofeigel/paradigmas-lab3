package feed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import namedEntity.NamedEntity;
import namedEntity.StringProcesser;
import namedEntity.heuristic.Heuristic;

/*Esta clase modela el contenido de un articulo (ie, un item en el caso del rss feed) */

public class Article implements Serializable {
	private String title;
	private String text;
	private Date publicationDate;
	private String link;

	private List<NamedEntity> namedEntityList = new ArrayList<NamedEntity>();

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

	public NamedEntity getNamedEntity(String namedEntity) {
		for (NamedEntity n : namedEntityList) {
			if (n.getName().compareTo(namedEntity) == 0) {
				return n;
			}
		}
		return null;
	}

	public void addNamedEntity(String namedEntity, String category) {
		this.namedEntityList.add(new NamedEntity(namedEntity, category, 1));
	}

	public List<NamedEntity> getListNamedEntities() {
		return this.namedEntityList;
	}

	static public Article computeNamedEntities(Article art, Heuristic h) {

		List<String> candidates = new ArrayList<String>();
		String text = art.getTitle() + " " + art.getText() + " " + art.getPublicationDate();

		StringProcesser strProc = new StringProcesser(text);
		strProc.processString();
		candidates.addAll(strProc.filterCandidates(strProc.getString()));

		for (String candidate : candidates) {
			if (h.isEntity(candidate)) {
				String category = h.mapEntity(candidate);
				if (category != null) {
					NamedEntity ne = art.getNamedEntity(candidate);
					if (ne == null) {
						art.addNamedEntity(candidate, category);
					} else {
						ne.incFrequency();
					}
				}
			}
		}
		return art;
	}

	public void prettyPrint() {
		System.out.println(
				"**********************************************************************************************");
		System.out.println("Title: " + this.getTitle());
		System.out.println("Publication Date: " + this.getPublicationDate());
		System.out.println("Link: " + this.getLink());
		System.out.println("Text: " + this.getText());
		System.out.println(
				"**********************************************************************************************");

	}

	public static void main(String[] args) {
		Article a = new Article("This Historically Black University Created Its Own Tech Intern Pipeline",
				"A new program at Bowie State connects computing students directly with companies, bypassing an often harsh Silicon Valley vetting process",
				new Date(),
				"https://www.nytimes.com/2023/04/05/technology/bowie-hbcu-tech-intern-pipeline.html");

		a.prettyPrint();
	}

}
