
package ClockworkParser;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class ParsedInput {

	private Keywords type;
	private ArrayList<KeyParamPair> keyParamPairs;
	private List<DateTime> dateTimes;
	private boolean isRecurring;
	private Period period;
	private boolean hasPeriod;
	private boolean hasLimit;
	private DateTime limit;

	/**
	 * Creates a ParsedInput object with the type of command, a list of
	 * KeyParamPair objects and a list of DateTime objects derived from the user
	 * input String.
	 * 
	 * @param type
	 *            KEYWORDS specifying the type of the command.
	 * @param keyParamPairs
	 *            the list of KeyParamPair objects parsed from the input based
	 *            on the command type
	 * @param dateTimes
	 *            dateTime objects for todo startDate and endDates
	 * @param period
	 *            the period for the recurring todo
	 * @param isRecurring
	 *            whether the todo is recurring
	 * @param hasLimit
	 *            whether user input has a limit identified for recurring todo
	 * @param limit
	 *            the dateTime object as limit for recurring todo
	 */
	public ParsedInput(Keywords type, ArrayList<KeyParamPair> keyParamPairs,
			List<DateTime> dateTimes, Period period, boolean isRecurring,
			boolean hasLimit, DateTime limit) {
		this.type = type;
		this.keyParamPairs = keyParamPairs;
		this.dateTimes = dateTimes;
		this.period = period;
		this.hasPeriod = (period != null && !period.equals(new Period()));
		this.isRecurring = isRecurring;
		this.limit = limit;
		this.hasLimit = hasLimit;
	}

	/**
	 * Creates a ParsedInput object to represent an invalid command.
	 */
	private ParsedInput() {
		this.type = Keywords.ERROR;
	}

	public static ParsedInput getPlaceholder() {
		return new ParsedInput();
	}

	/**
	 * Retrieves the type of command specified by the input.
	 * 
	 * @return KEYWORDS specifying the type of the command.
	 */
	public Keywords getType() {
		return type;
	}

	/**
	 * Retrieves the list of KeyParamPair objects parsed from the input if any.
	 * 
	 * @return the list of KeyParamPair objects parsed from the input.
	 */
	public ArrayList<KeyParamPair> getParamPairs() {
		return keyParamPairs;
	}

	/**
	 * Retrieves the list of DateTime objects parsed from the input if any.
	 * 
	 * @return the list of DateTime objects parsed from the input.
	 */
	public List<DateTime> getDateTimes() {
		return dateTimes;
	}

	/**
	 * Checks if only the command keyword and its parameter is present.
	 * 
	 * @return true if there only the command keyword and its parameter is
	 *         present.
	 */
	public boolean containsOnlyCommand() {
		if (keyParamPairs.size() == 1 && dateTimes.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Iterates through the keyParamPair ArrayList and checks if any parameter
	 * is an empty string.
	 * 
	 * @return boolean If there is at least one empty string parameter, return
	 *         true. Else, return false.
	 */
	public boolean containsEmptyParams() {
		for (KeyParamPair pair : keyParamPairs) {
			if (pair.getParam().equals("")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Iterates through the keyParamPair ArrayList and checks if input contains
	 * given flagKeyword
	 * 
	 * @param flagKeyword
	 * @return if input contains given flagKeyword
	 */
	public boolean containsFlag(Keywords flagKeyword) {
		for (KeyParamPair pair : keyParamPairs) {
			if (pair.getKeyword() == flagKeyword
					&& InputStringKeyword.isFlag(pair.getKeyString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if any dates are parsed.
	 * 
	 * @return true if at least one date has been parsed.
	 */
	public boolean containDates() {
		if (dateTimes.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if parsedInput is pertaining to recurring tasks.
	 * 
	 * @return true if parsedInput is pertaining to recurring tasks.
	 */
	public boolean isRecurring() {
		return isRecurring;
	}

	/**
	 * Returns the period in object
	 * @return period of the object
	 */
	public Period getPeriod() {
		return period;
	}

	/**
	 * Checks if parsedInput has a period
	 * @return true if parsedInput has a period
	 */
	public boolean hasPeriod() {
		return hasPeriod;
	}

	/**
	 * Checks if parsedInput has a limit
	 * @return true if parsedInput has a limit
	 */
	public boolean hasLimit() {
		return hasLimit;
	}

	/**
	 * Retrieves the limit of the parsedInput object
	 * @return limit as DateTime object
	 */
	public DateTime getLimit() {
		return limit;
	}

	@Override
	public boolean equals(Object o) {
		if (o.getClass().equals(this.getClass())) {
			ParsedInput other = (ParsedInput) o;

			return this.getType().equals(other.getType())
					&& this.getParamPairs().equals(other.getParamPairs())
					&& this.getDateTimes().equals(other.getDateTimes())
					&& this.getPeriod().equals(other.getPeriod())
					&& this.isRecurring() == other.isRecurring()
					&& this.hasLimit() == other.hasLimit()
					&& this.getLimit().equals(other.getLimit());
		}
		return false;
	}
}
