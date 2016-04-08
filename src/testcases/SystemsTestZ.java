package testcases;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exceptions.InvalidRecurringException;
import exceptions.InvalidTodoNameException;
import exceptions.ParsingFailureException;
import logic.ClockWork;
import logic.DisplayCommand;
import logic.Todo;


// We decided to do system testing with a text file containing commands 
// and compare output with expected output.
// For example: java -jar 048.jar < test_commands.txt > output.txt
public class SystemsTestZ {

    Collection<Todo> todos;
    ClockWork logic;

    //@@author Morgan
    @Before
    public void setUp() {
    	String fileDirectory = ClockWork.getStorageFileDirFromSettings();
        logic = new ClockWork(fileDirectory);
        logic.reloadMemory();
        
    }

    @After
    public void tearDown() {
    	logic.deleteStorageFile();
    }

    @Test
    public void testAll() throws InvalidRecurringException,
            InvalidTodoNameException, ParsingFailureException {

        logic.handleInput("add floating task");

        logic.handleInput("add CS2103 deadline on 9 March 9pm");

        logic.handleInput("add CIP event from 3 March at 10am to 3 March at 12pm");

        logic.handleInput("add new year on 1 January every year");

        logic.handleInput("add CS1010 deadline by 3 Feb at 10pm");

        logic.handleInput("add read floating books");

        logic.handleInput("add EE2024 project deadline by 7 March at 9am");

        logic.handleInput("add CG2271 project 2 on 7 Apr 10am");

        logic.handleInput("mark 0");

        logic.handleInput("mark 2");

        logic.handleInput("search read");

        logic.handleInput("add go to NUS hackers on 3 apr every friday until 4 apr");

        logic.handleInput("add learn something every sunday");

        logic.handleInput("search learn");

        logic.handleInput("undo");

        logic.handleInput("undo");

        logic.handleInput("redo");

        logic.handleInput("mark 1");

        logic.handleInput("undo");

        logic.handleInput("mark 2");

        logic.handleInput("search new");

        logic.handleInput("search NUS");

        logic.handleInput("edit 3 last new year");

        logic.handleInput("search new");

        logic.handleInput("delete 6");

        logic.handleInput("display");

        todos = ClockWork.memory.getAllTodos();

        String expected = "Showing pending todos:\n"
                + "ID | Name                           | Time\n\n"
                + "..Fri 01 Jan 2016...\n3  | last new year                  | 23:59\n\n"
                + "..Wed 03 Feb 2016...\n5  | CS1010 deadline                | 22:00\n\n"
                + "..Mon 07 Mar 2016...\n7  | EE2024 project deadline        | 09:00\n\n"
                + "..Wed 09 Mar 2016...\n1  | CS2103 deadline                | 21:00\n\n"
                + "..Thu 03 Apr 2016...\n9  | (Recurring) go to NUS hackers  | 23:59\n\n"
                + "..Fri 07 Apr 2016...\n8  | CG2271 project 2               | 10:00\n\n"
                + "..Sun 01 Jan 2017...\n4  | (Recurring) new year           | 23:59\n";
        assertEquals(expected, DisplayCommand.getDisplayString(todos, 0));

    }

}
