package parser;

import java.io.IOException;
import java.text.ParseException;

import feed.Feed;

import org.jdom2.JDOMException;

import com.fasterxml.jackson.core.JsonParseException;

/*Esta clase modela los atributos y metodos comunes a todos los distintos tipos de parser existentes en la aplicacion*/
public abstract class GeneralParser {

    public abstract Feed parse(String str, String type)
            throws IOException, JsonParseException, JDOMException, ParseException;

}
