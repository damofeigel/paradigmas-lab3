package jsonFileReader;

import java.io.IOException;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFileReader {
    public JsonNode ReadJSONFile(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readValue(
                Paths.get(path).toFile(),
                JsonNode.class);

        return node;
    }
}
