package zaesora.madeshiro.parser.json;

import zaesora.madeshiro.parser.ParseException;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

import static zaesora.madeshiro.parser.json.JSONObject.rebuildPath;
import static zaesora.madeshiro.parser.json.JSONObject.transformString;

/**
 * Class of jMdParser in package zaesora.madeshiro.parser.json
 *
 * @author MađeShirő ƵÆsora
 * @since jMdParser 1.0
 */
public class JSONArray extends ArrayList<Object> implements JSONValue {

    public JSONArray() {
        super();
    }

    public JSONArray(Collection<Object> list) {
        super(list);
    }

    @Override
    public void addJValue(Object value) {
        add(value);
    }

    @Override
    public void addJValue(String name, Object value) throws ParseException {
        throw new ParseException("Method unavailable for JSONArray");
    }


    /**
     * @param writer The writer where write the json source
     */
    @Override
    public void write(Writer writer) throws IOException {
        writer.write('[');
        for (int i = 0; i < size(); i++) {
            writeValue(writer, i, false);
            writer.write(i != size()-1 ? ", " : "]");
        }
    }

    int _tab = 1;

    private void tab(Writer writer) throws IOException {
        for (int i = 0; i < _tab; i++)
            writer.write('\t');
    }

    /**
     * @param writer The writer where write the JSON source
     */
    @Override
    public void writeForFile(Writer writer) throws IOException {
        writer.write("[\n");
        for (int i = 0; i < size(); i++) {
            tab(writer);
            writeValue(writer, i, true);
            writer.write(i != size()-1 ? ",\n" : "\n");
        }
        _tab--;
        tab(writer);
        writer.write(']');
        _tab++;
    }

    @Override
    public Object getObject(Object... path) {
        Object o = get((Integer) path[0]);

        if (path.length == 1)
            return o;
        else if (o instanceof JSONValue)
            return ((JSONValue) o).getObject(rebuildPath(path));
        else
            return null;
    }

    private boolean instanceofInteger(Object o) {
        return o instanceof Integer || o instanceof Long;
    }

    private boolean instanceofFloat(Object o) {
        return o instanceof Float || o instanceof Double;
    }

    private void writeValue(Writer writer, int key, boolean forFile) throws IOException {
        Object o = get(key);
        if (o == null) writer.write("null");
        else if (o instanceof JSONValue) {
            if (forFile) {
                if (o instanceof JSONObject)
                    ((JSONObject) o)._tab = _tab + 1;
                else if (o instanceof JSONArray)
                    ((JSONArray) o)._tab = _tab + 1;

                ((JSONValue) o).writeForFile(writer);
            } else
                ((JSONValue) o).write(writer);
        } else if (instanceofInteger(o))
            writer.write(o.toString().replace(".0", ""));
        else if (instanceofFloat(o) || o instanceof Boolean)
            writer.write(o.toString());
        else if (o instanceof String)
            writer.write("\"" + transformString((String) o) + "\"");
        else
            writer.write("\"" + o.toString() + "\"");
    }
}
