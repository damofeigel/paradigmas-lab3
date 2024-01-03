package feed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*Esta clase modela la lista de articulos de un determinado feed*/
public class Feed implements Serializable {
	String siteName;
	List<Article> articleList;

	public Feed(String siteName) {
		super();
		this.siteName = siteName;
		this.articleList = new ArrayList<Article>();
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	public void addArticle(Article a) {
		this.getArticleList().add(a);
	}

	public Article getArticle(int i) {
		return this.getArticleList().get(i);
	}

	public int getNumberOfArticles() {
		return this.getArticleList().size();
	}

	@Override
	public String toString() {
		return "Feed [siteName=" + getSiteName() + ", articleList=" + getArticleList() + "]";
	}

	public void prettyPrint() {
		for (Article a : this.getArticleList()) {
			a.prettyPrint();
		}

	}
}
