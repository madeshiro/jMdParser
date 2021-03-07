package zaesora.madeshiro.parser.json;

import zaesora.madeshiro.parser.ParseException;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class of jMdParser in package zaesora.madeshiro.parser.json
 *
 * @author MađeShirő ƵÆsora
 * @since jMdParser 1.0
 */
public class JSONObject extends HashMap<String, Object> implements JSONValue {
    private ArrayList<String> organizedKeys = new ArrayList<String>();

    public JSONObject() {
        super();
    }

    public JSONObject(HashMap<String, Object> map) {
        super(map);
        organizedKeys.addAll(map.keySet());
    }

    @Override
    public void clear() {
        organizedKeys.clear();
        super.clear();
    }

    @Override
    public Object remove(Object key) {
        organizedKeys.remove(key.toString());
        return super.remove(key);
    }

    @Override
    public void addJValue(Object value) throws ParseException {
        throw new ParseException("Method unavailable for JSONObject");
    }

    @Override
    public void addJValue(String name, Object value) {
        this.put(name, value);
    }

    @Override
    public Object put(String key, Object value) {
        if (!organizedKeys.contains(key))
            organizedKeys.add(key);
        return super.put(key, value);
    }

    /**
     * @param writer The writer where write the json source
     */
    @Override
    public void write(Writer writer) throws IOException {
        writer.write('{');
        for (int i = 0; i < organizedKeys.size(); i++) {
            writer.write("\"" + transformString(organizedKeys.get(i)) + "\": ");
            writeValue(writer, organizedKeys.get(i), false);
            writer.write(i != organizedKeys.size()-1 ? ", " : "}");
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
        writer.write("{\n");
        for (int i = 0; i < organizedKeys.size(); i++) {
            tab(writer);
            writer.write("\"" + transformString(organizedKeys.get(i)) + "\": ");
            writeValue(writer, organizedKeys.get(i), true);
            writer.write(i != organizedKeys.size()-1 ? ",\n" : "\n");
        }
        _tab--;
        tab(writer);
        writer.write('}');
        _tab++;
    }

    protected static Object[] rebuildPath(Object... path) {
        Object[] nPath = new Object[path.length-1];
        System.arraycopy(path, 1, nPath, 0, path.length - 1);
        return nPath;
    }

    @Override
    public <E> E getObject(Object... path) {
        Object o = get(path[0]);

        if (path.length == 1)
            return (E) o;
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

    private void writeValue(Writer writer, String key, boolean forFile) throws IOException {
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

    @SuppressWarnings("StringConcatenationInLoop")
    protected static String transformString(String o) {
        String build = "";
        for (char c : o.toCharArray()) {
            switch (c) {
                case '\n':
                    build += "\\n";
                    break;
                case '\t':
                    build += "\\t";
                    break;
                case '\b':
                    build += "\\b";
                    break;
                case '\\':
                    build += "\\\\";
                    break;
                case '\f':
                    build += "\\f";
                    break;
                case '"':
                    build += "\\\"";
                    break;
                default:
                    build += c;
                    break;
            }
        }
        return build;
    }
}
