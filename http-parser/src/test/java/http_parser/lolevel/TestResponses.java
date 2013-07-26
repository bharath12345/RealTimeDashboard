package http_parser.lolevel;

import static http_parser.lolevel.Util.*;

import http_parser.ParserType;
import org.junit.Test;
import org.junit.Assert;

import java.util.LinkedList;
import java.util.List;

public class TestResponses {

    @Test
    public void testResponses() {
        //p(Responses.class);
        List<Message> all = TestLoaderNG.load("tests.dumped");
        List<Message> responses = new LinkedList<Message>();
        for (Message m : all) {
            //System.out.println("Message = " + m);
            if (ParserType.HTTP_RESPONSE == m.type) {
                responses.add(m);
            }
        }
        for (Message m : responses) {
            test_message(m);
        }

        for (int i = 0; i != responses.size(); ++i) {
            if (!responses.get(i).should_keep_alive) continue;
            for (int j = 0; j != responses.size(); ++j) {
                if (!responses.get(j).should_keep_alive) continue;
                for (int k = 0; k != responses.size(); ++k) {
                    test_multiple3(responses.get(i), responses.get(j), responses.get(k));
                }
            }
        }

        // not sure what test_message_count_body does that test_message doesn't...
        //   Message m = find(responses, "404 no headers no body");
        //   test_message_count_body(m);
        //           m = find(responses, "200 trailing space on chunked body");
        //   test_message_count_body(m);

        // TODO testParseUrl very large chunked response

        // test_scan is more or less the same as test_permutations, will implement later...
    }


}
