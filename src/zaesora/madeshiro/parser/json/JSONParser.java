package zaesora.madeshiro.parser.json;

import zaesora.madeshiro.parser.ParseException;
import zaesora.madeshiro.parser.Parser;

/**
 * Class of jMdParser in package zaesora.madeshiro.parser.json
 *
 * @author MađeShirő ƵÆsora
 * @since jMdParser 1.0
 */
public class JSONParser implements Parser<JSONValue> {

    public JSONParser() {
    }

    public <E extends JSONValue> E parse(String str) throws ParseException {
       return (E) new JSONReader(str).parse();

    }

    /**
     * Gets the current language supported by the parser.
     *
     * @return the language of this parser
     */
    @Override
    public AvailableLanguage getLanguage() {
        return AvailableLanguage.JSON;
    }
}
