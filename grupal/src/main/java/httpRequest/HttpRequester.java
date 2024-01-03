package httpRequest;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpRequester {

	public String getFeed(String urlFeed) throws IOException {

		OkHttpClient client = new OkHttpClient();

		Request req = new Request.Builder()
				.url(urlFeed)
				.build();

		Call call = client.newCall(req);
		Response res = call.execute();
		return res.body().string();
	}
}
