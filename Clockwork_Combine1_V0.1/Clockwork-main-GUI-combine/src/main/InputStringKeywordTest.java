package main;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class InputStringKeywordTest {

    String s = "add first word";
    String s2 = "add";
    String s3 = "nonkeyword";

    @Test
    public void testIsKeyword() {

        assertEquals(false, InputStringKeyword.isKeyword(s));
        assertEquals(true, InputStringKeyword.isKeyword(s2));
        assertEquals(false, InputStringKeyword.isKeyword(s3));
    }
}
