package main;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;

public class EditCommandTest {
	Memory memory = new Memory();
	
	String startTime = "midnight 29 Sep 2016";
	String endTime = "1859h 10 Nov 2016";
	String title = "Meet parents for dinner";
	String editedStart = "7pm 6 May";
	String editedEnd = "2100h 6 May";

	@Test
	public void test() throws NullTodoException, DateUndefinedException {
		Todo actual = new Todo(memory, "Project meeting", startTime + endTime);
		memory.add(actual);
		ArrayList<KeyParamPair> paramList = new ArrayList<KeyParamPair>();
		paramList.add(new KeyParamPair("", Integer.toString(actual.getId())));
		paramList.add(new KeyParamPair("title", title));
		paramList.add(new KeyParamPair("start", editedStart));
		paramList.add(new KeyParamPair("end", editedEnd));
		ParsedInput input = new ParsedInput(KEYWORDS.EDIT, paramList);
		EditCommand c = new EditCommand(input, memory);
		c.execute();
		
		Todo expectedTodo = new Todo(memory, title, editedStart + " to " + editedEnd);
		assertEquals(expectedTodo, memory.get(actual.getId()));
		
	}

}
