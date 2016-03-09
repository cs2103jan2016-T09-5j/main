package main;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DisplayCommandTest {
    Collection<Todo> todos;

    @Before
    public void setUp() throws Exception{

        // try {
    	ClockworkGUI.handleInput("add floating task");

    	ClockworkGUI.handleInput("add EE2024 deadline 6 March at 9pm");

    	ClockworkGUI
                .handleInput("add CIP event from 3 March at 10am to 3 March at 12pm");

    	ClockworkGUI
                .handleInput("add new year from 1 January at 10am to 1 January at 11am");

    	ClockworkGUI.handleInput("add CS1010 deadline by 3 Feb at 10pm");

    	ClockworkGUI.handleInput("add read floating books");

    	ClockworkGUI.handleInput("add CG2271 project deadline by 7 March at 9am");

    	ClockworkGUI.handleInput("mark 0");

    	ClockworkGUI.handleInput("mark 2");

        todos = ClockworkGUI.memory.getAllTodos();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDisplayDefaultPending() {
        String expected = "Pending:\n" + "         floating task            "
                + "\n06 Mar   EE2024 deadline           21:00"
                + "\n         read floating b          "
                + "\n07 Mar   CG2271 project            09:00" + "\n";
        assertEquals(expected, DisplayCommand.getDisplayDefault(todos, 0));
    }

    @Test
    public void testDisplayChronoPending() {
        String expected = "Pending:\n"
                + "06 Mar   EE2024 deadline           21:00"
                + "\n07 Mar   CG2271 project            09:00"
                + "\n         floating task            "
                + "\n         read floating b          " + "\n";
        assertEquals(expected, DisplayCommand.getDisplayChrono(todos, 0));
    }

    @Test
    public void testDisplayDefaultCompleted() {
        String expected = "Completed:\n" + "         eat more                 "
                + "\n03 Mar   CIP event                 10:00 - 12:00"
                + "\n01 Jan   new year                  10:00 - 11:00"
                + "\n03 Feb   CS1010 deadline           22:00"
                + "\n";
        assertEquals(expected, DisplayCommand.getDisplayDefault(todos, 1));
    }

    @Test
    public void testDisplayChronoCompleted() {
        String expected = "Completed:\n"
                + "01 Jan   new year                  10:00 - 11:00"
                + "\n03 Feb   CS1010 deadline           22:00"
                + "\n03 Mar   CIP event                 10:00 - 12:00"
                + "\n         eat more                 " + "\n";

        assertEquals(expected, DisplayCommand.getDisplayChrono(todos, 1));
    }

}
