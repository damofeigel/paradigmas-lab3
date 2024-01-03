package namedEntity.heuristic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;


// No funcional porque se hace una peticion por cada palabra,
// y al ser demasiadas palabras tira el error 429 por hacer demasiados
// pedidos. Se tendria que cambiar el como es la heuristica general 
// para hacerlo funcional

public class AIHeuristic extends Heuristic {
    public boolean isEntity (String word) {
        try {
            String url = "https://api.openai.com/v1/completions";
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer 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");

            JSONObject data = new JSONObject();
            data.put("model", "text-davinci-003");
            data.put("prompt", "Tell me if the next word is a name entity: " + word +
                            ". Reply only with 'True' or 'False'.");
            data.put("max_tokens", 1000);
            data.put("temperature", 1.0);

            con.setDoOutput(true);
            con.getOutputStream().write(data.toString().getBytes());

            String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                    .reduce((a, b) -> a + b).get();

            String response = new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
            if (response.trim() == "True") {
                return true;
            } else {
                return false;
            }
        }
        catch (Exception e) { 
            
            System.out.println(e);
            e.setStackTrace(null);
            return false;
        }
    }
}