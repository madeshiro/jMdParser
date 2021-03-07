package zaesora.madeshiro.parser.json;

import zaesora.madeshiro.parser.ParseException;
import zaesora.madeshiro.parser.ParserReader;

import java.io.IOException;
import java.io.Reader;

/**
 * Class of jMdParser in package zaesora.madeshiro.parser.json
 *
 * @author MađeShirő ƵÆsora
 * @since jMdParser 1.0
 */
public class JSONReader extends ParserReader {

    private JSONValue value;

    public JSONReader(String src) {
        super(src);
    }

    public JSONReader(Reader reader) throws IOException {
        super(reader);
    }

    /* --- parsing methods --- */

    private void parseObject(JSONObject object) throws ParseException {
        String name;
        while (ignoreCharacters(spacingChars) != '}') {
            switch (current()) {
                case '"':
                    next();
                    name = buildStringUntil('"');
                    if (ignoreCharacters(spacingChars) != ':')
                        throw new ParseException("Expected ':' character at " + cursor);

                    ignoreCharacters(spacingChars);
                    object.addJValue(name,parseValue());
                    if (ignoreCharacters(spacingChars) != ',' && current() != '}')
                        throw new ParseException("Unexpected character at " + cursor);
                    else if (current() == '}')
                        return;
                    break;
                default:
                    throw new ParseException("Expecting '\"' character at " + cursor);
            }
        }
    }

    private void parseArray(JSONArray array) throws ParseException {
        while (ignoreCharacters(spacingChars) != ']') {
            array.addJValue(parseValue());
            if (ignoreCharacters(spacingChars) != ',' && current() != ']')
                throw new ParseException("Unexpected character at " + cursor);
            else if (current() == ']')
                break;
        }
    }

    private Object parseValue() throws ParseException {
        char c = current();

        if ((c >= '0' && c <= '9') || c == '-')
            return parseNumber();
        else {
            c = Character.toLowerCase(c);
            switch (c) {
                case '{':
                    JSONObject object = new JSONObject();
                    parseObject(object);
                    return object;
                case '[':
                    JSONArray array = new JSONArray();
                    parseArray(array);
                    return array;
                case 't': case 'f': case 'n':
                    return parseOthers();
                case '"':
                    return parseString();
                default:
                    throw new ParseException("Invalid value format at " + cursor);
            }
        }
    }

    private String parseString() throws ParseException {
        String str = "";
        while (next() != '"' || getPrevious() == '\\') {
            if (CurrentMatches('\r', '\n'))
                throw new ParseException("Unexpected character at " + cursor);

            str += current();
        }
        return transformStrings(str);
    }

    private Object parseNumber() throws ParseException {

        boolean negative = current() == '-', isDouble = false, ePowerNegative = false;

        String number = "",ePower = "";

        if (negative) next();

        if (current() != '0') {
            while (current() >= '0' && current() <= '9') {
                number += current();
                next();
            }
            previous();
            if (number.length() == 0)
                throw new ParseException("Invalid number format at " + cursor);
        } else if (current() == '0') {
            number+='0';
        }

        if (next() == '.') {
            number+='.';
            isDouble = true;

            int count = 0;
            while (next() >= '0' && current() <= '9') {
                number += current();
                count++;
            }
            if (count == 0)
                throw new ParseException("Invalid number format at " + cursor);
        }

        Object x = (negative ? -1 : 1) * (isDouble ? Double.parseDouble(number) : Long.parseLong(number));

        if (Character.toLowerCase(current()) == 'e') {
            switch (next()) {
                case '-':
                    ePowerNegative = true;
                case '+':
                    next();
                    break;
            }

            while (current() >= '0' && current() <= '9') {
                ePower += current();
                next();
            }
            if (ePower.length() == 0)
                throw new ParseException("Invalid number format at " + cursor);

            x = ((Double) x).doubleValue() * Math.pow(10, (ePowerNegative ? -1 : 1) * Long.parseLong(ePower));
        } else previous();

        if (isDouble || ePowerNegative)
            return (Double) x;
        else
            return (Long) ((Double)x).longValue();
    }

    // NULL, TRUE, FALSE
    private Object parseOthers() throws ParseException {
        String obj = "";
        switch (current()) {
            case 't':
                obj = getSource().substring(cursor, cursor+4); cursor+=3;
                if (obj.equalsIgnoreCase("true"))
                    return true;
                break;
            case 'f':
                obj = getSource().substring(cursor, cursor+5); cursor+=4;
                if (obj.equalsIgnoreCase("false"))
                    return false;
                break;
            case 'n':
                obj = getSource().substring(cursor, cursor+4); cursor+=3;
                if (obj.equalsIgnoreCase("null"))
                    return null;
                break;
        }
        throw new ParseException("Invalid value parsed at " + cursor);
    }

    @Override
    public Object parse() throws ParseException {
        try {
            cursor = -1;
            switch (ignoreCharacters(spacingChars)) {
                case '{':
                    value = new JSONObject();
                    parseObject((JSONObject) value);
                    break;
                case '[':
                    value = new JSONArray();
                    parseArray((JSONArray) value);
                    break;
                default:
                    throw new ParseException("Missing declaration-type character at the beginning");
            }
        } catch (ParseException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParseException(e.getMessage(), e.getCause());
        }

        return value;
    }

    /* --- transform methods --- */
    private String transformStrings(String str) throws ParseException {
        String build = "";
        for (int i = 0; i < str.length(); i++) {
            char c;
            switch ((c=str.charAt(i))) {
                case '\\':
                    switch (str.charAt(i++)) {
                        case 'n':
                            c = '\n';
                            break;
                        case 't':
                            c = '\t';
                            break;
                        case 'b':
                            c = '\b';
                            break;
                        case 'f':
                            c = '\f';
                            break;
                        case '\\':
                            c = '\\';
                            break;
                        case 'u':
                            String hex = str.substring(cursor+1, cursor+4);
                            c = (char) Long.parseLong(str,16);
                            break;
                        case '"':
                            c = '"';
                            break;
                        default:
                            throw new ParseException("Unknown Control char at " + cursor);

                    }
                default:
                    build += c;
            }
        }

        return build;
    }
}
