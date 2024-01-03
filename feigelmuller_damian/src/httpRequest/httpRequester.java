package httpRequest;

import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

/* Esta clase se encarga de realizar efectivamente el pedido de feed al servidor de noticias
 * Leer sobre como hacer una http request en java
 * https://www.baeldung.com/java-http-request
 * */

public class httpRequester {

	public String getFeedRss(String urlFeed) {
		try {
			URI url = new URI(urlFeed);
	
			HttpRequest req = HttpRequest.newBuilder()
				.uri(url)
				.GET()
				.build();
	
			HttpResponse<String> res = HttpClient
				.newBuilder()
				.proxy(ProxySelector.getDefault())
				.build()
				.send(req, BodyHandlers.ofString());
				
			return res.body();
		} catch (Exception e) { 
			throw new RuntimeException(e);
		}
	}
	
	public String getFeedReedit(String urlFeed) {
		try {
			URI url = new URI(urlFeed);
	
			HttpRequest req = HttpRequest.newBuilder()
				.uri(url)
				.GET()
				.build();
	
			HttpResponse<String> res = HttpClient
				.newBuilder()
				.proxy(ProxySelector.getDefault())
				.build()
				.send(req, BodyHandlers.ofString());
				
			return res.body();
		} catch (Exception e) { 
			throw new RuntimeException(e);
		}
	}
}
