package zaesora.madeshiro.parser;

import java.io.IOException;

/**
 * Class of jMdParser created by maᴅeliƒe Æský on 03/05/2016
 */
public class ParseException extends IOException {

    public ParseException() {
        super();
    }

    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }
}
