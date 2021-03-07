package zaesora.madeshiro.parser;

/**
 * Class of jMdParser created by maᴅeliƒe Æský on 03/05/2016
 */
public interface Parser<T> {

    /**
     * Parse a source according to the parser language support.
     *
     * @param str The source to parse
     * @return The token type of the parser
     * @throws ParseException If an error occurred when trying to parse the payload
     */
    <E extends T> E parse(String str) throws ParseException;

    /**
     * Gets the current language supported by the parser.
     * @return the language of this parser
     */
    AvailableLanguage getLanguage();

    /**
     *
     */
    enum AvailableLanguage {
        JSON
    }
}