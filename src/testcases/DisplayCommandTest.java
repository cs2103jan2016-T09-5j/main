package testcases;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import logic.ClockWork;
import logic.DisplayCommand;
import logic.Todo;

 //@@author A0133247L
public class DisplayCommandTest {

    Collection<Todo> todos;

    ClockWork logic;
    
    @After
    public void tearDown() {
        logic.deleteStorageFile();
    }

    @Before
    public void setUp() throws Exception {

        String fileDirectory = ClockWork.getStorageFileDirFromSettings();
        logic = new ClockWork(fileDirectory);
        logic.reloadMemory();

        // try {
        logic.handleInput("add floating task");

        logic.handleInput("add CS3230 deadline on 9 March 9pm");

        logic.handleInput("add CIP event from 3 March at 10am to 3 March at 12pm");

        logic.handleInput("add new year from 1 January at 10am to 1 January at 11am");

        logic.handleInput("add CS1010 deadline by 3 Feb at 10pm");

        logic.handleInput("add read floating books");

        logic.handleInput("add CS3243 project deadline by 7 March at 9am");

        logic.handleInput("add CS3333 project 2 on 7 Apr 10am");

        logic.handleInput("add meet june from malaysia from 9pm on 9 march to 10pm on 10 march");

        logic.handleInput("mark 0");

        logic.handleInput("mark 2");

        todos = ClockWork.memory.getAllTodos();
    }

    @Test
    public void testDisplayChronoPending() {
        String expected = "Showing pending todos:\n"+"ID | Name                           | "
                + "Time\n\n..Fri 01 Jan 2016...\n3  | new year                       | "
                + "10:00 - 11:00\n\n..Wed 03 Feb 2016...\n4  | CS1010 deadline                | "
                + "22:00\n\n..Mon 07 Mar 2016...\n6  | CS3243 project deadline        | "
                + "09:00\n\n..Wed 09 Mar 2016...\n1  | CS3230 deadline                | "
                + "21:00\n8  | meet june from malaysia        | "
                + "21:00 - 23:59\n\n..Thu 10 Mar 2016...\n8  | meet june from malaysia        | "
                + "00:00 - 22:00\n\n..Thu 07 Apr 2016...\n7  | CS3333 project 2               | "
                + "10:00\n\n......Anytime.......\n5  | read floating books            | NIL\n";
        assertEquals(expected, DisplayCommand.getDisplayChrono(ClockWork.memory, 0));
    }

    @Test
    public void testDisplayChronoCompleted() {
        String expected = "Showing completed todos:\nID | Name                           "
                + "| Time\n\n..Thu 03 Mar 2016...\n2  | CIP event                      "
                + "| 10:00 - 12:00\n\n......Anytime.......\n0  | floating task                  "
                + "| NIL\n";
        assertEquals(expected, DisplayCommand.getDisplayChrono(ClockWork.memory, 1));
    }

}
