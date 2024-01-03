package parser;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.XMLConstants;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/*Esta clase modela los atributos y metodos comunes a todos los distintos tipos de parser existentes en la aplicacion*/
public abstract class GeneralParser {

    public Document parseXML(String xmlString) throws IOException, JDOMException {

        SAXBuilder sax = new SAXBuilder();

        sax.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        sax.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        Document doc = sax.build(new StringReader(xmlString));

        return doc;
    }

    public JsonNode parseJSON(String jsonString) throws IOException, JsonParseException {

        ObjectMapper obj = new ObjectMapper();

        JsonFactory factory = obj.getFactory();

        JsonParser parser = factory.createParser(jsonString);

        JsonNode node = obj.readTree(parser);

        return node;
    }
}
