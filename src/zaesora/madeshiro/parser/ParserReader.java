package zaesora.madeshiro.parser;

import java.io.IOException;
import java.io.Reader;

/**
 * Class of jMdParser in package zaesora.madeshiro.parser
 *
 * @author MađeShirő ƵÆsora
 * @since jMdParser 1.0
 */
public abstract class ParserReader {
    private String str;
    protected int cursor, undo;

    protected final char[] spacingChars = {' ', '\r', '\t', '\n'};
    protected final char[] controlChars = {'"', '\\', '/', 'b', 'f', 'n', 'r', 't', 'u'};
    protected final char[] specialChars = {'\t', '\r', '\b', '\f', '\n'};

    public ParserReader(String src) {
        this.str = src;
        this.cursor = 0;
    }

    public ParserReader(Reader reader) throws IOException {
        this.setSource(reader);
    }

    public int getCursor() {
        return cursor;
    }

    public void setSource(String src) {
        this.str = src;
    }

    public void setSource(Reader reader) throws IOException {
        str = "";
        cursor = 0;

        int c;
        while ((c = reader.read()) != -1)
            //noinspection StringConcatenationInLoop
            str += (char)c;
    }

    public String getSource() {
        return str;
    }

    public abstract Object parse() throws ParseException;

    protected char getPrevious() {
        return str.charAt(cursor-1);
    }

    protected char getNext() {
        return str.charAt(cursor+1);
    }

    protected char next() {
        cursor++;
        return str.charAt(cursor);
    }

    protected char current() {
        return str.charAt(cursor);
    }

    @SuppressWarnings("UnusedReturnValue")
    protected char previous() {
        cursor--;
        return str.charAt(cursor);
    }

    protected boolean CurrentMatches(char... ignored) {
        for (char c : ignored)
            if (str.charAt(cursor) == c)
                return true;
        return false;
    }

    protected char ignoreCharacters(char... ignored) {
        undo = cursor;

        do {
            cursor++;
        } while (CurrentMatches(ignored));

        return str.charAt(cursor);
    }

    protected char gotoNextChar(char c) {
        do {
            cursor++;
        } while (str.charAt(cursor) != c);

        return str.charAt(cursor);
    }

    protected String buildStringUntil(char... c) {
        String build = "";

        for (; !CurrentMatches(c); cursor++)
            //noinspection StringConcatenationInLoop
            build += str.charAt(cursor);

        return build;
    }

    protected char undo() {
        int _undo = cursor;
        cursor = undo;
        undo = _undo;

        return str.charAt(cursor);
    }
}
