package testcases;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exceptions.InvalidRecurringException;
import exceptions.InvalidTodoNameException;
import exceptions.ParsingFailureException;
import logic.ClockWork;
import logic.Signal;

import static org.junit.Assert.assertEquals;

//@@author A0126219J
public class MarkCommandTest {
	private ClockWork logic;
	private static final String COMMAND_1 = "add floatingTask";
	private static final String COMMAND_2 = "add deadline by 3pm on 5 jun";
	private static final String COMMAND_3 = "add event from 4pm to 5pm on 7 jul";
	
	@Before
	public void setUp(){
		String fileDirectory = ClockWork.getStorageFileDirFromSettings();
		logic = new ClockWork(fileDirectory);
		logic.reloadMemory();
			try{
				logic.handleInput(COMMAND_1);
				logic.handleInput(COMMAND_2);
				logic.handleInput(COMMAND_3);
			} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e){
			}
		}
	
	@After
	public void tearDown(){
		logic.deleteStorageFile();
	}
	
	@Test
	public void testMark(){
		String markCommand;
		String markString;
		Signal markSuccess;
		
		/*
		 * Test marking of Todo with index 0
		 */
		markCommand = "mark 0";
		markString = "Floating task \"floatingTask\"";
		markSuccess = new Signal(String.format(Signal.MARK_SUCCESS_SIGNAL_FORMAT, markString), true);
		try{
			assertEquals(markSuccess, logic.handleInput(markCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e){
		}
		
		/*
		 * Test marking of Todo with index 1
		 */
		markCommand = "mark 1";
		markString = "Deadline \"deadline\" by Sun 05 Jun 2016 at 15:00";
		markSuccess = new Signal(String.format(Signal.MARK_SUCCESS_SIGNAL_FORMAT, markString), true);
		try{
			assertEquals(markSuccess, logic.handleInput(markCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e){
		}
		
		/*
		 * Test marking of Todo with index 2
		 */
		markCommand = "mark 2";
		markString = "Event \"event\" from Thu 07 Jul 2016 at 16:00 to Thu 07 Jul 2016 at 17:00";
		markSuccess = new Signal(String.format(Signal.MARK_SUCCESS_SIGNAL_FORMAT, markString), true);
		try{
			assertEquals(markSuccess, logic.handleInput(markCommand));
		} catch (InvalidRecurringException | InvalidTodoNameException | ParsingFailureException e){
		}
	}
}
