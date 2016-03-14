
package ClockworkTestcases;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ClockworkParser.InputStringKeyword;
import ClockworkLogic.Keywords;

public class InputStringKeywordTest {

	@Test
	public void testIsFlag() {
		String s = "-y";
		String s1 = "nonflag";
		assertEquals(true, InputStringKeyword.isFlag(s));
		assertEquals(false, InputStringKeyword.isFlag(s1));
	}

	@Test
	public void testIsCommand() {
		String s = "add";
		String s1 = "noncommand";
		assertEquals(true, InputStringKeyword.isCommand(s));
		assertEquals(false, InputStringKeyword.isCommand(s1));
	}

	@Test
	public void testIsKeyword() {
		String s = "add";
		String s1 = "nonkeyword";
		assertEquals(true, InputStringKeyword.isKeyword(s));
		assertEquals(false, InputStringKeyword.isKeyword(s1));
	}

	@Test
	public void testGetFlag() {
		String s = "-d";
		String s2 = "nonflag";
		assertEquals(Keywords.DAY, InputStringKeyword.getFlag(s));
		assertEquals(Keywords.ERROR, InputStringKeyword.getFlag(s2));
	}

	@Test
	public void testGetCommand() {
		String s = "delete";
		String s1 = "noncommand";
		assertEquals(Keywords.DELETE, InputStringKeyword.getCommand(s));
		assertEquals(Keywords.ERROR, InputStringKeyword.getCommand(s1));
	}

	@Test
	public void testGetKeyword() {
		String s = "on";
		String s1 = "nonkeyword";
		assertEquals(Keywords.ON, InputStringKeyword.getKeyword(s));
		assertEquals(Keywords.ERROR, InputStringKeyword.getKeyword(s1));
	}
}
