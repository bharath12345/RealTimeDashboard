package http_parser.lolevel;

import java.nio.*;

import http_parser.lolevel.Util.*;

import org.junit.Test;
import org.junit.Assert;

import http_parser.lolevel.Util;

public class TestHeaderOverflowError {

    public void test(http_parser.ParserType type) {
        HTTPParser parser = new HTTPParser(type);
        ByteBuffer buf = getBytes(type);

        int numbytes = buf.limit();

        parser.execute(Util.SETTINGS_NULL, buf);

        Util.check(numbytes == buf.position());

        buf = Util.buffer("header-key: header-value\r\n");
        numbytes = buf.limit();
        for (int i = 0; i != 1000; ++i) {
            parser.execute(Util.SETTINGS_NULL, buf);
            Util.check(numbytes == buf.position());

            buf.rewind();

        }
    }

    ByteBuffer getBytes(http_parser.ParserType type) {
        if (http_parser.ParserType.HTTP_BOTH == type) {
            throw new RuntimeException("only HTTP_REQUEST and HTTP_RESPONSE");
        }

        if (http_parser.ParserType.HTTP_REQUEST == type) {
            return Util.buffer("GET / HTTP/1.1\r\n");
        }
        return Util.buffer("HTTP/1.0 200 OK\r\n");
    }

    @Test
    public void testHeaderOverflowError() {
        //p(TestHeaderOverflowError.class);
        test(http_parser.ParserType.HTTP_REQUEST);
        test(http_parser.ParserType.HTTP_RESPONSE);
    }


}
