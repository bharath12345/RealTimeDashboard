package http_parser.lolevel;

import http_parser.HTTPParserUrl;
import org.junit.Test;

import static http_parser.lolevel.Util.*;

public class TestParseUrl {

    public void test(int i) {
        HTTPParserUrl u = new HTTPParserUrl();
        HTTPParser p = new HTTPParser();
        Url test = Url.URL_TESTS[i];
//    System.out.println(":: " + testParseUrl.name);
        int rv = p.parse_url(Util.buffer(test.url), test.is_connect, u);
        UnitTest ut = new UnitTest();
        ut.check_equals(rv, test.rv);
        if (test.rv == 0) {
            ut.check_equals(u, test.u);
        }

    }

    @Test
    public void testParseUrl() {
        //p(ParseUrl.class);

        for (int i = 0; i < Url.URL_TESTS.length; i++) {
            test(i);
        }
    }

    static void usage() {
        p("usage: [jre] http_parser.lolevel.TestParseUrl [i]");
        p("             i : optional testParseUrl case id");
        p("---------------------------------------------");
        p("Test Cases:");
        for (int i = 0; i != Url.URL_TESTS.length; ++i) {
            p(" " + i + ": " + Url.URL_TESTS[i].name);
        }
    }

    /*public static void main(String[] args) {
        if (0 == args.length) {
            testParseUrl();
        } else {
            try {
                int i = Integer.parseInt(args[0]);
                testHeaderOverflowError(i);
            } catch (Throwable t) {
                t.printStackTrace();
                usage();
            }

        }
    }*/
}
