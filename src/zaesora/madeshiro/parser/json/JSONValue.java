package zaesora.madeshiro.parser.json;

import zaesora.madeshiro.parser.ParseException;

import java.io.IOException;
import java.io.Writer;

/**
 * Class of jMdParser in package zaesora.madeshiro.parser.json
 *
 * @author MađeShirő ƵÆsora
 * @since jMdParser 1.0
 */
public interface JSONValue {

    void addJValue(Object value) throws ParseException;
    void addJValue(String name, Object value) throws ParseException;

    /**
     *
     * @param writer The writer where write the json source
     */
    void write(Writer writer) throws IOException;

    /**
     *
     * @param writer The writer where write the JSON source
     */
    void writeForFile(Writer writer) throws IOException;

    <E> E getObject(Object... path);

    void clear();
}
