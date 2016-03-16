package com.clockwork;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.clockwork.exceptions.InvalidRecurringException;
import com.clockwork.exceptions.InvalidTodoNameException;
import com.clockwork.exceptions.ParsingFailureException;

public class ClashDetectorTest {
	
	//Stubbing standard input and output for mocked streams
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	Memory memory;
	ParsedInput input;
	Keywords addCommand = Keywords.ADD;
	ClockWork logic;

	public enum TYPE {
		ADD, MARK, DELETE, SEARCH, EDIT, DISPLAY, UNDO, ERROR;
	}

	private static final DateTimeFormatter DateFormatter = DateTimeFormat
			.forPattern("EEE dd MMM yyyy");
	private static final DateTimeFormatter TimeFormatter = DateTimeFormat
			.forPattern("HH:mm");

	private static String formatTime(DateTime time) {
		String timeString = TimeFormatter.print(time);
		return timeString;
	}

	private static String formatDate(DateTime time) {
		String dateString = DateFormatter.print(time);

		return dateString;
	}

	@Before
	public void setUp() {
		String fileDirectory = ClockWork.getStorageFileDirFromSettings();
		logic = new ClockWork(fileDirectory);
		logic.reloadMemory();
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	    System.setIn(System.in);
	}

	@After
	public void tearDown() {
		logic.deleteStorageFile();
	}
	
	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Test
	public void testEventWithDeadlineClash() {
		
		try {
			
			//We add a deadline and then purposely clash an event that overlaps with the deadline
			//logic.handleInput("add canoeing from 7am to 9am on 13 apr");
			//String promptUserToOverwrite = lastLineOfSeries(outContent);
			//The system should recognize the overlap and ask the use if they wish to proceed
			//assertEquals(Signal.CLASH_CONTINUE_PROPOSITION, promptUserToOverwrite);
			//If we do not want to overlap the events, the attempted event should not be added
			//logic.handleInput("no");
			//assertEquals(memory.getAllTodos().size(), 1);
			//We can re-attempt the add and choose yes to 'force' the overlap in our system
//			logic.handleInput("add canoeing from 7am to 9am on 13 apr");
//			promptUserToOverwrite = lastLineOfSeries(outContent);
//			//The system should recognize the overlap and ask the use if they wish to proceed
//			assertEquals(Signal.CLASH_CONTINUE_PROPOSITION, promptUserToOverwrite);
//			//If we do not want to overlap the events, the attempted event should not be added
//			logic.handleInput("yes");
			assertEquals(memory.getAllTodos().size(),logic.handleInput("add interview by 0800 on 13 Apr"));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e) {
		}

	}
	
	public void testDeadlineWithEventClash() {
		
	}
	
	@Test 
	public void testOverlappingEventClash() {
		
		
	}
	
	@Test
	public void testEventWithSameEndTimeClash() {
		
	}
	
	@Test
	public void testOverlappingDeadlineClash() {
		
	}
	
	//Sometimes we want to clean standard output to just read the last line or last couple of lines for convenience
	private String lastLineOfSeries(ByteArrayOutputStream outContent) {
		String paragraph = outContent.toString().trim();
		return paragraph.substring(paragraph.lastIndexOf("\n")).trim();
	}
	
	private String lastLines(ByteArrayOutputStream outContent, int[] targetIndicesFromLast) {
		String[] parsedInput = outContent.toString().split("\\n");
		String targetFormat = "";
		for(int i : targetIndicesFromLast) {
			targetFormat += parsedInput[parsedInput.length-1-i] + "\n";
		}
		return targetFormat;
	}
	
	//Stubbing sys in if necessary to mock user input
	private void spoofSystemInput(String input) {
		try {
			System.setIn(new ByteArrayInputStream(input.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}