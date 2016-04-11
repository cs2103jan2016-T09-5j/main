package testcases;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Test;

import exceptions.InvalidDateException;
import logic.RecurringTodoRule;
import parser.Parser;
import storage.Memory;

//@@author A0126219J
public class RecurringTodoRuleTest {

	@Test
    public void testUpdateTodoList() throws InvalidDateException {
        Period periodWeek = new Period().withWeeks(1);
        Period periodMonth = new Period().withMonths(1);
        List<DateTime> pastArrayList;
        DateTime past;

        // Test the case where the limit is in the past and is before the
        // initial date, the recurrence does not happen, only the initial todo
        // is added
        pastArrayList = Parser.parseDates("4 March 4pm");
        past = pastArrayList.get(0);
        DateTime pastLimitBefore = past.minus(2);
        RecurringTodoRule ruleBefore = new RecurringTodoRule(0,
                "past deadline", pastArrayList, periodWeek, pastLimitBefore);
        assertEquals(1, ruleBefore.updateTodoList(new Memory()));

        // Test the case where the limit is in the past and is immediately after
        // the initial date, the todo does not recur no new todos should be
        // added except the initial one
        pastArrayList = Parser.parseDates("4 March 4pm");
        past = pastArrayList.get(0);
        DateTime pastLimitImmediate = past.plus(2);
        RecurringTodoRule ruleImmediate = new RecurringTodoRule(0,
                "past deadline", pastArrayList, periodWeek, pastLimitImmediate);
        assertEquals(1, ruleImmediate.updateTodoList(new Memory()));
        
        // Test the case where the limit is in the past and is after the initial
        // date, the rule recurs once, 2 new todos should be added
        pastArrayList = Parser.parseDates("4 March 4pm");
        past = pastArrayList.get(0);
        DateTime pastLimitOneWeek = past.plus(periodWeek).plus(2);
        RecurringTodoRule ruleOneWeek = new RecurringTodoRule(0,
                "past deadline", pastArrayList, periodWeek, pastLimitOneWeek);
        assertEquals(2, ruleOneWeek.updateTodoList(new Memory()));

        // Test the case where the limit is in distant future, initial todo is
        // in near past, add 2 new todos until the next occurrence in the future
        past = new DateTime().minus(2);
        pastArrayList = new ArrayList<DateTime>();
        pastArrayList.add(past);
        DateTime futureLimitOneMonth = past.plus(periodMonth).plus(2);
        RecurringTodoRule ruleFuture = new RecurringTodoRule(0,
                "past deadline", pastArrayList, periodWeek, futureLimitOneMonth);
        assertEquals(2, ruleFuture.updateTodoList(new Memory()));

        // Test the case where the limit is in distant future, initial todo is
        // in distance past, add 4 new todos until the next occurrence in the
        // future
        past = new DateTime().minus(periodWeek).minus(periodWeek).minus(2);
        pastArrayList = new ArrayList<DateTime>();
        pastArrayList.add(past);
        RecurringTodoRule ruleFuture2 = new RecurringTodoRule(0,
                "past deadline", pastArrayList, periodWeek, futureLimitOneMonth);
        assertEquals(4, ruleFuture2.updateTodoList(new Memory()));

    }

}
